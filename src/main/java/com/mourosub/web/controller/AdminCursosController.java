package com.mourosub.web.controller;

import com.mourosub.web.model.Curso;
import com.mourosub.web.model.Fichero;
import com.mourosub.web.service.CursoService;
import com.mourosub.web.service.FicheroService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/admin/cursos")
public class AdminCursosController {

    private final CursoService cursoService;
    private final FicheroService ficheroService;

    public AdminCursosController(CursoService cursoService, FicheroService ficheroService) {
        this.cursoService = cursoService;
        this.ficheroService = ficheroService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("cursos", cursoService.listarTodos());
        return "admin/cursos/lista";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("curso", new Curso());
        model.addAttribute("imagenes", ficheroService.listarPorBucket("imagenes"));
        return "admin/cursos/formulario";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Curso curso,
                          @RequestParam(value = "imagenExistenteId", required = false) Long imagenExistenteId,
                          @RequestParam(value = "imagenArchivo", required = false) MultipartFile imagenArchivo,
                          Model model) {
        try {
            aplicarImagen(curso, imagenExistenteId, imagenArchivo);
        } catch (Exception ex) {
            model.addAttribute("curso", curso);
            model.addAttribute("imagenes", ficheroService.listarPorBucket("imagenes"));
            model.addAttribute("error", ex.getMessage());
            return "admin/cursos/formulario";
        }
        cursoService.guardar(curso);
        return "redirect:/admin/cursos";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("curso", cursoService.buscarPorId(id));
        model.addAttribute("imagenes", ficheroService.listarPorBucket("imagenes"));
        return "admin/cursos/formulario";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        cursoService.eliminar(id);
        return "redirect:/admin/cursos";
    }

    private void aplicarImagen(Curso curso, Long imagenExistenteId, MultipartFile imagenArchivo) throws Exception {
        if (imagenArchivo != null && !imagenArchivo.isEmpty()) {
            Fichero nuevo = ficheroService.subirImagenAdmin(imagenArchivo);
            curso.setImagenUrl("/ficheros/ver/" + nuevo.getIdFichero());
            return;
        }
        if (imagenExistenteId != null) {
            Fichero existente = ficheroService.buscarPorId(imagenExistenteId);
            if (!"imagenes".equalsIgnoreCase(existente.getBucket())) {
                throw new IllegalArgumentException("La imagen seleccionada no pertenece al bucket de imagenes.");
            }
            curso.setImagenUrl("/ficheros/ver/" + existente.getIdFichero());
        }
    }
}
