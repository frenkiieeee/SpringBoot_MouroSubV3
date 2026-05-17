package com.mourosub.web.repository;

import com.mourosub.web.model.TicketServicioTecnico;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

// Repositorio de tickets de servicio tecnico.
// Extiende JpaRepository: hereda save, findById, findAll, deleteById, etc.
public interface TicketServicioTecnicoRepository extends JpaRepository<TicketServicioTecnico, Long> {

    // Todos los tickets ordenados por fecha de creacion, del mas nuevo al mas antiguo (Desc).
    List<TicketServicioTecnico> findAllByOrderByFechaCreacionDesc();

    // Solo los tickets de un estado concreto, tambien ordenados del mas nuevo al mas antiguo.
    // Spring deriva la consulta del nombre: "ByEstado" -> WHERE estado = ?
    List<TicketServicioTecnico> findByEstadoOrderByFechaCreacionDesc(String estado);
}
