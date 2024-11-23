package com.example.tiendaonline;

public class MetodoPago {
    private String detalle;
    private String tipo;

    public MetodoPago(String detalle, String tipo) {
        this.detalle = detalle;
        this.tipo = tipo;
    }

    public String getDetalle() {
        return detalle;
    }

    public String getTipo() {
        return tipo;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}