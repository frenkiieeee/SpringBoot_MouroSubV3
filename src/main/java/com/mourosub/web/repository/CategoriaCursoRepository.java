package com.mourosub.web.repository;

import com.mourosub.web.model.CategoriaCurso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// repositorio para la gestion de categorias
@Repository
public interface CategoriaCursoRepository extends JpaRepository<CategoriaCurso, Long> {
}