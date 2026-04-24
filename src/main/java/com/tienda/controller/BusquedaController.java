package com.tienda.controller;

import com.tienda.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class BusquedaController {

    @Autowired
    private ProductoService productoService;

    @GetMapping("/buscar")
    public String buscar(@RequestParam("texto") String texto, Model model) {

        model.addAttribute("textoBuscado", texto);
        model.addAttribute("productos", productoService.buscarProductos(texto));

        return "busqueda";
    }
}