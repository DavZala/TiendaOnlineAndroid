package com.example.tiendaonline;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
public class ProductDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "products.db";
    private static final int DATABASE_VERSION = 2;  // Aumentamos la versión de la base de datos

    public static final String TABLE_PRODUCTS = "products";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_IMAGE_PATH = "image_path"; // Nueva columna para la ruta de la imagen

    public ProductDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_PRODUCTS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_PRICE + " REAL, " +
                COLUMN_IMAGE_PATH + " TEXT);";  // Agregamos la columna image_path
        db.execSQL(createTable);
    }

    // Método para obtener todos los productos (incluye la ruta de la imagen)
    public Cursor getAllProducts() {
        SQLiteDatabase db = this.getReadableDatabase();

        // Consulta SQL para obtener todos los productos
        String query = "SELECT * FROM " + TABLE_PRODUCTS;

        // Ejecutar la consulta y devolver el resultado como un Cursor
        return db.rawQuery(query, null);
    }

    // Método para agregar un producto a la base de datos
    public void addProduct(String name, double price, String imagePath) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_PRICE, price);
        values.put(COLUMN_IMAGE_PATH, imagePath);  // Guardamos la ruta de la imagen

        db.insert(TABLE_PRODUCTS, null, values);
        db.close();
    }

    // Método para actualizar un producto
    public void updateProduct(int id, String name, double price, String imagePath) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_PRICE, price);
        values.put(COLUMN_IMAGE_PATH, imagePath);  // Actualizamos la ruta de la imagen

        db.update(TABLE_PRODUCTS, values, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    // Método para eliminar un producto
    public void deleteProduct(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PRODUCTS, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    // Este método se usa para actualizar la base de datos si cambiamos la estructura
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE " + TABLE_PRODUCTS + " ADD COLUMN " + COLUMN_IMAGE_PATH + " TEXT;");
        }
    }
}
