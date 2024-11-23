package com.example.tiendaonline;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class CarritoAdapter extends ArrayAdapter<Producto> {
    private Context context;
    private List<Producto> productos;
    private OnCarritoChangeListener onCarritoChangeListener;

    public CarritoAdapter(Context context, List<Producto> productos, OnCarritoChangeListener onCarritoChangeListener) {
        super(context, 0, productos);
        this.context = context;
        this.productos = productos;
        this.onCarritoChangeListener = onCarritoChangeListener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.productov2, parent, false);
        }

        Producto producto = productos.get(position);

        ImageView productImage = convertView.findViewById(R.id.product_image);
        TextView productName = convertView.findViewById(R.id.product_name);
        TextView productPrice = convertView.findViewById(R.id.product_price);
        Button btnEliminar = convertView.findViewById(R.id.delete_button);

        // Verifica si el producto tiene una imagen y carga la imagen
        if (producto.getImagePath() != null && !producto.getImagePath().isEmpty()) {
            productImage.setImageURI(Uri.parse(producto.getImagePath())); // Si la ruta es válida, carga la imagen
        } else {
            productImage.setImageResource(R.drawable.ic_launcher_foreground); // Imagen predeterminada
        }

        // Establece el nombre y el precio del producto
        productName.setText(producto.getName());
        productPrice.setText("$" + producto.getPrice());

        // Manejo del botón de eliminar producto
        btnEliminar.setOnClickListener(v -> {
            productos.remove(position);  // Elimina el producto del carrito
            notifyDataSetChanged();  // Notificar al adaptador que se ha actualizado la lista

            // Actualizar el total en la actividad principal
            if (onCarritoChangeListener != null) {
                onCarritoChangeListener.onCarritoChanged();
            }
        });

        return convertView;
    }

    // Interfaz para actualizar el total en la actividad
    public interface OnCarritoChangeListener {
        void onCarritoChanged();
    }
}
