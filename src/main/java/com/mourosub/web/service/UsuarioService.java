package com.mourosub.web.service;

import com.mourosub.web.model.Usuario;
import com.mourosub.web.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

// service marca esta clase como el cerebro donde esta la logica de la aplicacion
@Service
public class UsuarioService {

    // pillamos el repositorio para poder usar la bd y lo ponemos final por pura seguridad
    private final UsuarioRepository usuarioRepository;

    // inyeccion de dependencias o sea que spring nos pasa el repo automaticamente al crear el servicio
    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    // devuelve una lista con todos los usuarios que hay
    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    // guarda un usuario nuevo o actualiza uno que ya exista
    public Usuario guardar(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    // busca por nuestra id y si no lo encuentra salta un error avisando
    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + id));
    }

    // busca por la id larguisima de supabase y si no esta devuelve un nulo
    public Usuario buscarPorSupabaseId(UUID supabaseUserId) {
        return usuarioRepository.findBySupabaseUserId(supabaseUserId)
            .orElse(null);
    }

    // se carga y borra a un usuario pasandole su id
    public void eliminar(Long id) {
        usuarioRepository.deleteById(id);
    }
}