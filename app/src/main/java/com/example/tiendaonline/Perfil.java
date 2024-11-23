package com.example.tiendaonline;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.net.URL;

public class Perfil extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LoginGeneral lg = new LoginGeneral();
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_perfil);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Obtener la cuenta de Google actual
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {
            // Aquí puedes acceder a los datos de la cuenta
            String email = account.getEmail();
            String name = account.getDisplayName();
            Uri foto = account.getPhotoUrl();
            TextView nombreUsuario = findViewById(R.id.nombre_usuario);
            TextView EmailUsuario = findViewById(R.id.correo_usuario);
            ImageView fotoUsuario = findViewById(R.id.foto_perfil);
            nombreUsuario.setText(name);
            EmailUsuario.setText(email);
            fotoUsuario.setImageURI(foto);
            if (foto != null) {
                Glide.with(this).load(foto).into(fotoUsuario);
            }
        }


        ImageView volver = findViewById(R.id.btn_volver);
        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Button cerarsesion = findViewById(R.id.btn_cerrar_sesion);
        // cerarsesion.setOnClickListener(View -> GoogleSignInHelper.signOut(this));
        cerarsesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Perfil.this, LoginGeneral.class);
                startActivity(intent);
                GoogleSignInHelper.signOut(Perfil.this);
            }
        });

        Button metodospago = findViewById(R.id.btn_metodos_pago);
        metodospago.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Perfil.this, MetodosPago.class);
                startActivity(intent);

            }
        });

    }
}