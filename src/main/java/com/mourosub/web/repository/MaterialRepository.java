package com.mourosub.web.repository;

import com.mourosub.web.model.Material;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// con esto spring ya sabe como guardar borrar y buscar materiales
@Repository
public interface MaterialRepository extends JpaRepository<Material, Long>{
    
}
