package com.example.equipshare;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AddReagentFragment extends Fragment {
    private TextInputEditText reagentNameEditText,quantityEditText,originEditText,companyNameEditText,
            catalogNoEditText;
    private Button reagentDoneButton;
    private View view;
    String currentUser,secretKey;
    private DatabaseReference findKeyRef,saveReagentRef;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_reagent, container, false);

        currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();

        reagentNameEditText = view.findViewById(R.id.reagentNameEditText);
        quantityEditText = view.findViewById(R.id.reagentQuantityEditText);
        originEditText = view.findViewById(R.id.reagentOriginEditText);
        companyNameEditText = view.findViewById(R.id.reagentCompanyEditText);
        catalogNoEditText = view.findViewById(R.id.reagentCatalogEditText);
        reagentDoneButton = view.findViewById(R.id.reagentDoneButton);
        findKeyRef = FirebaseDatabase.getInstance().getReference("Users");
        findKeyRef.child(currentUser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    secretKey = snapshot.child("SecretKey").getValue().toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        saveReagentRef = FirebaseDatabase.getInstance().getReference("Reagent");
        reagentDoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveReagentInfo();
            }
        });
        return view;
    }

    private void saveReagentInfo() {

        String name = reagentNameEditText.getText().toString();
        String quantity = quantityEditText.getText().toString();
        String origin = originEditText.getText().toString();
        String company = companyNameEditText.getText().toString();
        String catalog = catalogNoEditText.getText().toString();

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Calendar c = Calendar.getInstance();
        String date = sdf.format(c.getTime());

        if(TextUtils.isEmpty(name)){
            reagentNameEditText.setError("Name is Required !!");
            reagentNameEditText.requestFocus();
        }
        else if(TextUtils.isEmpty(quantity)){
            quantityEditText.setError("Quantity is Required !!");
            quantityEditText.requestFocus();
        }
        else if(TextUtils.isEmpty(origin)){
            originEditText.setError("Origin is Required !!");
            originEditText.requestFocus();
        }
        else if(TextUtils.isEmpty(company)){
            companyNameEditText.setError("Company Name is Required");
            companyNameEditText.requestFocus();
        }
        else{
            HashMap hashMap = new HashMap();
            hashMap.put("Name",name);
            hashMap.put("Quantity",quantity);
            hashMap.put("Origin",origin);
            hashMap.put("Company",company);
            hashMap.put("Catalog",catalog);
            hashMap.put("AddedBy",currentUser);
            hashMap.put("Date",date);
            hashMap.put("SecretKey",secretKey);
            String key = saveReagentRef.push().getKey().toString();
            saveReagentRef.child(key).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(getContext(),"Reagent Added Successfully !!",Toast.LENGTH_SHORT).show();
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