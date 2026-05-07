package com.mourosub.web.repository;

import com.mourosub.web.model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    // Busca todas las reservas de un usuario por su ID
    List<Reserva> findByUsuario_IdUsuario(Long idUsuario);
}
