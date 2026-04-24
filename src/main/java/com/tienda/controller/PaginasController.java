package com.tienda.controller;

import com.tienda.domain.Usuario;
import com.tienda.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import com.tienda.service.ProductoService;

@Controller
public class PaginasController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ProductoService productoService;

    @GetMapping("/registro")
    public String registro(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "registro";
    }

    @PostMapping("/registro")
    public String guardarUsuario(Usuario usuario, Model model) {

        Usuario existente = usuarioService.getUsuarioPorCorreo(usuario.getCorreo());

        if (existente != null) {
            model.addAttribute("error", "Ese correo ya está registrado");
            model.addAttribute("usuario", usuario);
            return "registro";
        }

        usuario.setRol("USER");
        usuario.setActivo(true);
        usuarioService.save(usuario);

        return "redirect:/login";
    }

    @GetMapping("/principal")
    public String principal() {
        return "principal";
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("productos", productoService.getProductosActivos());
        return "index";
    }

    @GetMapping("/index")
    public String index2(Model model) {
        model.addAttribute("productos", productoService.getProductosActivos());
        return "index";
    }
}
