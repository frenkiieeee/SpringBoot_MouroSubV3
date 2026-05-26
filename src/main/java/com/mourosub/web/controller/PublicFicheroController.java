package com.mourosub.web.controller;

import com.mourosub.web.model.Fichero;
import com.mourosub.web.service.FicheroService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Endpoints publicos para servir ficheros (especialmente imagenes del bucket "imagenes").
@RestController
@RequestMapping("/ficheros")
public class PublicFicheroController {

    private final FicheroService ficheroService;

    public PublicFicheroController(FicheroService ficheroService) {
        this.ficheroService = ficheroService;
    }

    @GetMapping("/ver/{id}")
    public ResponseEntity<byte[]> ver(@PathVariable Long id) {
        Fichero fichero = ficheroService.buscarPorId(id);
        MediaType tipo;
        try {
            tipo = MediaType.parseMediaType(fichero.getTipoContenido());
        } catch (Exception ex) {
            tipo = MediaType.APPLICATION_OCTET_STREAM;
        }
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fichero.getNombre() + "\"")
                .contentType(tipo)
                .body(fichero.getDatos());
    }
}
