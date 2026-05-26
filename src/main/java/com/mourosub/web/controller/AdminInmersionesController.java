package com.mourosub.web.controller;

import com.mourosub.web.model.Inmersiones;
import com.mourosub.web.service.InmersionesService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/inmersiones")
public class AdminInmersionesController {

    private final InmersionesService inmersionesService;

    public AdminInmersionesController(InmersionesService inmersionesService) {
        this.inmersionesService = inmersionesService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("inmersiones", inmersionesService.listarActivas());
        return "admin/inmersiones/lista";
    }

    @GetMapping("/nueva")
    public String nueva(Model model) {
        model.addAttribute("inmersion", new Inmersiones());
        return "admin/inmersiones/formulario";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Inmersiones inmersion) {
        inmersionesService.guardar(inmersion);
        return "redirect:/admin/inmersiones";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("inmersion", inmersionesService.buscarPorId(id));
        return "admin/inmersiones/formulario";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        inmersionesService.eliminar(id);
        return "redirect:/admin/inmersiones";
    }
}