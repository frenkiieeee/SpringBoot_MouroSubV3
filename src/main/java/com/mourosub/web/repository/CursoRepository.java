package com.mourosub.web.repository;

import com.mourosub.web.model.Curso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// con esto spring ya sabe como guardar borrar y buscar cursos
@Repository
public interface CursoRepository extends JpaRepository<Curso, Long> {
}