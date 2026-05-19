package com.mourosub.web.repository;

import com.mourosub.web.model.Tickets;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketsRepository extends JpaRepository<Tickets, Long> {

    List<Tickets> findByUsuario_IdUsuario(Long idUsuario);
}