package com.uts.proyecto;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    // UI elements
    EditText textName, textEmail, textPassword, textConfirmPassword;
    Button signUpButton;
    TextView loginButton;

    // Firebase Authentication
    FirebaseAuth mAuth;
    FirebaseUser firebaseUser;
    DatabaseReference dbRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

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
                signUpButton.setEnabled(false);
                String email = String.valueOf(textEmail.getText());
                String password = String.valueOf(textPassword.getText());
                String confirmPassword = String.valueOf(textConfirmPassword.getText());

                // Validate email input
                if (TextUtils.isEmpty(email)) {
                    Snackbar.make(v, "Ingresa un correo electrónico", Snackbar.LENGTH_SHORT)
                            .setBackgroundTint(getResources().getColor(R.color.danger, getTheme()))
                            .setTextColor(getResources().getColor(R.color.black, getTheme()))
                            .show();
                    signUpButton.setEnabled(true);
                    return;
                }

                // Validate password input
                if (TextUtils.isEmpty(password)) {
                    Snackbar.make(v, "Ingresa una contraseña", Snackbar.LENGTH_SHORT)
                            .setBackgroundTint(getResources().getColor(R.color.danger, getTheme()))
                            .setTextColor(getResources().getColor(R.color.black, getTheme()))
                            .show();
                    signUpButton.setEnabled(true);
                    return;
                }

                if (!password.equals(confirmPassword)) {
                    Snackbar.make(v, "Contraseñas no coinciden", Snackbar.LENGTH_SHORT)
                            .setBackgroundTint(getResources().getColor(R.color.danger, getTheme()))
                            .setTextColor(getResources().getColor(R.color.black, getTheme()))
                            .show();
                    signUpButton.setEnabled(true);
                    return;
                }

                // Create user with email and password
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    registerUser();
                                    signUpButton.setEnabled(true);
                                    // Registration successful, redirect to HomeActivity
                                    startActivity(new Intent(SignUpActivity.this, HomeActivity.class));
                                    finish();
                                } else {
                                    signUpButton.setEnabled(true);
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
                loginButton.setEnabled(false);
                // Redirect to LoginActivity
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
                loginButton.setEnabled(true);
                finish();
            }
        });
    }

    private void registerUser() {
        mAuth.signInWithEmailAndPassword(textEmail.getText().toString(), textPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    firebaseUser = mAuth.getCurrentUser();
                    if (firebaseUser != null) {
                        String uid = firebaseUser.getUid();
                        String name = textName.getText().toString(); // Name from user

                        // Use the model class
                        User user = new User(name);

                        dbRef = FirebaseDatabase.getInstance().getReference("users");
                        dbRef.child(uid).setValue(user);
                    }
                }
            }
        });


    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is already signed in
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // User is already logged in, redirect to HomeActivity
            Intent intent = new Intent(SignUpActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
