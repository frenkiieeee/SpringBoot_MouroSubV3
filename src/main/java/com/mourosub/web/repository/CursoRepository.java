package com.mourosub.web.repository;

import com.mourosub.web.model.Curso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CursoRepository extends JpaRepository<Curso, Long> {
    @Query("SELECT c FROM Curso c WHERE c.activo = true")
    List<Curso> findAllActive();
    List<Curso> findByCategoriaAndActivoTrueOrderByNombreAsc(String categoria);
    @Query(value = "SELECT DISTINCT c.categoria FROM cursos c WHERE c.activo = true AND c.categoria IS NOT NULL", nativeQuery = true)
    List<String> findDistinctCategoriaByActivoTrue();
}