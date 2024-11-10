package com.example.tiendaonline;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ProductAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Producto> products;

    public ProductAdapter(Context context, ArrayList<Producto> products) {
        this.context = context;
        this.products = products;
    }

    @Override
    public int getCount() {
        return products.size();
    }

    @Override
    public Object getItem(int position) {
        return products.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Reutilizar la vista si ya existe
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.producto, parent, false);
        }

        Producto product = products.get(position);

        // Obtener las vistas de cada elemento
        ImageView productImage = convertView.findViewById(R.id.product_image);
        TextView productName = convertView.findViewById(R.id.product_name);
        TextView productPrice = convertView.findViewById(R.id.product_price);
        Button editButton = convertView.findViewById(R.id.edit_button);
        Button deleteButton = convertView.findViewById(R.id.delete_button);

        // Configurar el producto
        productImage.setImageResource(R.drawable.ic_launcher_foreground); // Cambia esto por una imagen si la tienes
        productName.setText(product.getName());
        productPrice.setText("$" + product.getPrice());

        // Manejo de eventos del botón de editar
        editButton.setOnClickListener(v -> {
            // Obtener el contexto de la actividad desde la vista
            if (context instanceof Home) {
                // Llamar al método de edición en la actividad
                ((Home) context).showEditProductDialog(product);
            }
        });

        // Manejo de eventos del botón de eliminar
        deleteButton.setOnClickListener(v -> {
            // Obtener el contexto de la actividad desde la vista
            if (context instanceof Home) {
                // Llamar al método de eliminación en la actividad
                ((Home) context).showDeleteProductDialog(product);
            }
        });

        return convertView;
    }
}


