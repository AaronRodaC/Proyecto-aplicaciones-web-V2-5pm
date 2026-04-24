package com.tienda.service;

import com.tienda.domain.Producto;
import com.tienda.repository.ProductoRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Transactional(readOnly = true)
    public List<Producto> getProductosPorCategoriaActivos(String categoria) {
        return productoRepository.findByCategoriaAndActivoTrue(categoria);
    }

    @Transactional(readOnly = true)
    public List<Producto> getProductosPorCategoria(String categoria) {
        return productoRepository.findByCategoria(categoria);
    }

    @Transactional(readOnly = true)
    public Producto getProducto(Producto producto) {
        return productoRepository.findById(producto.getIdProducto()).orElse(null);
    }

    @Transactional(readOnly = true)
    public Producto getProductoPorId(Long idProducto) {
        return productoRepository.findById(idProducto).orElse(null);
    }

    @Transactional
    public void save(Producto producto) {
        productoRepository.save(producto);
    }

    @Transactional
    public void delete(Producto producto) {
        productoRepository.delete(producto);
    }

    @Transactional(readOnly = true)
    public List<Producto> buscarProductos(String texto) {
        return productoRepository.buscarProductos(texto);
    }

    @Transactional(readOnly = true)
    public List<Producto> getProductosActivos() {
        return productoRepository.findByActivoTrue();
    }
}
