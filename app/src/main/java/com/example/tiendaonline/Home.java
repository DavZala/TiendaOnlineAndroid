package com.example.tiendaonline;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageView carrito = findViewById(R.id.carrito);
        carrito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, Carrito.class);
                startActivity(intent);
            }
        });
        ImageView mapa = findViewById(R.id.ubicacion);
        mapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, Ubicacion.class);
                startActivity(intent);
            }
        });

        ImageView perfil = findViewById(R.id.perfil);
        perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, Perfil.class);
                startActivity(intent);
            }
        });

        // Obtener la cuenta de Google actual
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {
            // Aquí puedes acceder a los datos de la cuenta
            String email = account.getEmail();
            String name = account.getDisplayName();
            TextView nombreUsuario = findViewById(R.id.nombreUsuario);
            nombreUsuario.setText(name);
            // Otros datos de la cuenta
        }

    }
}