package com.mourosub.web.controller;

import com.mourosub.web.service.FicheroService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

// Controlador del panel admin para revisar y validar los certificados subidos.
// Protegido por SupabaseJwtFilter al estar bajo /admin/**.
@Controller
@RequestMapping("/admin/certificados")
public class AdminFicheroController {

    private final FicheroService ficheroService;

    public AdminFicheroController(FicheroService ficheroService) {
        this.ficheroService = ficheroService;
    }

    // GET /admin/certificados -> lista todos los certificados subidos por los usuarios.
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("ficheros", ficheroService.listarTodos());
        return "admin/certificados/lista";
    }

    // POST /admin/certificados/{id}/estado -> el admin marca un certificado como VALIDO o NO_VALIDO.
    // usuarioId es opcional: si la accion vino desde el detalle de un usuario, volvemos
    // a ese detalle; si vino de la lista general, volvemos a la lista de certificados.
    @PostMapping("/{id}/estado")
    public String cambiarEstado(@PathVariable Long id,
                                @RequestParam("estado") String estado,
                                @RequestParam(value = "usuarioId", required = false) Long usuarioId,
                                RedirectAttributes ra) {
        try {
            ficheroService.cambiarEstado(id, estado);
            ra.addFlashAttribute("ok", "Estado del certificado actualizado.");
        } catch (RuntimeException ex) {
            ra.addFlashAttribute("error", ex.getMessage());
        }
        if (usuarioId != null) {
            return "redirect:/admin/usuarios/" + usuarioId;
        }
        return "redirect:/admin/certificados";
    }
}
