package com.example.tiendaonline;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.Manifest;

public class Home extends AppCompatActivity {


    private ProductDatabaseHelper dbHelper;
    private ListView productList;
    private ProductAdapter adapter;
    private ArrayList<Producto> productListItems;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private Uri imageUri;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 100;
    private static final int STORAGE_PERMISSION_REQUEST_CODE = 101;

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

    /*
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
    */

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
                String imageUri = cursor.getString(cursor.getColumnIndex(ProductDatabaseHelper.COLUMN_IMAGE_PATH)); // Obtener la URI de la imagen

                // Agregar el producto a la lista con la URI de la imagen
                productListItems.add(new Producto(id, name, price, imageUri)); // Ahora se pasan los tres argumentos y la URI de la imagen
            } while (cursor.moveToNext());
        }

        cursor.close(); // Cerrar el cursor después de usarlo
        adapter.notifyDataSetChanged(); // Notificar al adaptador que la lista ha cambiado
    }


    private void captureImage() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile(); // Método para crear el archivo de imagen
            } catch (IOException ex) {
                // Error al crear el archivo
            }
            if (photoFile != null) {
                imageUri = FileProvider.getUriForFile(this, "com.example.tiendaonline.fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    // Crear un archivo para almacenar la imagen
    private File createImageFile() throws IOException {
        String imageFileName = "JPEG_" + System.currentTimeMillis() + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefijo */
                ".jpg",         /* sufijo */
                storageDir      /* directorio */
        );
        return image;
    }

    // Método para manejar la respuesta de la cámara
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permiso concedido, abre la cámara
                captureImage();
            } else {
                // Permiso denegado
                Toast.makeText(this, "Permiso de cámara denegado", Toast.LENGTH_SHORT).show();
            }
        }
    }


/*
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
            dbHelper.addProduct(name, price, String.valueOf(imageUri));
            loadProducts();
        });

        dialogBuilder.setNegativeButton("Cancelar", null);
        dialogBuilder.show();
    }
    */
