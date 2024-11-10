package com.example.tiendaonline;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class ProductDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "products.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_PRODUCTS = "products";

    public static final String TABLE_NAME = "products";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_PRICE = "price";

    public ProductDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_PRODUCTS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_PRICE + " REAL)";
        db.execSQL(createTable);
    }


    public Cursor getAllProducts() {
        SQLiteDatabase db = this.getReadableDatabase();

        // Consulta SQL para obtener todos los productos
        String query = "SELECT * FROM " + TABLE_PRODUCTS;

        // Ejecutar la consulta y devolver el resultado como un Cursor
        return db.rawQuery(query, null);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // Método para añadir un producto
    public void addProduct(String name, double price) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_PRICE, price);

        // Inserta el producto y obtiene el id autoincrementado
        long id = db.insert(TABLE_NAME, null, values);
        db.close();

        // Si quieres asignar el id en la clase Product (aunque es autoincremental)
        // puedes crear un nuevo objeto Product con el id obtenido
        if (id != -1) {
            Producto product = new Producto((int) id, name, price);
            // Aquí puedes agregar el producto a tu lista o hacer algo más con él
        }
    }


    // Método para obtener todos los productos
    public ArrayList<Producto> getProducts() {
        ArrayList<Producto> products = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID)); // Recupera el id
                String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
                double price = cursor.getDouble(cursor.getColumnIndex(COLUMN_PRICE));

                // Crea un objeto Product con el id
                Producto product = new Producto(id, name, price);
                products.add(product);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return products;
    }

    public void updateProduct(int id, String name, double price) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_PRICE, price);

        db.update(TABLE_NAME, values, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    public void deleteProduct(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }


}
