package com.uts.proyecto;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 123;
    // Firebase Authentication instance
    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;
    private DatabaseReference dbRef;

    private GoogleSignInClient mGoogleSignInClient;
    // UI Components
    private EditText textEmail, textPassword;
    private Button signInButton, signInGoogleButton;
    private TextView signUpLink, recoveryLink;

    @Override
    protected void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();

        // Check if user is already signed in
        FirebaseUser user = mAuth.getCurrentUser();
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
        mAuth = FirebaseAuth.getInstance();

        createRequest();

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
                    Snackbar.make(v, "Ingresa un Email", Snackbar.LENGTH_SHORT)
                            .setBackgroundTint(getResources().getColor(R.color.danger, getTheme()))
                            .setTextColor(getResources().getColor(R.color.black, getTheme()))
                            .show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Snackbar.make(v, "Ingresa una Contraseña", Snackbar.LENGTH_SHORT)
                            .setBackgroundTint(getResources().getColor(R.color.danger, getTheme()))
                            .setTextColor(getResources().getColor(R.color.black, getTheme()))
                            .show();
                    return;
                }

                // Attempt sign in
                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Redirect to HomeActivity on success
                            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                            finish();
                        } else {
                            // Show error message
                            Snackbar.make(v, "Error en autenticacion -> signInWithEmailAndPassword()", Snackbar.LENGTH_SHORT)
                                    .setBackgroundTint(getResources().getColor(R.color.danger, getTheme()))
                                    .setTextColor(getResources().getColor(R.color.black, getTheme()))
                                    .show();
                        }
                    }
                });


            }
        });

        // Google Sign-In button event
        signInGoogleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
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

    private void createRequest() {
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)) // Reemplaza con tu ID de cliente
                .requestEmail().build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            // Signed in successfully, show authenticated UI.
            firebaseAuthWithGoogle(account.getIdToken());
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Snackbar.make(findViewById(R.id.signInGoogleButton), "Error en -> handleSignInResult()", Snackbar.LENGTH_SHORT)
                    .setBackgroundTint(getResources().getColor(R.color.danger, getTheme()))
                    .setTextColor(getResources().getColor(R.color.black, getTheme()))
                    .show();

        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                // Sign in success, upload user data into Firebase Realtime Database
                firebaseUser = mAuth.getCurrentUser();

                if (firebaseUser != null) {
                    String uid = firebaseUser.getUid();
                    String name = firebaseUser.getDisplayName(); // Name from Google

                    // Use the model class instead of HashMap
                    User user = new User(name);

                    dbRef = FirebaseDatabase.getInstance().getReference("users");
                    dbRef.child(uid).setValue(user);
                }

                // Redirect to HomeActivity
                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                finish();
            } else {
                // If sign in fails, display an error message to the user.
                Snackbar.make(findViewById(R.id.signInGoogleButton), "Error en -> firebaseAuthWithGoogle()", Snackbar.LENGTH_SHORT)
                        .setBackgroundTint(getResources().getColor(R.color.danger, getTheme()))
                        .setTextColor(getResources().getColor(R.color.black, getTheme()))
                        .show();
            }
        });
    }
}
