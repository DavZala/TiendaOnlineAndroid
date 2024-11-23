package com.example.tiendaonline;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Detallesproducto extends AppCompatActivity {

    private ImageView productImage;
    private TextView productName, productPrice;
    private Button buyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detallesproducto);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageButton volver = findViewById(R.id.volver);
        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // Obtener los datos pasados a través del Intent
        Intent intent = getIntent();
        String productName = intent.getStringExtra("productName");
        String productPrice = intent.getStringExtra("productPrice");
        String productImage = intent.getStringExtra("productImage");

        // Asegúrate de que los datos no sean null y luego actualiza los elementos UI
        TextView nameTextView = findViewById(R.id.product_name);
        TextView priceTextView = findViewById(R.id.product_price);
        ImageView productImageView = findViewById(R.id.product_image);

        nameTextView.setText(productName);
        priceTextView.setText("$" + productPrice);

        // Verificar si la ruta de la imagen es válida
        if (productImage != null && !productImage.isEmpty()) {
            // Si la imagen es una ruta válida, cargarla usando URI
            Uri imageUri = Uri.parse(productImage);
            productImageView.setImageURI(imageUri);
        } else {
            // Si no se pasa una imagen válida, usar una imagen predeterminada
            productImageView.setImageResource(R.drawable.ic_launcher_foreground);
        }

        // Obtener el producto desde el intent
        Double productPrice2 = Double.parseDouble(productPrice);
        Producto producto = new Producto(productName, productPrice2, productImage);
        Button btnAñadirCarrito = findViewById(R.id.add_to_cart_button);
        // Evento al hacer clic en "Añadir al carrito"
        btnAñadirCarrito.setOnClickListener(v -> {
            Carro.agregarProducto(producto);
            Toast.makeText(this, "Producto añadido al carrito", Toast.LENGTH_SHORT).show();
            finish(); // Vuelve a la pantalla anterior
        });

    }
}