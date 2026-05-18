package com.mourosub.web.controller;

import java.util.List;
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
    private final SegurosRepository segurosRepository;

    public SegurosController(
            SegurosService segurosService,
            SegurosRepository segurosRepository
    ) {
        this.segurosService = segurosService;
        this.segurosRepository = segurosRepository;
    }

    // cargar la lista de todos los seguros
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("seguros", segurosRepository.findAll());
        return "seguros/lista";
    }

    // cargar el formulario vacio para crear un seguro nuevo
    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("seguro", new Seguros());
        return "seguros/formulario";
    }

    // procesar el formulario y guardarlo
    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Seguros seguro) {
        segurosService.guardarSeguro(seguro);
        return "redirect:/seguros";
    }

    // cargar el formulario con los datos de un seguro que ya existe
    @GetMapping("/editar/{idSeguro}")
    public String editar(@PathVariable Long idSeguro, Model model) {
        model.addAttribute("seguro", segurosService.buscarPorId(idSeguro));
        return "seguros/formulario";
    }

    // borrar el seguro buscando por su id
    @GetMapping("/eliminar/{idSeguro}")
    public String eliminar(@PathVariable Long idSeguro) {
        segurosService.eliminarSeguro(idSeguro);
        return "redirect:/seguros";
    }
}