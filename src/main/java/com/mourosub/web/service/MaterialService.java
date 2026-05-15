package com.mourosub.web.service;

import com.mourosub.web.exception.exceptions.*;
import com.mourosub.web.model.*;
import com.mourosub.web.repository.*;

import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class MaterialService {

    private final MaterialRepository materialRepo;

    public MaterialService(MaterialRepository materialRepo) {
        this.materialRepo = materialRepo;
    }

    // crear o actualizar material
    @Transactional
    public Material guardarMaterial(Material material) {
        // comprobamos que los datos basicos esten antes de guardar
        if (material.getNombre() == null || material.getNombre().isEmpty()) {
            throw new RuntimeException("el nombre del material es obligatorio");
        }

        // guardamos el material
        return materialRepo.save(material);
    }

    // obtener todos los materiales
    public List<Material> listarTodos() {
        return materialRepo.findAll();
    }

    // buscar material especifico
    public Material buscarPorId(Long idMaterial) {
        return materialRepo.findById(idMaterial)
                .orElseThrow(() -> new MaterialNotFoundException("material no encontrado con id: " + idMaterial));
    }

    /*===================
      eliminar material
      ===================
    */
    @Transactional
    public void eliminarMaterial(Long idMaterial) {
        // obtener material o lanzar excepcion
        Material material = materialRepo.findById(idMaterial)
                .orElseThrow(() -> new MaterialNotFoundException("material no encontrado con id: " + idMaterial));

        // comprobar disponibilidad u otras relaciones si las hubiera (alquileres)
        if (!material.getAlquileres().isEmpty()) {
            throw new RuntimeException("no se puede eliminar el material porque tiene alquileres asociados");
        }

        // guardar cambios eliminando de la base de datos
        materialRepo.delete(material);
    }
}