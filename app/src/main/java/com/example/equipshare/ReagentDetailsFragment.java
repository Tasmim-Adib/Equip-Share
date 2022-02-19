package com.example.equipshare;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ReagentDetailsFragment extends Fragment {

    private DatabaseReference retrieveReagentRef, secretKeyRef,userRef,shareRef,updateRef;
    private TextView nameTV, quantityTV, companyTV, dateTV, addedByTV, originTV, labTV, deptTV, catalogTV;
    private Button updateButton, deleteButton, shareButton, okButton, shareUpdateBtn, shareRemoveBtn;
    private EditText editText;
    private int clickBtn;
    private String reagentName, identifyingKey, reagenterId;
    public ReagentDetailsFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reagent_details, container, false);
        String reagentId = getArguments().getString("REAGENT_ID");
        setReagenterId(reagentId);
        String fragmentNumber = getArguments().getString("FRAGMENT_N0");
        String fragmentQuantity = getArguments().getString("QUANTITY");
        retrieveReagentRef = FirebaseDatabase.getInstance().getReference().child("Reagent").child(reagentId);
        secretKeyRef = FirebaseDatabase.getInstance().getReference().child("Keys");
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        shareRef = FirebaseDatabase.getInstance().getReference().child("Shared");

        nameTV = view.findViewById(R.id.reagentDetailsName);
        quantityTV = view.findViewById(R.id.reagentDetailsquantity);
        companyTV = view.findViewById(R.id.reagentDetailsCompany);
        dateTV = view.findViewById(R.id.reagentDetailsdate);
        addedByTV = view.findViewById(R.id.reagentDetailsperson);
        originTV = view.findViewById(R.id.reagentDetailsOrigin);
        labTV = view.findViewById(R.id.reagentDetailslab);
        deptTV = view.findViewById(R.id.reagentDetailsDept);
        catalogTV = view.findViewById(R.id.reagentDetailsCatalog);
        updateButton = view.findViewById(R.id.reagentDetailsUpdateBtn);
        shareButton = view.findViewById(R.id.reagentDetailsShareBtn);
        deleteButton = view.findViewById(R.id.reagentDetailsDeleteBtn);
        okButton = view.findViewById(R.id.reagentDetailsOkBtn);
        editText = view.findViewById(R.id.reagentDetailsEditText);
        shareUpdateBtn = view.findViewById(R.id.reagentDetailsUpdateShareBtn);
        shareRemoveBtn = view.findViewById(R.id.reagentDetailsremoveShareBtn);
        if(fragmentNumber.equals("2")){ //department fragment
            updateButton.setVisibility(View.GONE);
            shareButton.setVisibility(View.GONE);
            deleteButton.setVisibility(View.GONE);
            shareUpdateBtn.setVisibility(View.VISIBLE);
            shareRemoveBtn.setVisibility(View.VISIBLE);
        }
        if(fragmentNumber.equals("3")){
            updateButton.setVisibility(View.GONE);
            shareButton.setVisibility(View.GONE);
            deleteButton.setVisibility(View.GONE);
        }
        retrieveInfo(fragmentQuantity);


        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeVisible();
                editText.setHint("Quantity");
                clickBtn = 1;
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeVisible();
                editText.setHint("Maximum Quantity");
                clickBtn = 2;
            }
        });
        shareUpdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeVisible();
                editText.setHint("Quantity");
                shareUpdateBtn.setVisibility(View.GONE);
                shareRemoveBtn.setVisibility(View.GONE);
                clickBtn = 3;
            }
        });

        shareRemoveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeShareItem();
            }
        });
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateOrShare();
            }
        });
        return view;
    }

    private void retrieveInfo(String fragmentQuantity) {
        retrieveReagentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String name = snapshot.child("Name").getValue().toString();
                    setReagentName(name);

                    String company = snapshot.child("Company").getValue().toString();
                    String origin = snapshot.child("Origin").getValue().toString();
                    String catalog = snapshot.child("Catalog").getValue().toString();
                    String date = snapshot.child("Date").getValue().toString();
                    String secretKey = snapshot.child("SecretKey").getValue().toString();
                    setIdentifyingKey(secretKey);
                    String userId = snapshot.child("AddedBy").getValue().toString();

                    nameTV.setText(name);
                    quantityTV.setText("Quantity : " + fragmentQuantity);
                    companyTV.setText("Company : " + company);
                    originTV.setText("Origin : " + origin);
                    catalogTV.setText("Catalog No : " + catalog);
                    dateTV.setText("Added When : " + date);

                    userRef.child(userId).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String userName = snapshot.child("Name").getValue().toString();
                            addedByTV.setText("Added By : "+userName);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    secretKeyRef.child(secretKey).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String lab = snapshot.child("LabName").getValue().toString();
                            String dept = snapshot.child("Department").getValue().toString();
                            labTV.setText("Lab : "+lab);
                            deptTV.setText("Department : "+dept);
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
    }

    private void makeVisible(){
        editText.setVisibility(View.VISIBLE);
        okButton.setVisibility(View.VISIBLE);
        updateButton.setVisibility(View.GONE);
        deleteButton.setVisibility(View.GONE);
        shareButton.setVisibility(View.GONE);
    }


    private void updateOrShare(){
        String newQuantity = editText.getText().toString();
        if(TextUtils.isEmpty(newQuantity)){
            editText.setError("Set quantity");
            editText.requestFocus();
            return;
        }
        else{
            if(clickBtn == 1){
                retrieveReagentRef.child("Quantity").setValue(newQuantity).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        makeRevisible();
                        Toast.makeText(getContext(),"Successfully Updated !!", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(),e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            else if(clickBtn == 2){
                HashMap hashMap = new HashMap();
                hashMap.put("Name",getReagentName());
                hashMap.put("SecretKey",getIdentifyingKey());
                hashMap.put("Quantity",newQuantity);
                shareRef.child(getReagenterId()).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        makeRevisible();
                        shareButton.setText("Shared");
                        Toast.makeText(getContext(),"This item is shared !!",Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
            }

            else if(clickBtn == 3){
                shareRef.child(getReagenterId()).child("Quantity").setValue(newQuantity).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        quantityTV.setText(newQuantity);
                        Toast.makeText(getContext(),"Shared Quantity Updated !!",Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
            }

        }
    }

    private void makeRevisible(){
        editText.setVisibility(View.GONE);
        okButton.setVisibility(View.GONE);
        updateButton.setVisibility(View.VISIBLE);
        deleteButton.setVisibility(View.VISIBLE);
        shareButton.setVisibility(View.VISIBLE);
    }
    private void removeShareItem(){
        Fragment fragment = new DepartmentFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer, fragment);
        fragmentTransaction.commit();
        shareRef.child(getReagenterId()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }
    public String getReagentName() {
        return reagentName;
    }

    public void setReagentName(String reagentName) {
        this.reagentName = reagentName;
    }

    public String getIdentifyingKey() {
        return identifyingKey;
    }

    public void setIdentifyingKey(String identifyingKey) {
        this.identifyingKey = identifyingKey;
    }

    public String getReagenterId() {
        return reagenterId;
    }

    public void setReagenterId(String reagenterId) {
        this.reagenterId = reagenterId;
    }
}