package com.mourosub.web.service;

import com.mourosub.web.exception.exceptions.ActividadNotFoundException;
import com.mourosub.web.model.Actividad;
import com.mourosub.web.repository.ActividadRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServicioActividades {

    private final ActividadRepository actividadRepository;

    public ServicioActividades(ActividadRepository actividadRepository) {
        this.actividadRepository = actividadRepository;
    }

    public List<Actividad> listarTodos() {
        return actividadRepository.findAll();
    }

    public Actividad guardar(Actividad actividad) {
        return actividadRepository.save(actividad);
    }

    public Actividad buscarPorId(Long id) {
        return actividadRepository.findById(id)
                .orElseThrow(() -> new ActividadNotFoundException("Actividad no encontrada con id: " + id));
    }

    public void eliminar(Long id) {
        Actividad actividad = buscarPorId(id);

        // Baja logica: no se borra, solo se desactiva.
        actividad.setActivo(false);

        actividadRepository.save(actividad);
    }
}