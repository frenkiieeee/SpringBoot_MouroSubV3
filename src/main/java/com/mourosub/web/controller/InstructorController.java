package com.mourosub.web.controller;

import java.util.*;
import com.mourosub.web.model.Instructor;
import com.mourosub.web.repository.InstructorRepository;
import com.mourosub.web.service.InstructorService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/instructores")
public class InstructorController {

    private final InstructorService instructorService;
    private final InstructorRepository instructorRepository;

    public InstructorController(
            InstructorService instructorService,
            InstructorRepository instructorRepository
    ) {
        this.instructorService = instructorService;
        this.instructorRepository = instructorRepository;
    }

    // cargar la lista de todos los instructores
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("instructores", instructorRepository.findAll());
        return "instructores/lista";
    }

    // cargar formulario vacio para añadir uno nuevo
    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("instructor", new Instructor());
        return "instructores/formulario";
    }

    // procesar el formulario y guardar
    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Instructor instructor) {
        instructorService.guardarInstructor(instructor);
        return "redirect:/instructores";
    }

    // cargar el formulario con los datos de un instructor existente
    @GetMapping("/editar/{idInstructor}")
    public String editar(@PathVariable Long idInstructor, Model model) {
        model.addAttribute("instructor", instructorService.buscarPorId(idInstructor));
        return "instructores/formulario";
    }

    // borrar el instructor por id
    @GetMapping("/eliminar/{idInstructor}")
    public String eliminar(@PathVariable Long idInstructor) {
        instructorService.eliminarInstructor(idInstructor);
        return "redirect:/instructores";
    }
}