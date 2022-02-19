package com.example.equipshare;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class HomeFragment extends Fragment {
    private DatabaseReference findCurrentUserKeyRef,retrieveReagentRef;
    private RecyclerView recyclerView;
    private String currentUser,quantity;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView =(RecyclerView) view.findViewById(R.id.fragment_home_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        findCurrentUserKeyRef = FirebaseDatabase.getInstance().getReference("Users");
        retrieveReagentRef = FirebaseDatabase.getInstance().getReference().child("Reagent");

        findCurrentUserKeyRef.child(currentUser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String secretKey = snapshot.child("SecretKey").getValue().toString();
               retrieveInfo(secretKey);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }

    private void retrieveInfo(String secretKey) {
        Query query = FirebaseDatabase.getInstance().getReference().child("Reagent").orderByChild("SecretKey").equalTo(secretKey);
        FirebaseRecyclerOptions<Reagent> options = new
                FirebaseRecyclerOptions.Builder<Reagent>()
                .setQuery(query,Reagent.class)
                .build();
        FirebaseRecyclerAdapter<Reagent, ReagenetViewHolder> adapter =
                new FirebaseRecyclerAdapter<Reagent, ReagenetViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ReagenetViewHolder holder, int position, @NonNull Reagent model) {
                        String id = getRef(position).getKey();
                        retrieveReagentRef.child(id).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String n = snapshot.child("Name").getValue().toString();
                                String q = snapshot.child("Quantity").getValue().toString();
                                setQuantity(q);
                                holder.name.setText(n);;
                                holder.quantity.setText(q);
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
                                args.putString("FRAGMENT_N0","1");
                                args.putString("QUANTITY",getQuantity());
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
                    public ReagenetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerviewitem,parent,false);
                        return new ReagenetViewHolder(v);
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public static class ReagenetViewHolder extends RecyclerView.ViewHolder{
        TextView name,quantity;
        public ReagenetViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.reagentNameTextView);
            quantity = itemView.findViewById(R.id.quantityTextView);

        }
    }

}


