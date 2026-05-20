package com.mourosub.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

// Controlador de vistas relacionadas con la cuenta del usuario
// Solo resuelve rutas "bonitas" y las conecta con su pagina correspondiente
@Controller
public class AuthViewController {

    // GET /login -> muestra la plantilla templates/auth/login.html.
    @GetMapping({"/login", "/login.html"})
    public String login() {
        return "auth/login";
    }

    // GET /cuenta -> muestra la pagina "Mi cuenta"
    @GetMapping("/cuenta")
    public String cuenta() {
        // Devuelve la plantilla templates/usuarios/cuenta.html
        return "usuarios/cuenta";
    }
}
