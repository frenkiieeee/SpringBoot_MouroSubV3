package com.mourosub.web.controller;

import com.mourosub.web.model.Fichero;
import com.mourosub.web.service.FicheroService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

// Controlador de cara al usuario: subir y consultar sus propios certificados.
@Controller
@RequestMapping("/certificados")
public class FicheroController {

    private final FicheroService ficheroService;

    public FicheroController(FicheroService ficheroService) {
        this.ficheroService = ficheroService;
    }

    // GET /certificados -> formulario de subida + lista de certificados del usuario.
    @GetMapping
    public String index(@RequestParam(value = "supabaseUserId", required = false) String supabaseUserId,
                         Model model) {
        // supabaseUserId llega como parametro; la plantilla lo rellena desde localStorage.
        if (supabaseUserId != null && !supabaseUserId.isBlank()) {
            try {
                model.addAttribute("ficheros", ficheroService.listarDeUsuario(supabaseUserId));
            } catch (RuntimeException ex) {
                model.addAttribute("error", ex.getMessage());
            }
        }
        model.addAttribute("supabaseUserId", supabaseUserId);
        return "certificados/index";
    }

    // POST /certificados/subir -> guarda el fichero que sube el usuario.
    @PostMapping("/subir")
    public String subir(@RequestParam("archivo") MultipartFile archivo,
                         @RequestParam(value = "supabaseUserId", required = false) String supabaseUserId,
                         RedirectAttributes ra) {
        try {
            ficheroService.subir(archivo, supabaseUserId);
            ra.addFlashAttribute("ok", "Certificado subido. Queda PENDIENTE de validacion por el admin.");
        } catch (Exception ex) {
            // Cubre tanto errores de validacion como de lectura del fichero (IOException).
            ra.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/certificados?supabaseUserId=" + (supabaseUserId == null ? "" : supabaseUserId);
    }

    // GET /certificados/descargar/{id} -> descarga el fichero guardado.
    @GetMapping("/descargar/{id}")
    public ResponseEntity<byte[]> descargar(@PathVariable Long id) {
        Fichero fichero = ficheroService.buscarPorId(id);
        // Content-Disposition "attachment" hace que el navegador lo descargue con su nombre.
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + fichero.getNombre() + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(fichero.getDatos());
    }

    // GET /certificados/ver/{id} -> abre el fichero en el navegador (PDF, imagen...).
    // A diferencia de /descargar, usa Content-Disposition "inline" para MOSTRARLO en
    // vez de descargarlo, y el tipo real del fichero para que el navegador lo pinte.
    // Lo usa el admin para revisar el certificado antes de validarlo.
    @GetMapping("/ver/{id}")
    public ResponseEntity<byte[]> ver(@PathVariable Long id) {
        Fichero fichero = ficheroService.buscarPorId(id);
        // Intentamos usar el tipo real del fichero; si no es valido, uno generico.
        MediaType tipo;
        try {
            tipo = MediaType.parseMediaType(fichero.getTipoContenido());
        } catch (Exception ex) {
            tipo = MediaType.APPLICATION_OCTET_STREAM;
        }
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=\"" + fichero.getNombre() + "\"")
                .contentType(tipo)
                .body(fichero.getDatos());
    }
}
