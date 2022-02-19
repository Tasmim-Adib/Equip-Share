package com.example.equipshare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private TextInputLayout emailTextInputLayout, passwordTextInputLayout;
    private TextInputEditText emailTextInputEditText, passwordTextInputEditText;
    private Button loginButton,signupButton;
    private TextView loginTextView, signupTextview, adminTextView;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser != null){
            sendUsertoDashBoard();
        }

        emailTextInputEditText = findViewById(R.id.emailEditText);
        passwordTextInputEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        signupButton = findViewById(R.id.signupButton);
        loginTextView = findViewById(R.id.loginTextView);
        signupTextview = findViewById(R.id.signupTextView);
        adminTextView = findViewById(R.id.adminTextView);
        loadingBar = new ProgressDialog(this);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loggedIn();
            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUp();
            }
        });

        loginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginTextView.setVisibility(View.GONE);
                loginButton.setVisibility(View.GONE);
                signupButton.setVisibility(View.VISIBLE);
                signupTextview.setVisibility(View.VISIBLE);
            }
        });

        signupTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginTextView.setVisibility(View.VISIBLE);
                loginButton.setVisibility(View.VISIBLE);
                signupButton.setVisibility(View.GONE);
                signupTextview.setVisibility(View.GONE);
            }
        });

        adminTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),AdminActivity.class);
                startActivity(intent);
            }
        });
    }



    private void loggedIn() {
        String email = emailTextInputEditText.getText().toString();
        String password = passwordTextInputEditText.getText().toString();

        if(TextUtils.isEmpty(email)){
            emailTextInputEditText.setError("please enter an valid email");
            emailTextInputEditText.requestFocus();
            return;
        }

        else if(TextUtils.isEmpty(password)){
            passwordTextInputEditText.setError("please give the valid passsword");
            passwordTextInputEditText.requestFocus();
        }

        else{
            loadingBar.setTitle("Login");
            loadingBar.setMessage("Please Wait...");

            loadingBar.setCanceledOnTouchOutside(true);
            loadingBar.show();
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                sendUsertoDashBoard();
                                Toast.makeText(MainActivity.this, "Successfully Login", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                            }
                            else {
                                String message = task.getException().toString();
                                Toast.makeText(MainActivity.this, "Error : "+message, Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                            }


                        }
                    });
        }


    }



    private void signUp() {
        String email = emailTextInputEditText.getText().toString();
        String password = passwordTextInputEditText.getText().toString();

        if(TextUtils.isEmpty(email)){
            emailTextInputEditText.setError("please enter an valid email");
            emailTextInputEditText.requestFocus();
            return;
        }

        else if(TextUtils.isEmpty(password)){
            passwordTextInputEditText.setError("please give the valid passsword");
            passwordTextInputEditText.requestFocus();
        }
        else{
            loadingBar.setTitle("Sign Up");
            loadingBar.setMessage("Please wait, while your account is creating...");
            loadingBar.setCanceledOnTouchOutside(true);
            loadingBar.show();
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                sendUsertoRegister();
                                Toast.makeText(MainActivity.this, "Account Created Successfully", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                            }
                            else {
                                String message = task.getException().toString();
                                Toast.makeText(MainActivity.this, "Error : "+message, Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                            }

                        }
                    });
        }
    }

    private void sendUsertoRegister() {
        Intent intent = new Intent(getApplicationContext(),RegisterActivity.class);
        startActivity(intent);
        finish();
    }
    private void sendUsertoDashBoard() {
        Intent intent = new Intent(getApplicationContext(),DashboardActivity.class);
        startActivity(intent);
        finish();
    }
}