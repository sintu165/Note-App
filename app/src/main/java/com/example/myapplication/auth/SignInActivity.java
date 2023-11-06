package com.example.myapplication.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

public class SignInActivity extends AppCompatActivity {

    TextInputEditText signInEmail,signInPassword;
    Button signInButton;
    FirebaseAuth mAuth;
    TextView alreadyHaveAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        signInEmail = findViewById(R.id.signInEmail);
        signInPassword = findViewById(R.id.signInPassword);
        signInButton = findViewById(R.id.signInButton);
        alreadyHaveAccount = findViewById(R.id.alreadyHaveAccountText);

        mAuth =FirebaseAuth.getInstance();
        signInButton.setOnClickListener(view -> {
            signIn();
        });
        alreadyHaveAccount.setOnClickListener( view -> {
                    startActivity(new Intent(SignInActivity.this, LoginActivity.class));
                }
        );
    }

    private void signIn() {
        String email = Objects.requireNonNull(signInEmail.getText()).toString();
        String password= Objects.requireNonNull(signInPassword.getText()).toString();

        if(TextUtils.isEmpty(email)){
            signInEmail.setError("enter");
            signInEmail.requestFocus();
            return;
        }
        if(TextUtils.isEmpty(password)){
            signInPassword.setError("enter");
            signInPassword.requestFocus();
            return;
        }
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(SignInActivity.this, "User Created", Toast.LENGTH_SHORT).show();
                   startActivity(new Intent(SignInActivity.this, NoteDetailsActivity.class));
                   finish();
                }
                else{
                    Toast.makeText(SignInActivity.this,"error"+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}