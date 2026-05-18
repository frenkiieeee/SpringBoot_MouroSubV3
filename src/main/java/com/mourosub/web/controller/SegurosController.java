package com.mourosub.web.controller;

import com.mourosub.web.dto.SegurosFormDTO;
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

    // cargar la lista de todos los seguros
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("seguros", segurosService.listarTodos());
        return "fragments/seguros/lista";
    }

    // cargar el formulario vacio para crear un seguro nuevo
    @GetMapping("/nuevo")
    public String mostrarFormulario(Model model) {
        model.addAttribute("segurosForm", new SegurosFormDTO());
        return "fragments/seguros/formulario";
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