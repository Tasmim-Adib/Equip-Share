package com.example.equipshare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

public class AdminActivity extends AppCompatActivity {
    private TextInputEditText adminEmail,adminPassword;
    private Button adminButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        adminEmail = findViewById(R.id.adminemailEditText);
        adminPassword = findViewById(R.id.adminpasswordEditText);
        adminButton = findViewById(R.id.adminloginButton);

        adminButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check();
            }
        });
    }

    private void check(){
        String email = adminEmail.getText().toString();
        String password = adminPassword.getText().toString();

        if(email.equals("tasmim.adib@gmail.com") && password.equals("12345678")){
            Intent intent = new Intent(getApplicationContext(),SetSecurityKey.class);
            startActivity(intent);
            finish();
        }
        else{
            Toast.makeText(getApplicationContext(),"Wrong email & password !!",Toast.LENGTH_SHORT).show();
        }
    }
}