package com.tienda.controller;

import com.tienda.domain.Producto;
import com.tienda.service.ProductoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @GetMapping("/producto/{idProducto}")
    public String verProducto(@PathVariable("idProducto") Long idProducto,
            Model model,
            HttpSession session) {

        Producto producto = productoService.getProductoPorId(idProducto);

        if (producto == null) {
            return "redirect:/index";
        }

        boolean esAdmin = session.getAttribute("rol") != null
                && session.getAttribute("rol").toString().equals("ADMIN");

        model.addAttribute("producto", producto);
        model.addAttribute("esAdmin", esAdmin);

        return "producto";
    }
}