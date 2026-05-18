package com.mourosub.web.service;

import com.mourosub.web.model.Seguros;
import com.mourosub.web.repository.SegurosRepository;

import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class SegurosService {

    private final SegurosRepository segurosRepo;

    public SegurosService(SegurosRepository segurosRepo) {
        this.segurosRepo = segurosRepo;
    }

    // crear o actualizar un seguro
    @Transactional
    public Seguros guardarSeguro(Seguros seguro) {
        // validacion de los campos obligatorios segun tu entidad
        if (seguro.getNombre() == null || seguro.getNombre().isEmpty()) {
            throw new RuntimeException("el nombre del seguro es obligatorio");
        }
        if (seguro.getCompania() == null || seguro.getCompania().isEmpty()) {
            throw new RuntimeException("la compania del seguro es obligatoria");
        }
        if (seguro.getCoberturaGastos() == null) {
            throw new RuntimeException("la cobertura de gastos es obligatoria");
        }

        // guardamos el seguro en la base de datos
        return segurosRepo.save(seguro);
    }

    // obtener todos los seguros
    public List<Seguros> listarTodos() {
        return segurosRepo.findAll();
    }

    // buscar un seguro especifico por su id
    public Seguros buscarPorId(Long idSeguro) {
        return segurosRepo.findById(idSeguro)
                .orElseThrow(() -> new RuntimeException("seguro no encontrado con id: " + idSeguro));
    }

    /* eliminar seguro */
    @Transactional
    public void eliminarSeguro(Long idSeguro) {
        // buscar el seguro o lanzar excepcion
        Seguros seguro = buscarPorId(idSeguro);

        // en este caso el seguro es el lado muchos (n) de la relacion con usuario (1).
        // si se borra el seguro, simplemente desaparece para ese usuario.
        segurosRepo.delete(seguro);
    }
}