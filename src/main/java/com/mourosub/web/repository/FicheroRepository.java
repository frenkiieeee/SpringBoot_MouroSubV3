package com.mourosub.web.repository;

import com.mourosub.web.model.Fichero;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

// Repositorio del "storage" de ficheros.
// Al extender JpaRepository hereda save, findById, findAll, deleteById, etc.
@Repository
public interface FicheroRepository extends JpaRepository<Fichero, Long> {

    // Devuelve todos los ficheros de un bucket concreto.
    // Spring genera la consulta a partir del nombre: "ByBucket" -> WHERE bucket = ?
    List<Fichero> findByBucket(String bucket);
}
