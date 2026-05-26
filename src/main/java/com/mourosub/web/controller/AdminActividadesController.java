package com.mourosub.web.controller;

import com.mourosub.web.model.Actividad;
import com.mourosub.web.model.Fichero;
import com.mourosub.web.service.FicheroService;
import com.mourosub.web.service.ServicioActividades;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/admin/actividades")
public class AdminActividadesController {

    private final ServicioActividades servicioActividades;
    private final FicheroService ficheroService;

    public AdminActividadesController(ServicioActividades servicioActividades, FicheroService ficheroService) {
        this.servicioActividades = servicioActividades;
        this.ficheroService = ficheroService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("actividades", servicioActividades.listarTodos());
        return "admin/actividades/lista";
    }

    @GetMapping("/nueva")
    public String nueva(Model model) {
        model.addAttribute("actividad", new Actividad());
        model.addAttribute("imagenes", ficheroService.listarPorBucket("imagenes"));
        return "admin/actividades/formulario";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Actividad actividad,
                          @RequestParam(value = "imagenExistenteId", required = false) Long imagenExistenteId,
                          @RequestParam(value = "imagenArchivo", required = false) MultipartFile imagenArchivo,
                          Model model) {
        try {
            aplicarImagen(actividad, imagenExistenteId, imagenArchivo);
        } catch (Exception ex) {
            model.addAttribute("actividad", actividad);
            model.addAttribute("imagenes", ficheroService.listarPorBucket("imagenes"));
            model.addAttribute("error", ex.getMessage());
            return "admin/actividades/formulario";
        }
        servicioActividades.guardar(actividad);
        return "redirect:/admin/actividades";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("actividad", servicioActividades.buscarPorId(id));
        model.addAttribute("imagenes", ficheroService.listarPorBucket("imagenes"));
        return "admin/actividades/formulario";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        servicioActividades.eliminar(id);
        return "redirect:/admin/actividades";
    }

    private void aplicarImagen(Actividad actividad, Long imagenExistenteId, MultipartFile imagenArchivo) throws Exception {
        if (imagenArchivo != null && !imagenArchivo.isEmpty()) {
            Fichero nuevo = ficheroService.subirImagenAdmin(imagenArchivo);
            actividad.setImagenUrl("/ficheros/ver/" + nuevo.getIdFichero());
            return;
        }
        if (imagenExistenteId != null) {
            Fichero existente = ficheroService.buscarPorId(imagenExistenteId);
            if (!"imagenes".equalsIgnoreCase(existente.getBucket())) {
                throw new IllegalArgumentException("La imagen seleccionada no pertenece al bucket de imagenes.");
            }
            actividad.setImagenUrl("/ficheros/ver/" + existente.getIdFichero());
        }
    }
}
