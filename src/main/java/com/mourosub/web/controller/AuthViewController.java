package com.mourosub.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

// Controlador de vistas relacionadas con la cuenta del usuario
// Solo resuelve rutas "bonitas" y las conecta con su pagina correspondiente
@Controller
public class AuthViewController {

    // GET /login -> atajo: redirige a la pagina estatica login.html
    // Asi podemos enlazar a "/login" aunque el archivo real sea "login.html"
    @GetMapping("/login")
    public String login() {
        return "redirect:/login.html";
    }

    // GET /cuenta -> muestra la pagina "Mi cuenta"
    @GetMapping("/cuenta")
    public String cuenta() {
        // Devuelve la plantilla templates/cuenta.html
        return "cuenta";
    }
}
