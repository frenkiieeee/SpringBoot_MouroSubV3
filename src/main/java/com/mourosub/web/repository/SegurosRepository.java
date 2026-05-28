package com.mourosub.web.repository;

/* comentario: repositorio para seguros, añadido comentario para aclarar cambios realizados */

import com.mourosub.web.model.Seguros;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SegurosRepository extends JpaRepository<Seguros, Long> {

    // Seguros "plantilla" del catalogo: los que no estan asociados a ningun usuario.
    List<Seguros> findByUsuarioIsNull();

    // Seguros contratados por un usuario concreto (para mostrarlos en su cuenta).
    List<Seguros> findByUsuario_IdUsuario(Long idUsuario);

    // Comprueba si el usuario ya tiene contratado un seguro con ese nombre (evita duplicados).
    boolean existsByUsuario_IdUsuarioAndNombre(Long idUsuario, String nombre);
}
