package com.example.equipshare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.HashMap;
import java.util.Map;

public class SetSecurityKey extends AppCompatActivity {

    private EditText secretKeyTextInputEditText,labTextInputEditText,deptTextInputEditText;
    private Button okButton;
    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_security_key);

        secretKeyTextInputEditText = findViewById(R.id.sskEditText);
        labTextInputEditText = findViewById(R.id.ssklEditText);
        deptTextInputEditText = findViewById(R.id.sskdEditText);
        okButton = findViewById(R.id.okButton);
        databaseReference = FirebaseDatabase.getInstance().getReference("Keys");

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SetNewSecurityKey();
            }
        });
    }

    private void SetNewSecurityKey() {
        String key = secretKeyTextInputEditText.getText().toString();
        String lab = labTextInputEditText.getText().toString();
        String dept = deptTextInputEditText.getText().toString();
        if(TextUtils.isEmpty(key)){
            secretKeyTextInputEditText.setError("It would not be empty");
            return;
        }
        else if(TextUtils.isEmpty(dept)){
            deptTextInputEditText.setError("It would not be empty");
            return;
        }
        else if(TextUtils.isEmpty(lab)){
            labTextInputEditText.setError("It would not be empty");
            return;
        }
        else{
            HashMap hashMap = new HashMap();
            hashMap.put("Department",dept);
            hashMap.put("LabName",lab);
            databaseReference.child(key).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(getApplicationContext(),"Successfully Added",Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(),"Can not Add",Toast.LENGTH_SHORT).show();
                }
            });

        }
    }
}