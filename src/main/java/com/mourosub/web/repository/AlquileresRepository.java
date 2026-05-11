package com.mourosub.web.repository;

import com.mourosub.web.model.Alquileres;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// con esto spring ya sabe como guardar borrar y buscar alquileres
@Repository
public interface AlquileresRepository extends JpaRepository <Alquileres, Long> {
    
}
