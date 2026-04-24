package com.tienda.service;

import com.tienda.domain.Producto;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CarritoService {

    @Autowired
    private ProductoService productoService;

    @SuppressWarnings("unchecked")
    private Map<Long, Integer> obtenerCarritoSession(HttpSession session) {
        Map<Long, Integer> carrito = (Map<Long, Integer>) session.getAttribute("carrito");

        if (carrito == null) {
            carrito = new HashMap<>();
            session.setAttribute("carrito", carrito);
        }

        return carrito;
    }

    public void agregarProducto(Long productoId, int cantidad, HttpSession session) {
        Map<Long, Integer> carrito = obtenerCarritoSession(session);
        carrito.merge(productoId, cantidad, Integer::sum);
        session.setAttribute("carrito", carrito);
        session.setAttribute("cantidadCarrito", obtenerCantidadTotal(session));
    }

    public List<ItemCarrito> obtenerItems(HttpSession session) {
        Map<Long, Integer> carrito = obtenerCarritoSession(session);
        List<ItemCarrito> items = new ArrayList<>();

        for (Map.Entry<Long, Integer> entry : carrito.entrySet()) {
            Long productoId = entry.getKey();
            int cantidad = entry.getValue();

            Producto producto = productoService.getProductoPorId(productoId);

            if (producto != null) {
                ItemCarrito item = new ItemCarrito(
                        producto.getIdProducto(),
                        producto.getNombre(),
                        producto.getPrecio(),
                        cantidad,
                        producto.getRutaImagen(),
                        producto.getStock()
                );
                items.add(item);
            }
        }

        return items;
    }

    public double obtenerTotal(HttpSession session) {
        return obtenerItems(session).stream()
                .mapToDouble(ItemCarrito::getSubtotal)
                .sum();
    }

    public int obtenerCantidadTotal(HttpSession session) {
        return obtenerItems(session).stream()
                .mapToInt(ItemCarrito::getCantidad)
                .sum();
    }

    public boolean finalizarCompra(HttpSession session) {
        List<ItemCarrito> items = obtenerItems(session);

        for (ItemCarrito item : items) {
            Producto producto = productoService.getProductoPorId(item.getProductoId());

            if (producto == null || producto.getStock() < item.getCantidad()) {
                return false;
            }
        }

        for (ItemCarrito item : items) {
            Producto producto = productoService.getProductoPorId(item.getProductoId());

            int nuevoStock = producto.getStock() - item.getCantidad();
            producto.setStock(nuevoStock);

            productoService.save(producto);
        }

        limpiar(session);

        return true;
    }

    public void eliminarProducto(Long productoId, HttpSession session) {
        Map<Long, Integer> carrito = obtenerCarritoSession(session);
        carrito.remove(productoId);
        session.setAttribute("carrito", carrito);
        session.setAttribute("cantidadCarrito", obtenerCantidadTotal(session));
    }

    public void limpiar(HttpSession session) {
        session.removeAttribute("carrito");
        session.setAttribute("cantidadCarrito", 0);
    }
}
