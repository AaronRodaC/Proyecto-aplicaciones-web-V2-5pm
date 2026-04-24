package com.tienda.repository;

import com.tienda.domain.Producto;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductoRepository extends JpaRepository<Producto, Long> {

    List<Producto> findByCategoriaAndActivoTrue(String categoria);

    List<Producto> findByCategoria(String categoria);

    List<Producto> findByActivoTrue();

    @Query("SELECT p FROM Producto p WHERE p.activo = true AND " +
           "(LOWER(p.nombre) LIKE LOWER(CONCAT('%', :texto, '%')) OR " +
           "LOWER(p.descripcion) LIKE LOWER(CONCAT('%', :texto, '%')) OR " +
           "LOWER(p.categoria) LIKE LOWER(CONCAT('%', :texto, '%')))")
    List<Producto> buscarProductos(String texto);
}