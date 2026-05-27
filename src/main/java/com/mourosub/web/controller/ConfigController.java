package com.mourosub.web.controller;

import jakarta.servlet.http.HttpServletRequest;
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

    // Puerto del gateway de Supabase (expuesto en el host).
    @Value("${SUPABASE_GATEWAY_PORT}")
    private int gatewayPort;

    // ANON_KEY = clave publica ("anonima") de Supabase, valida para usar desde el navegador.
    @Value("${ANON_KEY}")
    private String anonKey;

    // GET /api/config -> devuelve un JSON con la URL y la key para que el login las use.
    // Usa el Host de la peticion para que funcione desde cualquier IP/dominio.
    @GetMapping("/api/config")
    public Map<String, String> config(HttpServletRequest request) {
        String host = request.getHeader("Host");
        String scheme = request.getScheme();
        String baseHost;
        int port;

        if (host == null || host.isEmpty()) {
            baseHost = "localhost";
            port = gatewayPort;
        } else {
            String[] parts = host.split(":");
            baseHost = parts[0];
            port = parts.length > 1 ? Integer.parseInt(parts[1]) : (request.getScheme().equals("https") ? 443 : 80);
        }

        String publicUrl = scheme + "://" + baseHost + ":" + gatewayPort;
        return Map.of(
            "supabaseUrl", publicUrl,
            "anonKey", anonKey
        );
    }
}
