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

    EditText textName, textEmail, textPassword, textConfirmPassword;
    Button signUpButton;
    TextView loginButton;

    FirebaseAuth auth;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null)
        FirebaseUser currentUser = auth.getCurrentUser();
        if(currentUser != null){
            // User already signed in
            // redirecting to HomeActivity
            Intent intent = new Intent(SignUpActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);

        auth = FirebaseAuth.getInstance();

        textName = findViewById(R.id.textName);
        textEmail = findViewById(R.id.textEmail);
        textPassword = findViewById(R.id.textPassword);
        textConfirmPassword = findViewById(R.id.textConfirmPassword);
        signUpButton = findViewById(R.id.signInButton);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email, password;
                email = String.valueOf(textEmail.getText());
                password = String.valueOf(textPassword.getText());

                if (TextUtils.isEmpty(email)) {
                    Snackbar.make(v,"Ingresa un Email", Snackbar.LENGTH_SHORT)
                            .setBackgroundTint(getResources().getColor(R.color.danger, getTheme()))
                            .setTextColor(getResources().getColor(R.color.black, getTheme()))
                            .show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Snackbar.make(v,"Ingresa una Contraseña", Snackbar.LENGTH_SHORT)
                            .setBackgroundTint(getResources().getColor(R.color.danger, getTheme()))
                            .setTextColor(getResources().getColor(R.color.black, getTheme()))
                            .show();
                    return;
                }


                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            // FirebaseUser user = auth.getCurrentUser();
//                            Snackbar.make(v,"Usuario Registrado", Snackbar.LENGTH_SHORT)
//                                    .setBackgroundTint(getResources().getColor(R.color.success, getTheme()))
//                                    .setTextColor(getResources().getColor(R.color.black, getTheme()))
//                                    .show();
                            // Redirect to HomeActivity on success
                            startActivity(new Intent(SignUpActivity.this, HomeActivity.class));
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Snackbar.make(v,"Error de autenticacion", Snackbar.LENGTH_SHORT)
                                    .setBackgroundTint(getResources().getColor(R.color.danger, getTheme()))
                                    .setTextColor(getResources().getColor(R.color.black, getTheme()))
                                    .show();
                        }
                    }
                });

            }
        });


        // Button to Login Activity
        loginButton = findViewById(R.id.signUpLink);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }
}