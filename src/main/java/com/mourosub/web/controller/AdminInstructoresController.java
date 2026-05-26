package com.mourosub.web.controller;

import com.mourosub.web.model.Instructor;
import com.mourosub.web.repository.InstructorRepository;
import com.mourosub.web.service.InstructorService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/instructores")
public class AdminInstructoresController {

    private final InstructorService instructorService;
    private final InstructorRepository instructorRepository;

    public AdminInstructoresController(InstructorService instructorService,
                                       InstructorRepository instructorRepository) {
        this.instructorService = instructorService;
        this.instructorRepository = instructorRepository;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("instructores", instructorRepository.findAll());
        return "admin/instructores/lista";
    }

    @GetMapping("/nueva")
    public String nueva(Model model) {
        model.addAttribute("instructor", new Instructor());
        return "admin/instructores/formulario";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Instructor instructor) {
        instructorService.guardarInstructor(instructor);
        return "redirect:/admin/instructores";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("instructor", instructorService.buscarPorId(id));
        return "admin/instructores/formulario";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        instructorService.eliminarInstructor(id);
        return "redirect:/admin/instructores";
    }
}