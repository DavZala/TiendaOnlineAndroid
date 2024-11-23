package com.example.tiendaonline;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MetodosPago extends AppCompatActivity {

    private ArrayList<MetodoPago> metodosPago; // Lista de métodos de pago
    private ArrayAdapter<String> adapter;
    private ListView listaMetodosPago; // Se declara el ListView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_metodos_pago);

        // Inicializar el ListView
        listaMetodosPago = findViewById(R.id.lista_metodos_pago);
        Button btnAddMetodoPago = findViewById(R.id.btn_add_metodo_pago);

        // Inicializar lista de métodos de pago
        metodosPago = new ArrayList<>();

        // Cargar métodos de pago simulados
        cargarMetodosPagoSimulados();

        // Adaptador para mostrar métodos de pago en el ListView
        adapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                convertirLista(metodosPago)
        ) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = view.findViewById(android.R.id.text1);
                textView.setTextColor(Color.BLACK); // Cambiar color de texto a negro
                return view;
            }
        };
        listaMetodosPago.setAdapter(adapter);

        // Botón para añadir un nuevo método de pago
        btnAddMetodoPago.setOnClickListener(v -> abrirDialogoNuevoMetodoPago());

        // Eliminar método de pago al hacer clic en un elemento de la lista
        listaMetodosPago.setOnItemClickListener((parent, view, position, id) -> abrirDialogoEliminarMetodoPago(position));


        ImageView volver = findViewById(R.id.volver);
        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    // Cargar algunos métodos de pago simulados
    private void cargarMetodosPagoSimulados() {
        metodosPago.add(new MetodoPago("Visa - 1234", "Tarjeta de Crédito"));
        metodosPago.add(new MetodoPago("PayPal", "Cuenta de PayPal"));
        metodosPago.add(new MetodoPago("Mastercard - 5678", "Tarjeta de Crédito"));
    }

    // Convertir la lista de objetos MetodoPago a una lista de strings (detalles de los métodos)
    private ArrayList<String> convertirLista(ArrayList<MetodoPago> metodos) {
        ArrayList<String> listaNombres = new ArrayList<>();
        for (MetodoPago metodo : metodos) {
            listaNombres.add(metodo.getDetalle());
        }
        return listaNombres;
    }

    // Método para abrir el diálogo para agregar un nuevo método de pago
    private void abrirDialogoNuevoMetodoPago() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Añadir Método de Pago");

        final EditText input = new EditText(this);
        input.setHint("Ej: Visa - 1234");
        builder.setView(input);

        builder.setPositiveButton("Añadir", (dialog, which) -> {
            String nuevoMetodo = input.getText().toString();
            if (!nuevoMetodo.isEmpty()) {
                // Agregar el nuevo método a la lista
                metodosPago.add(new MetodoPago(nuevoMetodo, "Tarjeta de Crédito"));
                actualizarLista();
            }
        });
        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }

    // Método para eliminar un método de pago
    private void eliminarMetodoPago(int position) {
        metodosPago.remove(position);
        actualizarLista();
    }

    // Método para abrir un diálogo de confirmación para eliminar un método de pago
    private void abrirDialogoEliminarMetodoPago(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Eliminar Método de Pago");
        builder.setMessage("¿Estás seguro de que deseas eliminar este método de pago?");

        builder.setPositiveButton("Eliminar", (dialog, which) -> eliminarMetodoPago(position));

        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }

    // Método para actualizar la lista en el ListView
    private void actualizarLista() {
        // Notificar al adaptador que los datos han cambiado
        adapter.clear();
        adapter.addAll(convertirLista(metodosPago));
        adapter.notifyDataSetChanged();
    }
}





