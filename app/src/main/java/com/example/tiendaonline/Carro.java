package com.example.tiendaonline;

import java.util.ArrayList;
import java.util.List;

public class Carro {

    private static List<Producto> productos = new ArrayList<>();
    private static double total = 0.0;

    public static void agregarProducto(Producto producto) {
        if (producto != null) {
            productos.add(producto);
            total += producto.getPrice(); // Suma el precio al total
        }
    }

    public static List<Producto> obtenerProductos() {
        return productos;
    }

    // Obtener el total del carrito
    public static double obtenerTotal() {
        double total = 0;
        for (Producto producto : productos) {
            total += producto.getPrice();
        }
        return total;
    }

    public static void limpiarCarrito() {
        productos.clear();
        total = 0.0;
    }
}
