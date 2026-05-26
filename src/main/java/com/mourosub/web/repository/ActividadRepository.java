package com.mourosub.web.repository;

import com.mourosub.web.model.Actividad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository

public interface ActividadRepository extends JpaRepository<Actividad, Long> {
    List<Actividad> findByTipoIn (List<String> tipos);
    List<Actividad> findByCategoriaAndActivoTrue(String categoria);
    List<Actividad> findByCategoriaAndActivoTrueOrderByNombreAsc(String categoria);
    List<Actividad> findByActivoTrueOrderByCategoriaAscNombreAsc();
}