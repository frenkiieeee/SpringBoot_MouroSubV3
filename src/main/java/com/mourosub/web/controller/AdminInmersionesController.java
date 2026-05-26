package com.mourosub.web.controller;

import com.mourosub.web.model.Inmersiones;
import com.mourosub.web.model.Fichero;
import com.mourosub.web.service.FicheroService;
import com.mourosub.web.service.InmersionesService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/admin/inmersiones")
public class AdminInmersionesController {

    private final InmersionesService inmersionesService;
    private final FicheroService ficheroService;

    public AdminInmersionesController(InmersionesService inmersionesService, FicheroService ficheroService) {
        this.inmersionesService = inmersionesService;
        this.ficheroService = ficheroService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("inmersiones", inmersionesService.listarActivas());
        return "admin/inmersiones/lista";
    }

    @GetMapping("/nueva")
    public String nueva(Model model) {
        model.addAttribute("inmersion", new Inmersiones());
        model.addAttribute("imagenes", ficheroService.listarPorBucket("imagenes"));
        return "admin/inmersiones/formulario";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Inmersiones inmersion,
                          @RequestParam(value = "imagenExistenteId", required = false) Long imagenExistenteId,
                          @RequestParam(value = "imagenArchivo", required = false) MultipartFile imagenArchivo,
                          Model model) {
        try {
            aplicarImagen(inmersion, imagenExistenteId, imagenArchivo);
        } catch (Exception ex) {
            model.addAttribute("inmersion", inmersion);
            model.addAttribute("imagenes", ficheroService.listarPorBucket("imagenes"));
            model.addAttribute("error", ex.getMessage());
            return "admin/inmersiones/formulario";
        }
        inmersionesService.guardar(inmersion);
        return "redirect:/admin/inmersiones";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("inmersion", inmersionesService.buscarPorId(id));
        model.addAttribute("imagenes", ficheroService.listarPorBucket("imagenes"));
        return "admin/inmersiones/formulario";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        inmersionesService.eliminar(id);
        return "redirect:/admin/inmersiones";
    }

    private void aplicarImagen(Inmersiones inmersion, Long imagenExistenteId, MultipartFile imagenArchivo) throws Exception {
        if (imagenArchivo != null && !imagenArchivo.isEmpty()) {
            Fichero nuevo = ficheroService.subirImagenAdmin(imagenArchivo);
            inmersion.setImagenUrl("/ficheros/ver/" + nuevo.getIdFichero());
            return;
        }
        if (imagenExistenteId != null) {
            Fichero existente = ficheroService.buscarPorId(imagenExistenteId);
            if (!"imagenes".equalsIgnoreCase(existente.getBucket())) {
                throw new IllegalArgumentException("La imagen seleccionada no pertenece al bucket de imagenes.");
            }
            inmersion.setImagenUrl("/ficheros/ver/" + existente.getIdFichero());
        }
    }
}
