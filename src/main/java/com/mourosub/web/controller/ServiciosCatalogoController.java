package com.mourosub.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

// Controlador sencillo del catalogo de servicios.
// Solo muestra paginas informativas: no guarda ni modifica datos.
@Controller
// Todas las rutas de aqui empiezan por /servicios.
@RequestMapping("/servicios")
public class ServiciosCatalogoController {

    // GET /servicios/cursos -> pagina informativa de cursos.
    @GetMapping("/cursos")
    public String cursos() {
        // Devuelve la plantilla templates/servicios/cursos.html.
        return "servicios/cursos";
    }

    // GET /servicios/alquiler -> pagina informativa de alquiler.
    @GetMapping("/alquiler")
    public String alquiler() {
        // Devuelve la plantilla templates/servicios/alquiler.html.
        return "servicios/alquiler";
    }
}
