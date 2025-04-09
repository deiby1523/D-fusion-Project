package com.uts.proyecto;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    // Firebase Authentication instance
    private FirebaseAuth auth;

    // UI Components
    private EditText textEmail, textPassword;
    private Button signInButton, signInGoogleButton;
    private TextView signUpLink, recoveryLink;

    @Override
    protected void onStart() {
        super.onStart();
        auth = FirebaseAuth.getInstance();

        // Check if user is already signed in
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            // Redirect to HomeActivity
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        // Initialize FirebaseAuth instance
        auth = FirebaseAuth.getInstance();

        // Initialize UI components
        textEmail = findViewById(R.id.textEmail);
        textPassword = findViewById(R.id.textPassword);
        signInButton = findViewById(R.id.signInButton);
        signInGoogleButton = findViewById(R.id.signInGoogleButton);
        signUpLink = findViewById(R.id.signUpLink);
        recoveryLink = findViewById(R.id.recoveryLink);

        // Sign in with email and password
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(v);

                String email = textEmail.getText().toString();
                String password = textPassword.getText().toString();

                // Validate input fields
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(LoginActivity.this, "Please enter an email", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(LoginActivity.this, "Please enter a password", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Attempt sign in
                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Redirect to HomeActivity on success
                                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                    finish();
                                } else {
                                    // Show error message
                                    Toast.makeText(LoginActivity.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        // Google Sign-In placeholder
        signInGoogleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirect to HomeActivity (replace with actual Google Sign-In logic if needed)
                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                finish();
            }
        });

        // Redirect to SignUpActivity
        signUpLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
                finish();
            }
        });

        // Redirect to RecoverPasswordActivity
        recoveryLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RecoverPasswordActivity.class));
                finish();
            }
        });
    }

    // Hides the keyboard when user interacts with the screen
    public void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
