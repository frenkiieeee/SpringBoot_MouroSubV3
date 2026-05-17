package com.mourosub.web.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

// @RestController es como @Controller pero todos sus metodos devuelven datos (JSON), no plantillas.

// Este controlador expone al frontend la configuracion publica de Supabase,
// asi no hace falta escribir la URL ni la key a mano en el JavaScript.
@RestController
public class ConfigController {

    // @Value lee una variable de entorno / propiedad y la guarda aqui.
    // SUPABASE_PUBLIC_URL = direccion publica de Supabase.
    @Value("${SUPABASE_PUBLIC_URL}")
    private String supabaseUrl;

    // ANON_KEY = clave publica ("anonima") de Supabase, valida para usar desde el navegador.
    @Value("${ANON_KEY}")
    private String anonKey;

    // GET /api/config -> devuelve un JSON con la URL y la key para que el login las use.
    @GetMapping("/api/config")
    public Map<String, String> config() {
        // Map.of crea el JSON: {"supabaseUrl": "...", "anonKey": "..."}.
        return Map.of(
            "supabaseUrl", supabaseUrl,
            "anonKey", anonKey
        );
    }
}
