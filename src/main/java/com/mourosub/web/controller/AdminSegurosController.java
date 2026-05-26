package com.mourosub.web.controller;

import com.mourosub.web.model.Seguros;
import com.mourosub.web.service.SegurosService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/seguros")
public class AdminSegurosController {

    private final SegurosService segurosService;

    public AdminSegurosController(SegurosService segurosService) {
        this.segurosService = segurosService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("seguros", segurosService.listarTodos());
        return "admin/seguros/lista";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("seguro", new Seguros());
        return "admin/seguros/formulario";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Seguros seguro) {
        segurosService.guardarSeguro(seguro);
        return "redirect:/admin/seguros";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("seguro", segurosService.buscarPorId(id));
        return "admin/seguros/formulario";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        segurosService.eliminarSeguro(id);
        return "redirect:/admin/seguros";
    }
}