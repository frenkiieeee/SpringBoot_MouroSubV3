package com.mourosub.web.repository;

import com.mourosub.web.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

// Repositorio de usuarios.
// @Repository marca esta interfaz como componente de acceso a datos.
// Al extender JpaRepository hereda save, findById, findAll, deleteById, etc.
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Busca un usuario por su id de Supabase.
    // Optional: puede venir vacio si no existe; obliga a comprobar el caso "no encontrado".
    Optional<Usuario> findBySupabaseUserId(UUID supabaseUserId);

    // Busca un usuario por su email.
    Optional<Usuario> findByEmail(String email);

    // Devuelve true/false segun exista o no un usuario con ese email.
    boolean existsByEmail(String email);
}
