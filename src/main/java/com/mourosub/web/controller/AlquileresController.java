package com.mourosub.web.controller;

import com.mourosub.web.model.Alquileres;
import com.mourosub.web.model.Material;
import com.mourosub.web.model.Usuario;
import com.mourosub.web.repository.MaterialRepository;
import com.mourosub.web.repository.UsuarioRepository;
import com.mourosub.web.service.AlquileresService;
import com.mourosub.web.service.MaterialService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequestMapping("/alquileres")
public class AlquileresController {
    private final AlquileresService alquileresService;
    private final MaterialService materialService;
    private final MaterialRepository materialRepository;
    private final UsuarioRepository usuarioRepository;

    public AlquileresController(
            AlquileresService alquileresService,
            MaterialService materialService,
            MaterialRepository materialRepository,
            UsuarioRepository usuarioRepository
    ) {
        this.alquileresService = alquileresService;
        this.materialService = materialService;
        this.materialRepository = materialRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping("/nuevo")
    public String mostrarFormulario(Model model) {
        model.addAttribute("alquiler", new Alquileres());
        model.addAttribute("materiales", materialService.listarTodos());
        return "alquileres/form";
    }

    @PostMapping
    public String crearAlquiler(
            @ModelAttribute Alquileres alquiler,
            @RequestParam Long materialId,
            @RequestParam(required = false) Long usuarioId,
            Model model
    ) {
        Material material = materialRepository.findById(materialId)
                .orElseThrow(() -> new RuntimeException("Material no encontrado"));
        alquiler.setMaterial(material);
        alquiler.setFechaAlquiler(LocalDate.now());

        if (usuarioId != null) {
            Usuario usuario = usuarioRepository.findById(usuarioId).orElse(null);
            alquiler.setUsuario(usuario);
        }

        alquileresService.crearAlquiler(alquiler, materialId, usuarioId);
        return "redirect:/alquileres";
    }
}
