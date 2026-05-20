package com.mourosub.web.repository;

import com.mourosub.web.model.Inmersiones;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// repositorio para hacer consultas a la tabla inmersiones
@Repository
public interface InmersionRepository extends JpaRepository<Inmersiones, Long> {
    List<Inmersiones> findByActivoTrue();
}