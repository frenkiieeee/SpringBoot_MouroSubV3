package com.mourosub.web.controller;

import com.mourosub.web.model.Actividad;
import com.mourosub.web.service.ServicioActividades;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/actividades")
public class AdminActividadesController {

    private final ServicioActividades servicioActividades;

    public AdminActividadesController(ServicioActividades servicioActividades) {
        this.servicioActividades = servicioActividades;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("actividades", servicioActividades.listarTodos());
        return "admin/actividades/lista";
    }

    @GetMapping("/nueva")
    public String nueva(Model model) {
        model.addAttribute("actividad", new Actividad());
        return "admin/actividades/formulario";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Actividad actividad) {
        servicioActividades.guardar(actividad);
        return "redirect:/admin/actividades";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("actividad", servicioActividades.buscarPorId(id));
        return "admin/actividades/formulario";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        servicioActividades.eliminar(id);
        return "redirect:/admin/actividades";
    }
}