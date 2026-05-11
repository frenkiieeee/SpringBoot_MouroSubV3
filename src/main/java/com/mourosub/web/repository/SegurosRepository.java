package com.mourosub.web.repository;

import com.mourosub.web.model.Seguros;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SegurosRepository extends JpaRepository<Seguros, Long> {
    
}
