package com.mourosub.web.controller;

import com.mourosub.web.model.Usuario;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

    // GET /cuenta/editar -> formulario para que el usuario edite sus propios datos.
    // El JS de auth/registro.html ya carga los datos desde Supabase/backend y manda
    // los cambios a /auth/sync, asi que aqui solo necesitamos pasar un Usuario vacio
    // para que el th:field no falle al renderizar.
    @GetMapping("/cuenta/editar")
    public String editarCuenta(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "auth/registro";
    }
}
