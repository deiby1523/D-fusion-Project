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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    FirebaseAuth auth;
    EditText textEmail, textPassword;
    Button signInButton;
    Button signInGoogleButton;
    TextView signUpLink;
    TextView recoveryLink;

    @Override
    public void onStart() {
        super.onStart();
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        // Check if user is signed in (non-null)
        if(user != null){
            // User already signed in
            // redirecting to HomeActivity
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();
        textEmail = findViewById(R.id.textEmail);
        textPassword = findViewById(R.id.textPassword);
        signInButton = findViewById(R.id.signInButton);
        signInGoogleButton = findViewById(R.id.signInGoogleButton);
        signUpLink = findViewById(R.id.signUpLink);
        recoveryLink = findViewById(R.id.recoveryLink);


        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email, password;
                email = String.valueOf(textEmail.getText());
                password = String.valueOf(textPassword.getText());

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(LoginActivity.this, "Ingresa un Email", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(LoginActivity.this, "Ingresa una contraseña", Toast.LENGTH_SHORT).show();
                    return;
                }

                auth.signInWithEmailAndPassword(email,password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success
                                    // Create an intent to HomeActivity
                                    Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
                                    startActivity(intent);
                                    finish(); // Optional, to close LoginActivity and prevent return action
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(LoginActivity.this, "Autenticación fallida",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        });

        signInGoogleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crear un intent para ir a HomeActivity
                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                startActivity(intent);
                finish(); // Opcional, para cerrar la LoginActivity y evitar que vuelva al presionar "atrás"
            }
        });


        signUpLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });

        recoveryLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RecoverPasswordActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}