package com.mourosub.web.repository;

import com.mourosub.web.model.Actividad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// esta anotacion convierte la interfaz en un bean de spring
@Repository
// heredar de jparepository nos regala metodos como save o findall sin programarlos
// hay que pasarle el nombre de la clase y el tipo de dato de su id
public interface ActividadRepository extends JpaRepository<Actividad, Long> {
}