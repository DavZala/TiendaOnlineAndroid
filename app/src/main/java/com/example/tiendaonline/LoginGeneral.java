package com.example.tiendaonline;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;


public class LoginGeneral extends AppCompatActivity {
    private static final int RC_SIGN_IN = 9001;
    private GoogleSignInClient mGoogleSignInClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_general);

        // Configura Google Sign-In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("657637162637-06emeslltc7hlreg5v68tn16nb07sg7r.apps.googleusercontent.com")  // Reemplaza con tu Client ID
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Establece el botón de Google Sign-In
        SignInButton signInButton = findViewById(R.id.btn_iniciarsesiongoogle);
        signInButton.setOnClickListener(view -> signIn());
    }

    // Inicia el proceso de autenticación
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    // Maneja el resultado del inicio de sesión de Google
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // El inicio de sesión fue exitoso, obtenemos la cuenta del usuario
                GoogleSignInAccount account = task.getResult(ApiException.class);
                String displayName = account.getDisplayName();
                String email = account.getEmail();
                Toast.makeText(this, "Bienvenido, " + displayName + " " + email, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(LoginGeneral.this, Home.class);
                startActivity(intent);
            } catch (ApiException e) {
                // Si ocurre un error en el inicio de sesión
                Toast.makeText(this, "Error de inicio de sesión: " + e.getStatusCode(), Toast.LENGTH_LONG).show();
            }
        }
    }

    // Cierra la sesión del usuario
    private void signOut() {
        mGoogleSignInClient.signOut().addOnCompleteListener(this, task -> {
            Toast.makeText(LoginGeneral.this, "Has cerrado sesión", Toast.LENGTH_SHORT).show();
        });
    }
}

