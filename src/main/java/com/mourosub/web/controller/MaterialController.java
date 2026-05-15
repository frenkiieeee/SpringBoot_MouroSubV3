package com.mourosub.web.controller;

import java.util.*;
import com.mourosub.web.model.Material;
import com.mourosub.web.repository.MaterialRepository;
import com.mourosub.web.service.MaterialService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/materiales")
public class MaterialController {

    private final MaterialService materialService;
    private final MaterialRepository materialRepository;

    public MaterialController(
            MaterialService materialService,
            MaterialRepository materialRepository
    ) {
        this.materialService = materialService;
        this.materialRepository = materialRepository;
    }

    // listar el material
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("materiales", materialRepository.findAll());
        return "materiales/lista";
    }

    // mostrar formulario de nuevo material
    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("material", new Material());
        return "materiales/formulario";
    }

    // recibir los datos y guardar
    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Material material) {
        materialService.guardarMaterial(material);
        return "redirect:/materiales";
    }

    // editar un material existente
    @GetMapping("/editar/{idMaterial}")
    public String editar(@PathVariable Long idMaterial, Model model) {
        model.addAttribute("material", materialService.buscarPorId(idMaterial));
        return "materiales/formulario";
    }

    // borrar el material de la base de datos
    @GetMapping("/eliminar/{idMaterial}")
    public String eliminar(@PathVariable Long idMaterial) {
        materialService.eliminarMaterial(idMaterial);
        return "redirect:/materiales";
    }
}