package com.example.equipshare;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class SearchFragment extends Fragment {

    private RecyclerView recyclerView;
    private String currentUser, quant;
    private DatabaseReference keyRef,labDeptRef,retrieveReagentRef;
    private TextInputEditText searchEditText;
    private Button searchButton;
    private TextInputLayout searchTextInputLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
        recyclerView =(RecyclerView) view.findViewById(R.id.fragment_search_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        searchEditText = view.findViewById(R.id.searchEditText);
        searchButton = view.findViewById(R.id.SearchButton);
        searchTextInputLayout = view.findViewById(R.id.searchTextInputLayout);

        keyRef = FirebaseDatabase.getInstance().getReference().child("Users");
        labDeptRef = FirebaseDatabase.getInstance().getReference().child("Keys");
        retrieveReagentRef = FirebaseDatabase.getInstance().getReference().child("Shared");

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                retrieveInfo();
            }
        });

        return view;
    }

    private void retrieveInfo() {
        String reagentName = searchEditText.getText().toString();
        searchEditText.setVisibility(View.GONE);
        searchButton.setVisibility(View.GONE);
        searchTextInputLayout.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        Query query = FirebaseDatabase.getInstance().getReference().child("Shared").orderByChild("Name").equalTo(reagentName);
        FirebaseRecyclerOptions<Reagent> options = new
                FirebaseRecyclerOptions.Builder<Reagent>()
                .setQuery(query,Reagent.class)
                .build();

        FirebaseRecyclerAdapter<Reagent,SearchReagentViewHolder> adapter =
                new FirebaseRecyclerAdapter<Reagent, SearchReagentViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull SearchReagentViewHolder holder, int position, @NonNull Reagent model) {
                        String id = getRef(position).getKey();
                        retrieveReagentRef.child(id).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists()){
                                    String name = snapshot.child("Name").getValue().toString();
                                    String quantity = snapshot.child("Quantity").getValue().toString();
                                    String key = snapshot.child("SecretKey").getValue().toString();
                                    setQuant(quantity);
                                    holder.name.setText(name);
                                    holder.quantity.setText(quantity);

                                    labDeptRef.child(key).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            String lab = snapshot.child("LabName").getValue().toString();
                                            String dept = snapshot.child("Department").getValue().toString();

                                            holder.lab.setText(lab);
                                            holder.dept.setText(dept);
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Fragment fragment = new ReagentDetailsFragment();
                                Bundle args = new Bundle();

                                args.putString("REAGENT_ID", id);
                                args.putString("FRAGMENT_N0","3");
                                args.putString("QUANTITY",getQuant());
                                fragment.setArguments(args);
                                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.replace(R.id.fragmentContainer, fragment);
                                fragmentTransaction.commit();
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public SearchReagentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.searchrecycleritem,parent,false);
                        return new SearchReagentViewHolder(v);
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    public String getQuant() {
        return quant;
    }

    public void setQuant(String quant) {
        this.quant = quant;
    }

    public static class SearchReagentViewHolder extends RecyclerView.ViewHolder{
        TextView name,quantity,lab,dept;
        public SearchReagentViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.searchReagentNameTextView);
            quantity = itemView.findViewById(R.id.searchQuantityTextView);
            lab = itemView.findViewById(R.id.searchLabNameTextView);
            dept = itemView.findViewById(R.id.searchDepartmentTextView);

        }
    }
}