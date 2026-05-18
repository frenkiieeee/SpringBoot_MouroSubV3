package com.mourosub.web.controller;

import com.mourosub.web.model.Actividad;
import com.mourosub.web.service.ServicioActividades;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/actividades")
public class ActividadesController {

    private final ServicioActividades servicioActividades;

    public ActividadesController(ServicioActividades servicioActividades) {
        this.servicioActividades = servicioActividades;
    }

    // GET /actividades -> lista todas las actividades
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("actividades", servicioActividades.listarTodos());
        return "actividades/lista";
    }

    // GET /actividades/nueva -> muestra formulario de nueva actividad
    @GetMapping("/nueva")
    public String nueva(Model model) {
        model.addAttribute("actividad", new Actividad());
        return "actividades/formulario";
    }

    // POST /actividades/guardar -> guarda actividad nueva o editada
    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Actividad actividad) {
        servicioActividades.guardar(actividad);
        return "redirect:/actividades";
    }

    // GET /actividades/editar/{id} -> carga actividad para editar
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("actividad", servicioActividades.buscarPorId(id));
        return "actividades/formulario";
    }

    // GET /actividades/eliminar/{id} -> baja logica de actividad
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        servicioActividades.eliminar(id);
        return "redirect:/actividades";
    }
}