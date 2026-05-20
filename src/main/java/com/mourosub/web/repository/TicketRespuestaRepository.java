package com.mourosub.web.repository;

import com.mourosub.web.model.TicketRespuesta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TicketRespuestaRepository extends JpaRepository<TicketRespuesta, Long> {
    List<TicketRespuesta> findByTicketIdTicketOrderByFechaCreacionAsc(Long idTicket);
}