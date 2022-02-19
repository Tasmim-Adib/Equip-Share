package com.example.equipshare;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private TextInputLayout nameTextInputLayout,contactTextInputLayout,keyTextInputLayout;
    private TextInputEditText nameTextInputEditText, keyTextInputEditText, contactTextInputEditText;
    private Button submitButton,agreeButton;
    private TextView nameTextView,deptTextView,labNameTextView;
    private String currentUser;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference,keyCheckRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        nameTextInputLayout = findViewById(R.id.nameTextInputLayout);
        keyTextInputLayout = findViewById(R.id.keyTextInputLayout);
        contactTextInputLayout = findViewById(R.id.contactTextInputLayout);
        nameTextInputEditText = findViewById(R.id.nameEditText);
        keyTextInputEditText = findViewById(R.id.keyEditText);
        contactTextInputEditText = findViewById(R.id.contactEditText);
        submitButton = findViewById(R.id.submitButton);
        nameTextView = findViewById(R.id.nameTextView);
        deptTextView = findViewById(R.id.deptTextView);
        labNameTextView = findViewById(R.id.labNameTextView);
        agreeButton = findViewById(R.id.agreeButton);
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        keyCheckRef = FirebaseDatabase.getInstance().getReference("Keys");

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                assignUser();
            }
        });

        agreeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitDetails();
            }
        });
    }

    private void submitDetails() {
        String name = nameTextInputEditText.getText().toString();
        String secretKey = keyTextInputEditText.getText().toString();
        String contact = contactTextInputEditText.getText().toString();
        HashMap hashMap = new HashMap();
        hashMap.put("Name",name);
        hashMap.put("Contact",contact);
        hashMap.put("SecretKey",secretKey);
        databaseReference.child(currentUser).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getApplicationContext(),"Successfully Registered !!",Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
        Intent intent = new Intent(getApplicationContext(),DashboardActivity.class);
        startActivity(intent);
        finish();

    }

    private void assignUser() {
        String name = nameTextInputEditText.getText().toString();
        String secretKey = keyTextInputEditText.getText().toString();
        String contact = contactTextInputEditText.getText().toString();

        if (TextUtils.isEmpty(name)){
            nameTextInputEditText.setError("Don't You Have any Name ?");
            nameTextInputEditText.requestFocus();
        }

        else if(TextUtils.isEmpty(secretKey)){
            keyTextInputEditText.setError("Insert the secret Key");
            keyTextInputEditText.requestFocus();
        }
        else if(TextUtils.isEmpty(contact)){
            contactTextInputEditText.setError("Insert the secret Key");
            contactTextInputEditText.requestFocus();
        }
        else{
            keyCheckRef.child(secretKey).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        String labName = snapshot.child("LabName").getValue().toString();
                        String department = snapshot.child("Department").getValue().toString();
                        nameTextView.setText(name);
                        labNameTextView.setText("Lab : " + labName);
                        deptTextView.setText("Department : " + department);
                        visibleField();
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Invalid Security Key",Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    private void visibleField() {
        nameTextView.setVisibility(View.VISIBLE);
        deptTextView.setVisibility(View.VISIBLE);
        labNameTextView.setVisibility(View.VISIBLE);
        agreeButton.setVisibility(View.VISIBLE);
        submitButton.setVisibility(View.GONE);
        nameTextInputEditText.setVisibility(View.GONE);
        keyTextInputEditText.setVisibility(View.GONE);
        contactTextInputEditText.setVisibility(View.GONE);
        contactTextInputLayout.setVisibility(View.GONE);
        keyTextInputLayout.setVisibility(View.GONE);
        nameTextInputLayout.setVisibility(View.GONE);
    }
}