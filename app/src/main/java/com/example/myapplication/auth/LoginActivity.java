package com.example.myapplication.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.NoteDetailsActivity;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    TextInputEditText loginEmail,loginPassword;
    TextView newUserText;
    Button loginButton;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginEmail=findViewById(R.id.loginEmail);
        loginPassword=findViewById(R.id.loginPassword);
        loginButton=findViewById(R.id.loginButton);
        newUserText= findViewById(R.id.newUserText);
        mAuth=FirebaseAuth.getInstance();

        loginButton.setOnClickListener(view -> {

            login();
        });

        newUserText.setOnClickListener(view -> {
            startActivity(new Intent(LoginActivity.this,SignInActivity.class));
        });

    }
    private void login() {
        String email= Objects.requireNonNull(loginEmail.getText()).toString();
        String password= Objects.requireNonNull(loginPassword.getText()).toString();
        if(TextUtils.isEmpty(email)){
            loginEmail.setError("enter");
            loginEmail.requestFocus();
        }
        if(TextUtils.isEmpty(password)){
            loginPassword.setError("enter");
            loginPassword.requestFocus();
        }
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(LoginActivity.this, "User Created", Toast.LENGTH_SHORT).show();

                    SharedPreferences preferences =getSharedPreferences("login",MODE_PRIVATE);
                    SharedPreferences.Editor editor=preferences.edit();
                    editor.putBoolean("flag",true);
                    editor.apply();

                    startActivity(new Intent(LoginActivity.this, NoteDetailsActivity.class));
                    finish();
                }
                else{
                    Toast.makeText(LoginActivity.this,"error"+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}