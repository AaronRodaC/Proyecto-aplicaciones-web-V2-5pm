package com.tienda.service;

import lombok.Data;

@Data
public class ItemCarrito {

    private Long productoId;
    private String nombre;
    private double precio;
    private int cantidad;
    private String rutaImagen;
    private int stock;

    public ItemCarrito(Long productoId, String nombre, double precio, int cantidad, String rutaImagen, int stock) {
        this.productoId = productoId;
        this.nombre = nombre;
        this.precio = precio;
        this.cantidad = cantidad;
        this.rutaImagen = rutaImagen;
        this.stock = stock;
    }

    public double getSubtotal() {
        return precio * cantidad;
    }
}