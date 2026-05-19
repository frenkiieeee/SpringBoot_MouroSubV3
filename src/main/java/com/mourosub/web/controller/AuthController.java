package com.mourosub.web.controller;

import com.mourosub.web.model.Usuario;
import com.mourosub.web.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

// @RestController: como @Controller, pero todos sus metodos devuelven DATOS (JSON),
// no plantillas. Es un controlador de "API": el frontend lo llama con fetch().
// @RequestMapping("/auth"): todas las rutas de esta clase empiezan por /auth.
@RestController
@RequestMapping("/auth")
public class AuthController {

    // Repositorio para leer/escribir en la tabla 'usuarios'.
    // @Autowired: Spring inyecta aqui la instancia el solo (inyeccion por campo).
    @Autowired
    private UsuarioRepository usuarioRepository;

    // ───────────────────────────────────────────────────────────────────────
    // POST /auth/login  ->  endpoint "puente".
    // Supabase ya ha autenticado al usuario; esto solo BUSCA su fila de dominio
    // y la devuelve. No crea nada: si no existe, responde 404.
    // ───────────────────────────────────────────────────────────────────────
    @PostMapping("/login")
    // @RequestBody: los datos llegan en el cuerpo de la peticion como JSON y
    // Spring los convierte en un objeto LoginBridgeRequest (la clase de abajo).
    public ResponseEntity<?> login(@RequestBody LoginBridgeRequest request) {
        // Optional<Usuario>: "puede haber un usuario o no"; obliga a comprobar el caso vacio.
        Optional<Usuario> usuario;

        // Si vino el id de Supabase, buscamos por ese id (es el identificador fiable).
        if (request.getSupabaseUserId() != null && !request.getSupabaseUserId().isBlank()) {
            try {
                // El id viaja como texto; lo convertimos al tipo UUID.
                UUID id = UUID.fromString(request.getSupabaseUserId());
                usuario = usuarioRepository.findBySupabaseUserId(id);
            } catch (IllegalArgumentException ex) {
                // Si el texto no es un UUID valido, respondemos 400 (peticion incorrecta).
                return ResponseEntity.badRequest().body(Map.of("error", "supabaseUserId invalido"));
            }
        } else {
            // Si no vino el id, buscamos por email como alternativa.
            usuario = usuarioRepository.findByEmail(request.getEmail());
        }

        // Si el Optional esta vacio, el usuario no existe en nuestra BD -> 404.
        if (usuario.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of("error", "usuario no encontrado en dominio"));
        }

        // Sacamos el usuario del Optional y devolvemos sus datos como JSON.
        Usuario u = usuario.get();
        return ResponseEntity.ok(Map.of(
                "idUsuario", u.getidUsuario(),
                "supabaseUserId", u.getSupabaseUserId().toString(),
                "email", u.getEmail(),
                "nombre", u.getNombre(),
                "apellidos", u.getApellidos()
        ));
    }

    // ───────────────────────────────────────────────────────────────────────
    // POST /auth/sync  ->  asegura que el usuario exista en nuestra tabla 'usuarios'.
    //
    // El problema que resuelve: cuando alguien se registra, Supabase Auth lo guarda
    // en SU tabla (auth.users), pero nuestra app necesita ademas una fila propia en
    // mourosub.usuarios (para enlazar reservas, certificados, etc.). Nada las conecta
    // automaticamente, asi que este endpoint lo hace.
    //
    // Patron "find-or-create" (buscar o crear): si la fila ya existe no toca nada;
    // si no existe, la crea. Lo llama login.html justo despues de registrarse o
    // iniciar sesion, asi que el usuario siempre acaba teniendo su fila de dominio.
    // ───────────────────────────────────────────────────────────────────────
    @PostMapping("/sync")
    // @RequestParam: los datos llegan como parametros en la URL (?supabaseUserId=...&email=...).
    // 'email' es required=false: si no viene, no pasa nada (quedara vacio).
    public ResponseEntity<?> sync(@RequestParam("supabaseUserId") String supabaseUserId,
                                  @RequestParam(value = "email", required = false) String email) {
        // 1) El id de Supabase llega como texto; lo convertimos al tipo UUID.
        UUID id;
        try {
            id = UUID.fromString(supabaseUserId);
        } catch (IllegalArgumentException ex) {
            // Si no es un UUID valido, cortamos aqui con un 400.
            return ResponseEntity.badRequest().body(Map.of("error", "supabaseUserId invalido"));
        }

        // 2) Buscar o crear (find-or-create):
        //    findBySupabaseUserId(id) devuelve un Optional<Usuario>: puede traer al
        //    usuario o venir vacio. Comprobamos cual es el caso con un if normal.
        Optional<Usuario> existente = usuarioRepository.findBySupabaseUserId(id);
        Usuario usuario;
        if (existente.isPresent()) {
            // El usuario YA existe en nuestra tabla: lo usamos tal cual, no creamos nada.
            usuario = existente.get();
        } else {
            // El usuario NO existe todavia: creamos su fila nueva.
            usuario = new Usuario();
            // Guardamos el id de Supabase: es el enlace entre auth.users y nuestra fila.
            usuario.setSupabaseUserId(id);
            // Guardamos el email; nombre y apellidos quedan vacios (se completan despues).
            usuario.setEmail(email);
            // save(...) hace el INSERT en la tabla y devuelve el usuario con su id ya generado.
            usuario = usuarioRepository.save(usuario);
        }

        // 3) Respondemos con el id interno del usuario (JSON: {"idUsuario": N}).
        return ResponseEntity.ok(Map.of("idUsuario", usuario.getidUsuario()));
    }

    // DTO (objeto simple de transporte de datos): representa el JSON que recibe
    // /auth/login. Spring rellena estos campos a partir del cuerpo de la peticion.
    public static class LoginBridgeRequest {
        private String email;
        private String supabaseUserId;

        // Getters y setters: Spring los usa para leer/escribir cada campo del JSON.
        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getSupabaseUserId() {
            return supabaseUserId;
        }

        public void setSupabaseUserId(String supabaseUserId) {
            this.supabaseUserId = supabaseUserId;
        }
    }
}
