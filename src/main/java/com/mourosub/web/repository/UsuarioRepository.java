package com.mourosub.web.repository;

import com.mourosub.web.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

// repository dice que esto maneja la conexion con los datos
// al extender de jparepository spring nos regala metodos como save o findall sin programar nada
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // usamos optional por si buscamos una id y no existe para que no reviente la aplicacion
    Optional<Usuario> findBySupabaseUserId(UUID supabaseUserId);

    // lo mismo de antes pero busca al usuario directamente por su email
    Optional<Usuario> findByEmail(String email);

    // devuelve un true o un false comprobando si el correo ya esta registrado en la bd
    boolean existsByEmail(String email);
}