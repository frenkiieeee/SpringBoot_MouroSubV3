package com.mourosub.web.controller;

import com.mourosub.web.model.Usuario;
import com.mourosub.web.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Endpoint puente: Supabase autentica y este endpoint solo resuelve el usuario de dominio.
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginBridgeRequest request) {
        Optional<Usuario> usuario;

        if (request.getSupabaseUserId() != null && !request.getSupabaseUserId().isBlank()) {
            try {
                UUID id = UUID.fromString(request.getSupabaseUserId());
                usuario = usuarioRepository.findBySupabaseUserId(id);
            } catch (IllegalArgumentException ex) {
                return ResponseEntity.badRequest().body(Map.of("error", "supabaseUserId invalido"));
            }
        } else {
            usuario = usuarioRepository.findByEmail(request.getEmail());
        }

        if (usuario.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of("error", "usuario no encontrado en dominio"));
        }

        Usuario u = usuario.get();
        return ResponseEntity.ok(Map.of(
                "idUsuario", u.getidUsuario(),
                "supabaseUserId", u.getSupabaseUserId().toString(),
                "email", u.getEmail(),
                "nombre", u.getNombre(),
                "apellidos", u.getApellidos()
        ));
    }

    public static class LoginBridgeRequest {
        private String email;
        private String supabaseUserId;

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
