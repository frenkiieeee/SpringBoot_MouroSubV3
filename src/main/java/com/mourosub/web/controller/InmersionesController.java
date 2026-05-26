package com.mourosub.web.controller;

import com.mourosub.web.model.Inmersiones;
import com.mourosub.web.service.InmersionesService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/inmersiones")
public class InmersionesController {

    private final InmersionesService inmersionesService;

    public InmersionesController(InmersionesService inmersionesService) {
        this.inmersionesService = inmersionesService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("inmersiones", inmersionesService.listarActivas());
        return "inmersiones/lista";
    }

    @GetMapping("/nueva")
    public String nueva(Model model) {
        model.addAttribute("inmersion", new Inmersiones());
        return "inmersiones/formulario";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Inmersiones inmersion) {
        inmersionesService.guardar(inmersion);
        return "redirect:/inmersiones";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("inmersion", inmersionesService.buscarPorId(id));
        return "inmersiones/formulario";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        inmersionesService.eliminar(id);
        return "redirect:/inmersiones";
    }

    @GetMapping("/{id}/reservar")
    public String reservar(@PathVariable Long id) {
        return "redirect:/reservas/inmersiones?id=" + id;
    }
}
