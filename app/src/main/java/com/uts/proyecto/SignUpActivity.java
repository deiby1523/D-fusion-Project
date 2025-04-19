package com.uts.proyecto;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity {

    // UI elements
    EditText textName, textEmail, textPassword, textConfirmPassword;
    Button signUpButton;
    TextView loginButton;

    // Firebase Authentication
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance();

        // Bind UI elements
        textName = findViewById(R.id.textName);
        textEmail = findViewById(R.id.textEmail);
        textPassword = findViewById(R.id.textPassword);
        textConfirmPassword = findViewById(R.id.textConfirmPassword);
        signUpButton = findViewById(R.id.signInButton);
        loginButton = findViewById(R.id.signUpLink);

        // Handle sign-up button click
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = String.valueOf(textEmail.getText());
                String password = String.valueOf(textPassword.getText());

                // Validate email input
                if (TextUtils.isEmpty(email)) {
                    Snackbar.make(v, "Please enter an email", Snackbar.LENGTH_SHORT)
                            .setBackgroundTint(getResources().getColor(R.color.danger, getTheme()))
                            .setTextColor(getResources().getColor(R.color.black, getTheme()))
                            .show();
                    return;
                }

                // Validate password input
                if (TextUtils.isEmpty(password)) {
                    Snackbar.make(v, "Please enter a password", Snackbar.LENGTH_SHORT)
                            .setBackgroundTint(getResources().getColor(R.color.danger, getTheme()))
                            .setTextColor(getResources().getColor(R.color.black, getTheme()))
                            .show();
                    return;
                }

                // Create user with email and password
                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Registration successful, redirect to HomeActivity
                                    startActivity(new Intent(SignUpActivity.this, HomeActivity.class));
                                    finish();
                                } else {
                                    // Registration failed, show error message
                                    Snackbar.make(v, "Authentication failed", Snackbar.LENGTH_SHORT)
                                            .setBackgroundTint(getResources().getColor(R.color.danger, getTheme()))
                                            .setTextColor(getResources().getColor(R.color.black, getTheme()))
                                            .show();
                                }
                            }
                        });
            }
        });

        // Handle login link click
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirect to LoginActivity
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        // Check if user is already signed in
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            // User is already logged in, redirect to HomeActivity
            Intent intent = new Intent(SignUpActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
