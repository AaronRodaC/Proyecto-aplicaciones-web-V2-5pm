package com.tienda.controller;

import com.tienda.service.CarritoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CarritoController {

    @Autowired
    private CarritoService carritoService;

    @GetMapping("/carrito")
    public String verCarrito(Model model, HttpSession session) {
        model.addAttribute("items", carritoService.obtenerItems(session));
        model.addAttribute("total", carritoService.obtenerTotal(session));
        model.addAttribute("cantidadTotal", carritoService.obtenerCantidadTotal(session));
        return "carrito";
    }

    @GetMapping("/carrito/finalizar")
    public String finalizarCompra(HttpSession session) {

        boolean compraRealizada = carritoService.finalizarCompra(session);

        if (compraRealizada) {
            session.setAttribute("mensajeCompra", "Compra realizada con éxito");
        } else {
            session.setAttribute("mensajeCompra", "No hay suficiente stock para realizar la compra");
        }

        return "redirect:/carrito";
    }

    @PostMapping("/carrito/agregar")
    public String agregarProducto(
            @RequestParam("productoId") Long productoId,
            @RequestParam("cantidad") int cantidad,
            @RequestParam(value = "origen", required = false, defaultValue = "index") String origen,
            HttpSession session) {

        carritoService.agregarProducto(productoId, cantidad, session);

        if (origen.startsWith("detalle/")) {
            return "redirect:/producto/" + productoId;
        }

        return "redirect:/" + origen;
    }

    @GetMapping("/carrito/eliminar/{idProducto}")
    public String eliminarProducto(@PathVariable("idProducto") Long idProducto, HttpSession session) {
        carritoService.eliminarProducto(idProducto, session);
        return "redirect:/carrito";
    }

    @GetMapping("/carrito/limpiar")
    public String limpiarCarrito(HttpSession session) {
        carritoService.limpiar(session);
        return "redirect:/carrito";
    }
}
