package com.mourosub.web.service;

import com.mourosub.web.model.Inmersiones;
import com.mourosub.web.repository.InmersionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InmersionesService {

    private final InmersionRepository inmersionRepository;

    public InmersionesService(InmersionRepository inmersionRepository) {
        this.inmersionRepository = inmersionRepository;
    }

    public List<Inmersiones> listarTodas() {
        return inmersionRepository.findAll();
    }

    public List<Inmersiones> listarActivas() {
        return inmersionRepository.findByActivoTrue();
    }

    public Inmersiones buscarPorId(Long id) {
        return inmersionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inmersión no encontrada: " + id));
    }

    public Inmersiones guardar(Inmersiones inmersion) {
        return inmersionRepository.save(inmersion);
    }

    public void eliminar(Long id) {
        inmersionRepository.deleteById(id);
    }
}
