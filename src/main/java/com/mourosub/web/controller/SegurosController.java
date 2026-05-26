package com.mourosub.web.controller;

import com.mourosub.web.model.Seguros;
import com.mourosub.web.repository.SegurosRepository;
import com.mourosub.web.service.SegurosService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/seguros")
public class SegurosController {

    private final SegurosService segurosService;

    public SegurosController(
            SegurosService segurosService,
            SegurosRepository segurosRepository
    ) {
        this.segurosService = segurosService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("seguros", segurosService.listarTodos());
        return "seguros/lista";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("seguro", new Seguros());
        return "seguros/formulario";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Seguros seguro) {
        segurosService.guardarSeguro(seguro);
        return "redirect:/seguros";
    }

    @GetMapping("/editar/{idSeguro}")
    public String editar(@PathVariable Long idSeguro, Model model) {
        model.addAttribute("seguro", segurosService.buscarPorId(idSeguro));
        return "seguros/formulario";
    }

    @GetMapping("/eliminar/{idSeguro}")
    public String eliminar(@PathVariable Long idSeguro) {
        segurosService.eliminarSeguro(idSeguro);
        return "redirect:/seguros";
    }
}