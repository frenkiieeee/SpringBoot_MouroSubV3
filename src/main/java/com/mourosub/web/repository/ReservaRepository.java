package com.mourosub.web.repository;

import com.mourosub.web.model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

// el repo hereda de jparepository para tener los metodos basicos de sql gratis
@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    // spring hace la select automatica buscando reservas filtrando por la id del usuario
    List<Reserva> findByUsuarios_IdUsuario(Long idUsuario);
}