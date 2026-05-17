package com.mourosub.web.controller;

import com.mourosub.web.dto.AlquileresFormDTO;
import com.mourosub.web.service.AlquileresService;
import com.mourosub.web.service.MaterialService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/alquileres")
public class AlquileresController {
    private final AlquileresService alquileresService;
    private final MaterialService materialService;


    public AlquileresController (
            AlquileresService alquileresService,
            MaterialService materialService
    ){
            this.alquileresService = alquileresService;
            this.materialService = materialService;
    }

    @GetMapping("/nuevo")
    public String mostrarFormulario(Model model){
        model.addAttribute("alquilerForm", new AlquileresFormDTO());
        model.addAttribute("materiales", materialService.listarTodos());

        return "alquileres/form";
    }

    @PostMapping
    public String crearAlquiler(@ModelAttribute ("alquilerForm") AlquileresFormDTO alquileresFormDTO){
        alquileresService.crearAlquiler(alquileresFormDTO);
        return "redirect:/alquileres";
    }

}
