package com.example.tiendaonline;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Home extends AppCompatActivity {


    private ProductDatabaseHelper dbHelper;
    private ListView productList;
    private ProductAdapter adapter;
    private ArrayList<Producto> productListItems;


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


        dbHelper = new ProductDatabaseHelper(this);
        productList = findViewById(R.id.product_list);
        productListItems = new ArrayList<>();
        adapter = new ProductAdapter(this, productListItems);
        productList.setAdapter(adapter);

        loadProducts();

        Button addProductButton = findViewById(R.id.add_product);
        addProductButton.setOnClickListener(view -> showAddProductDialog());
    }

    private void loadProducts() {
        // Obtener todos los productos desde la base de datos
        Cursor cursor = dbHelper.getAllProducts();

        productListItems.clear(); // Limpiar la lista antes de agregar los nuevos productos

        // Comprobar si el cursor tiene registros
        if (cursor.moveToFirst()) {
            do {
                // Obtener los valores del cursor
                int id = cursor.getInt(cursor.getColumnIndex(ProductDatabaseHelper.COLUMN_ID)); // Obtener el id
                String name = cursor.getString(cursor.getColumnIndex(ProductDatabaseHelper.COLUMN_NAME));
                double price = cursor.getDouble(cursor.getColumnIndex(ProductDatabaseHelper.COLUMN_PRICE));

                // Agregar el producto a la lista
                productListItems.add(new Producto(id, name, price)); // Ahora se pasan los tres argumentos
            } while (cursor.moveToNext());
        }

        cursor.close(); // Cerrar el cursor después de usarlo
        adapter.notifyDataSetChanged(); // Notificar al adaptador que la lista ha cambiado
    }


    private void showAddProductDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Nuevo Producto");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText nameInput = new EditText(this);
        nameInput.setHint("Nombre del producto");
        layout.addView(nameInput);

        final EditText priceInput = new EditText(this);
        priceInput.setHint("Precio");
        priceInput.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        layout.addView(priceInput);

        dialogBuilder.setView(layout);

        dialogBuilder.setPositiveButton("Agregar", (dialog, which) -> {
            String name = nameInput.getText().toString();
            double price = Double.parseDouble(priceInput.getText().toString());
            dbHelper.addProduct(name, price);
            loadProducts();
        });

        dialogBuilder.setNegativeButton("Cancelar", null);
        dialogBuilder.show();
    }

    public void showEditProductDialog(final Producto product) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Editar Producto");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText nameInput = new EditText(this);
        nameInput.setHint("Nombre del producto");
        nameInput.setText(product.getName()); // Muestra el nombre actual del producto
        layout.addView(nameInput);

        final EditText priceInput = new EditText(this);
        priceInput.setHint("Precio");
        priceInput.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        priceInput.setText(String.valueOf(product.getPrice())); // Muestra el precio actual del producto
        layout.addView(priceInput);

        dialogBuilder.setView(layout);

        dialogBuilder.setPositiveButton("Guardar", (dialog, which) -> {
            String name = nameInput.getText().toString();
            String priceString = priceInput.getText().toString();

            if (name.isEmpty() || priceString.isEmpty()) {
                // Mostrar mensaje de error si el nombre o el precio están vacíos
                Toast.makeText(this, "Por favor ingresa un nombre y un precio", Toast.LENGTH_SHORT).show();
                return;
            }

            double price = Double.parseDouble(priceString);

            // Llamar al método updateProduct de la base de datos
            dbHelper.updateProduct(product.getId(), name, price); // Actualiza el producto en la base de datos

            loadProducts(); // Recarga la lista de productos
        });

        dialogBuilder.setNegativeButton("Cancelar", null);
        dialogBuilder.show();
    }


    public void showDeleteProductDialog(final Producto product) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Eliminar Producto")
                .setMessage("¿Estás seguro de que deseas eliminar este producto?");

        dialogBuilder.setPositiveButton("Eliminar", (dialog, which) -> {
            // Llamamos al método deleteProduct de la base de datos
            dbHelper.deleteProduct(product.getId()); // Elimina el producto por su id

            loadProducts(); // Recarga la lista de productos
        });

        dialogBuilder.setNegativeButton("Cancelar", null);
        dialogBuilder.show();
    }



}