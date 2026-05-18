package com.mourosub.web.controller;

import com.mourosub.web.model.Actividad;
import com.mourosub.web.repository.ActividadRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/actividades")
public class ActividadesController {

    private final ActividadRepository actividadRepository;

    public ActividadesController(ActividadRepository actividadRepository) {
        this.actividadRepository = actividadRepository;
    }

    // GET /actividades -> listar todas las actividades
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("actividades", actividadRepository.findAll());
        return "actividades/lista";
    }

    // GET /actividades/nueva -> mostrar formulario para crear actividad
    @GetMapping("/nueva")
    public String nueva(Model model) {
        model.addAttribute("actividad", new Actividad());
        return "actividades/formulario";
    }

    // POST /actividades/guardar -> guardar actividad nueva o editada
    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Actividad actividad) {
        actividadRepository.save(actividad);
        return "redirect:/actividades";
    }

    // GET /actividades/editar/{id} -> cargar actividad en formulario
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Actividad actividad = actividadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Actividad no encontrada con id: " + id));

        model.addAttribute("actividad", actividad);
        return "actividades/formulario";
    }

    // GET /actividades/eliminar/{id} -> baja logica de actividad
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        Actividad actividad = actividadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Actividad no encontrada con id: " + id));

        actividad.setActivo(false);
        actividadRepository.save(actividad);

        return "redirect:/actividades";
    }
}
