package com.example.tiendaonline;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class Carrito extends AppCompatActivity implements CarritoAdapter.OnCarritoChangeListener {

    private ListView listaCarrito;
    private TextView totalCarrito;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_carrito);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageView volver = findViewById(R.id.volver);
        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ListView listaCarrito = findViewById(R.id.lista_carrito);
        TextView totalCarrito = findViewById(R.id.total_carrito);

        // Obtener los productos del carrito
        List<Producto> productosEnCarrito = Carro.obtenerProductos();

        // Crear el adaptador y asignarlo al ListView
        CarritoAdapter adapter = new CarritoAdapter(this, productosEnCarrito, this);
        listaCarrito.setAdapter(adapter);

        // Mostrar el total inicial del carrito
        actualizarTotal();
    }

    // Método para actualizar el total del carrito
    private void actualizarTotal() {
        TextView totalCarrito = findViewById(R.id.total_carrito);
        totalCarrito.setText("Total: $" + Carro.obtenerTotal());
    }

    // Método de la interfaz para actualizar el total del carrito
    @Override
    public void onCarritoChanged() {
        // Al cambiar el carrito, actualizamos el total
        actualizarTotal();
    }
}