/* esteeeeee un
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

        Button takePhotoButton = new Button(this);
        takePhotoButton.setText("Tomar Foto");
        // Verificar si se tiene permiso para la cámara
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // Si no se tiene el permiso, solicitarlo
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
        } else {
            // Si ya tienes el permiso, puedes abrir la cámara
            takePhotoButton.setOnClickListener(v -> captureImage());  // Abrir la cámara
        }
        layout.addView(takePhotoButton);

        dialogBuilder.setView(layout);

        dialogBuilder.setPositiveButton("Agregar", (dialog, which) -> {
            String name = nameInput.getText().toString();
            double price = Double.parseDouble(priceInput.getText().toString());

            // Asegúrate de que imageUri no sea null antes de agregar el producto
            if (imageUri != null) {
                dbHelper.addProduct(name, price, imageUri.toString());  // Guardar la URI como string
                loadProducts();
            }
        });

        dialogBuilder.setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());

        dialogBuilder.create().show();
    }
    */

    public void showAddProductDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Nuevo Producto");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        // Reinicia imageUri
        imageUri = null;

        final EditText nameInput = new EditText(this);
        nameInput.setHint("Nombre del producto");
        layout.addView(nameInput);

        final EditText priceInput = new EditText(this);
        priceInput.setHint("Precio");
        priceInput.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        layout.addView(priceInput);

        Button takePhotoButton = new Button(this);
        takePhotoButton.setText("Tomar Foto");
        takePhotoButton.setOnClickListener(v -> captureImage());
        layout.addView(takePhotoButton);

        dialogBuilder.setView(layout);

        dialogBuilder.setPositiveButton("Agregar", (dialog, which) -> {
            String name = nameInput.getText().toString();
            String priceString = priceInput.getText().toString();

            if (name.isEmpty() || priceString.isEmpty()) {
                Toast.makeText(this, "Por favor ingresa un nombre y un precio", Toast.LENGTH_SHORT).show();
                return;
            }

            double price = Double.parseDouble(priceString);

            // Verificar si hay imagen; si no, asignar null o un valor predeterminado
            String imagePath = imageUri != null ? imageUri.toString() : null;

            dbHelper.addProduct(name, price, imagePath);
            loadProducts();
        });

        dialogBuilder.setNegativeButton("Cancelar", null);
        dialogBuilder.show();
    }


    /*
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
            dbHelper.updateProduct(product.getId(), name, price, String.valueOf(imageUri)); // Actualiza el producto en la base de datos

            loadProducts(); // Recarga la lista de productos
        });

        dialogBuilder.setNegativeButton("Cancelar", null);
        dialogBuilder.show();
    }
    */
    /* eestssssss
    public void showEditProductDialog(final Producto product) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Editar Producto");

        ScrollView scrollView = new ScrollView(this); // Permite hacer scroll si el contenido es grande
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        // Campo para nombre del producto
        final EditText nameInput = new EditText(this);
        nameInput.setHint("Nombre del producto");
        nameInput.setText(product.getName());
        layout.addView(nameInput);

        // Campo para precio
        final EditText priceInput = new EditText(this);
        priceInput.setHint("Precio");
        priceInput.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        priceInput.setText(String.valueOf(product.getPrice()));
        layout.addView(priceInput);

        // Imagen del producto
        final ImageView productImage = new ImageView(this);

        // Ajustar el tamaño de la imagen
        LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, // Ancho completo del diálogo
                400 // Altura limitada a 400 píxeles
        );
        imageParams.setMargins(0, 16, 0, 16); // Márgenes alrededor de la imagen
        productImage.setLayoutParams(imageParams);

        if (product.getImagePath() != null) {
            productImage.setImageURI(Uri.parse(product.getImagePath())); // Cargar la imagen actual
        } else {
            productImage.setImageResource(R.drawable.ic_launcher_foreground); // Imagen predeterminada
        }
        layout.addView(productImage);

        // Botón para tomar una nueva foto
        Button takePhotoButton = new Button(this);
        takePhotoButton.setText("Cambiar Foto");
        takePhotoButton.setOnClickListener(v -> {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {
                    Toast.makeText(this, "Error al crear el archivo de la imagen", Toast.LENGTH_SHORT).show();
                }
                if (photoFile != null) {
                    imageUri = FileProvider.getUriForFile(this, "com.example.tiendaonline.fileprovider", photoFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        });
        layout.addView(takePhotoButton);

        // Agregar el layout al ScrollView
        scrollView.addView(layout);

        dialogBuilder.setView(scrollView);

        // Botón de guardar
        dialogBuilder.setPositiveButton("Guardar", (dialog, which) -> {
            String name = nameInput.getText().toString();
            String priceString = priceInput.getText().toString();

            if (name.isEmpty() || priceString.isEmpty()) {
                Toast.makeText(this, "Por favor ingresa un nombre y un precio", Toast.LENGTH_SHORT).show();
                return;
            }

            double price = Double.parseDouble(priceString);
            String imagePath = imageUri != null ? imageUri.toString() : product.getImagePath();

            dbHelper.updateProduct(product.getId(), name, price, imagePath);
            loadProducts();
        });

        // Botón de cancelar
        dialogBuilder.setNegativeButton("Cancelar", null);
        dialogBuilder.show();
    }
*/
    public void showEditProductDialog(final Producto product) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Editar Producto");

        ScrollView scrollView = new ScrollView(this);
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        // Reinicia imageUri
        imageUri = null;

        final EditText nameInput = new EditText(this);
        nameInput.setHint("Nombre del producto");
        nameInput.setText(product.getName());
        layout.addView(nameInput);

        final EditText priceInput = new EditText(this);
        priceInput.setHint("Precio");
        priceInput.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        priceInput.setText(String.valueOf(product.getPrice()));
        layout.addView(priceInput);

        final ImageView productImage = new ImageView(this);
        LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                400
        );
        productImage.setLayoutParams(imageParams);

        if (product.getImagePath() != null) {
            productImage.setImageURI(Uri.parse(product.getImagePath()));
        } else {
            productImage.setImageResource(R.drawable.ic_launcher_foreground);
        }
        layout.addView(productImage);

        Button takePhotoButton = new Button(this);
        takePhotoButton.setText("Cambiar Foto");
        takePhotoButton.setOnClickListener(v ->
                captureImage()

        );
        layout.addView(takePhotoButton);

        scrollView.addView(layout);
        dialogBuilder.setView(scrollView);

        dialogBuilder.setPositiveButton("Guardar", (dialog, which) -> {
            String name = nameInput.getText().toString();
            String priceString = priceInput.getText().toString();

            if (name.isEmpty() || priceString.isEmpty()) {
                Toast.makeText(this, "Por favor ingresa un nombre y un precio", Toast.LENGTH_SHORT).show();
                return;
            }

            double price = Double.parseDouble(priceString);

            // Si no se tomó una nueva foto, conservar la imagen anterior
            String imagePath = imageUri != null ? imageUri.toString() : product.getImagePath();

            dbHelper.updateProduct(product.getId(), name, price, imagePath);
            loadProducts();
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