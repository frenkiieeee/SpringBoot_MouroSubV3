package com.mourosub.web.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

// @ControllerAdvice lo que hace es crear una clase "global" que se aplica a TODOS los controladores.
// Aqui la usamos para que ciertos datos esten disponibles en todas las plantillas
// sin tener que repetirlos en cada controlador.
@ControllerAdvice
public class GlobalViewAttributes {

    // URL publica de Supabase, leida de la configuracion del entorno.
    @Value("${SUPABASE_PUBLIC_URL}")
    private String supabaseUrl;

    // Key publica ("anonima") de Supabase.
    @Value("${ANON_KEY}")
    private String supabaseAnonKey;

    // @ModelAttribute("supabaseUrl"): mete este valor en el modelo de TODAS las vistas
    // con el nombre "supabaseUrl", para poder usarlo desde cualquier plantilla.
    @ModelAttribute("supabaseUrl")
    public String supabaseUrl() {
        // Valor publico para inicializar Supabase en el frontend.
        return supabaseUrl;
    }

    // Igual que el anterior, pero con la key publica.
    @ModelAttribute("supabaseAnonKey")
    public String supabaseAnonKey() {
        // Key publica para el login desde el cliente.
        return supabaseAnonKey;
    }
}
