package com.uts.proyecto;

import android.content.Intent;
import android.os.Bundle;
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
import com.google.firebase.auth.FirebaseAuth;

public class RecoverPasswordActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    TextView btnCancel;
    Button btnRecover;
    EditText textEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_recover_password);

        mAuth = FirebaseAuth.getInstance();
        btnCancel = findViewById(R.id.cancelButton);
        btnRecover = findViewById(R.id.recoverButton);
        textEmail = findViewById(R.id.emailRecovery);

        btnRecover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnRecover.setEnabled(false);
                mAuth.sendPasswordResetEmail(textEmail.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Snackbar.make(v, "Correo enviado", Snackbar.LENGTH_SHORT)
                                            .setBackgroundTint(getResources().getColor(R.color.success, getTheme()))
                                            .setTextColor(getResources().getColor(R.color.black, getTheme()))
                                            .show();
                                    btnRecover.setEnabled(true);
                                } else {
                                    Snackbar.make(v, "Ocurrió un problema al enviar el correo", Snackbar.LENGTH_SHORT)
                                            .setBackgroundTint(getResources().getColor(R.color.danger, getTheme()))
                                            .setTextColor(getResources().getColor(R.color.black, getTheme()))
                                            .show();
                                    btnRecover.setEnabled(true);
                                }
                            }
                        });
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecoverPasswordActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }

}