package com.mourosub.web.controller;

import com.mourosub.web.model.Material;
import com.mourosub.web.service.MaterialService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/materiales")
public class AdminMaterialesController {

    private final MaterialService materialService;

    public AdminMaterialesController(MaterialService materialService) {
        this.materialService = materialService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("materiales", materialService.listarTodos());
        return "admin/materiales/lista";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("material", new Material());
        return "admin/materiales/formulario";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Material material) {
        materialService.guardarMaterial(material);
        return "redirect:/admin/materiales";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("material", materialService.buscarPorId(id));
        return "admin/materiales/formulario";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        materialService.eliminarMaterial(id);
        return "redirect:/admin/materiales";
    }
}