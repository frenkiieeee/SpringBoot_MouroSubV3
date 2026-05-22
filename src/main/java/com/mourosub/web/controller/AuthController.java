package com.mourosub.web.controller;

import com.mourosub.web.model.Usuario;
import com.mourosub.web.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

// restcontroller es la clave porque devuelve datos puros en json y es ideal para hablar con el frontend
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // esto es super importante porque lo llamamos desde el front justo despues de que el usuario haga login en supabase
    @PostMapping("/sync")
    public ResponseEntity<?> sync(@RequestBody SyncUsuarioRequest request) {
        
        // convertimos la id que manda supabase que es texto normal a formato uuid de java
        UUID id;
        try {
            id = UUID.fromString(request.getSupabaseUserId());
        } catch (IllegalArgumentException ex) {
            // si la id es un desastre o viene vacia cortamos el rollo devolviendo un error 400
            return ResponseEntity.badRequest().body(Map.of("error", "supabaseUserId invalido"));
        }

        // miramos si el usuario ya existe en nuestra propia base de datos
        Optional<Usuario> existente = usuarioRepository.findBySupabaseUserId(id);
        Usuario usuario;
        
        if (existente.isPresent()) {
            // si ya lo teniamos guardado de antes nos lo quedamos para actualizarle cosas luego
            usuario = existente.get();
        } else {
            // si no existe es su primera vez en la app asi que creamos un usuario vacio y le pegamos la id de supabase
            usuario = new Usuario();
            usuario.setSupabaseUserId(id);
        }

        // le metemos todos los datos personales si es que los ha mandado en la peticion
        if (request.getEmail() != null) usuario.setEmail(request.getEmail());
        if (request.getNombre() != null) usuario.setNombre(request.getNombre());
        if (request.getApellidos() != null) usuario.setApellidos(request.getApellidos());
        if (request.getDni() != null) usuario.setDni(request.getDni());
        if (request.getTelefono() != null) usuario.setTelefono(request.getTelefono());
        if (request.getDireccion() != null) usuario.setDireccion(request.getDireccion());
        if (request.getCodPostal() != null) usuario.setCodPostal(request.getCodPostal());
        if (request.getLocalidad() != null) usuario.setLocalidad(request.getLocalidad());

        // guardamos la creacion o los cambios en nuestra base de datos
        usuario = usuarioRepository.save(usuario);

        // devolvemos un ok con los datos del usuario listos para que el frontend pueda usarlos
        return ResponseEntity.ok(usuario);
    }
}