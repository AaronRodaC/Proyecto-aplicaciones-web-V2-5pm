package com.tienda.controller;

import com.tienda.domain.Categoria;
import com.tienda.domain.Producto;
import com.tienda.service.CategoriaService;
import com.tienda.service.ProductoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private ProductoService productoService;


    @GetMapping("/juguetes")
    public String juguetes(Model model, HttpSession session) {
        cargarPaginaCategoria(model, session, "JUGUETES", "Juguetes",
                "Encuentra juguetes divertidos y seguros para consentir a tus mascotas");
        return "juguetes";
    }

    @GetMapping("/snacks")
    public String snacks(Model model, HttpSession session) {
        cargarPaginaCategoria(model, session, "SNACKS", "Snacks",
                "Premios y bocaditos deliciosos para consentir a tus mascotas");
        return "snacks";
    }

    @GetMapping("/ropa")
    public String ropa(Model model, HttpSession session) {
        cargarPaginaCategoria(model, session, "ROPA", "Ropa",
                "Ropa y accesorios cómodos para tu mascota");
        return "ropa";
    }

    @GetMapping("/alimentos")
    public String alimentos(Model model, HttpSession session) {
        cargarPaginaCategoria(model, session, "ALIMENTOS", "Alimentos",
                "Alimentos de calidad para el bienestar de tus mascotas");
        return "alimentos";
    }

    @GetMapping("/contactanos")
    public String contactanos() {
        return "contactanos";
    }


    @PostMapping({"/guardar", "/categoria/guardar"})
    public String guardar(Categoria categoria,
            @RequestParam(value = "imageFile", required = false) MultipartFile imagenFile) {

        categoriaService.save(categoria);
        return "redirect:/principal";
    }


    @PostMapping("/producto/guardar")
    public String guardarProducto(Producto producto, HttpSession session) {

        if (!esAdmin(session)) {
            return "redirect:/index";
        }

        producto.setActivo(true);
        productoService.save(producto);

        return redireccionarPorCategoria(producto.getCategoria());
    }

    @PostMapping("/producto/actualizar")
    public String actualizarProducto(Producto producto, HttpSession session) {

        if (!esAdmin(session)) {
            return "redirect:/index";
        }

        producto.setActivo(true);
        productoService.save(producto);

        return redireccionarPorCategoria(producto.getCategoria());
    }

    @GetMapping("/producto/eliminar/{idProducto}")
    public String eliminarProducto(@PathVariable("idProducto") Long idProducto, HttpSession session) {

        if (!esAdmin(session)) {
            return "redirect:/index";
        }

        Producto producto = new Producto();
        producto.setIdProducto(idProducto);
        productoService.delete(producto);

        return "redirect:/index";
    }


    private void cargarPaginaCategoria(Model model, HttpSession session,
            String categoria, String titulo, String subtitulo) {

        boolean esAdmin = esAdmin(session);

        model.addAttribute("tituloCategoria", titulo);
        model.addAttribute("subtituloCategoria", subtitulo);
        model.addAttribute("productos",
                productoService.getProductosPorCategoriaActivos(categoria));
        model.addAttribute("productosAdmin",
                productoService.getProductosPorCategoria(categoria));
        model.addAttribute("producto", new Producto());
        model.addAttribute("esAdmin", esAdmin);
        model.addAttribute("categoriaActual", categoria);
    }

    private boolean esAdmin(HttpSession session) {
        Object rol = session.getAttribute("rol");
        return rol != null && rol.toString().equals("ADMIN");
    }

    private String redireccionarPorCategoria(String categoria) {
        if (categoria.equalsIgnoreCase("JUGUETES")) {
            return "redirect:/juguetes";
        }
        if (categoria.equalsIgnoreCase("SNACKS")) {
            return "redirect:/snacks";
        }
        if (categoria.equalsIgnoreCase("ROPA")) {
            return "redirect:/ropa";
        }
        return "redirect:/alimentos";
    }
}