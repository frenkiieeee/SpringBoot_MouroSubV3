package com.mourosub.web.controller;

import com.mourosub.web.model.Curso;
import com.mourosub.web.service.CursoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/cursos")
public class AdminCursosController {

    private final CursoService cursoService;

    public AdminCursosController(CursoService cursoService) {
        this.cursoService = cursoService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("cursos", cursoService.listarTodos());
        return "admin/cursos/lista";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("curso", new Curso());
        return "template/cursosLista/lista";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Curso curso) {
        cursoService.guardar(curso);
        return "redirect:/admin/cursos";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("curso", cursoService.buscarPorId(id));
        return "admin/cursos/formulario";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        cursoService.eliminar(id);
        return "redirect:/admin/cursos";
    }
}