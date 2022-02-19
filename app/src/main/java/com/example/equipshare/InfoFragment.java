package com.example.equipshare;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class InfoFragment extends Fragment {
    private TextView title, subTitle;
    private RecyclerView recyclerView;
    private String currentUser, key;
    private DatabaseReference keyRef,userRef,labDeptRef;
    private Button logoutBtn;
    private FirebaseAuth mAuth;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info, container, false);

        title = view.findViewById(R.id.infoFragmentTitle);
        subTitle = view.findViewById(R.id.infoFragmentSubTitle);
        recyclerView = view.findViewById(R.id.fragment_info_recyclerView);
        currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mAuth = FirebaseAuth.getInstance();


        keyRef = FirebaseDatabase.getInstance().getReference().child("Users");
        labDeptRef = FirebaseDatabase.getInstance().getReference().child("Keys");
        logoutBtn = view.findViewById(R.id.LogoutButton);
        keyRef.child(currentUser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String k = snapshot.child("SecretKey").getValue().toString();
                setKey(k);
                labDeptRef.child(k).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String t = snapshot.child("Department").getValue().toString();
                        String sT = snapshot.child("LabName").getValue().toString();
                        title.setText(t);
                        subTitle.setText(sT);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        retrieveInfo();

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                Intent intent = new Intent(getContext(),MainActivity.class);
                startActivity(intent);
                getActivity().finish();

            }
        });

        return view;
    }

    private void retrieveInfo() {
        Query query = FirebaseDatabase.getInstance().getReference().child("Users").orderByChild("SecretKey").equalTo(getKey());

        FirebaseRecyclerOptions<Reagent> options = new
                FirebaseRecyclerOptions.Builder<Reagent>()
                .setQuery(query,Reagent.class)
                .build();

        FirebaseRecyclerAdapter<Reagent, UserViewHolder> adapter =
                new FirebaseRecyclerAdapter<Reagent, UserViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull UserViewHolder holder, int position, @NonNull Reagent model) {
                        String id = getRef(position).getKey();
                        keyRef.child(id).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String a = snapshot.child("Name").getValue().toString();
                                String b = snapshot.child("Contact").getValue().toString();
                                holder.name.setText(a);
                                holder.contact.setText(b);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }

                    @NonNull
                    @Override
                    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.inforrecycleritem,parent,false);
                        return new UserViewHolder(v);
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder{

        TextView name,contact;
        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.infoNameTextView);
            contact = itemView.findViewById(R.id.infoContactTextView);
        }
    }

}