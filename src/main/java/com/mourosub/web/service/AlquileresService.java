package com.mourosub.web.service;
import com.mourosub.web.exception.exceptions.*;
import com.mourosub.web.model.*;
import com.mourosub.web.repository.*;

import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.time.*;
import java.time.temporal.ChronoUnit;

import org.springframework.stereotype.Service;

@Service
public class AlquileresService {
    private final AlquileresRepository alquileresRepository;
    private final UsuarioRepository usuarioRepository;
    private final MaterialRepository materialRepository;

    public AlquileresService(AlquileresRepository alquileresRepository,
                             UsuarioRepository usuarioRepository,
                             MaterialRepository materialRepository) {
        this.alquileresRepository = alquileresRepository;
        this.usuarioRepository = usuarioRepository;
        this.materialRepository = materialRepository;
    }

    @Transactional
    public Alquileres crearAlquiler(Alquileres alquiler, Long materialId, Long usuarioId) {
        if (alquiler.getFechaDevolucion() == null) {
            throw new IllegalArgumentException("Debe indicarse la fecha de devolucion");
        }

        if (alquiler.getFechaDevolucion().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha de devolucion no puede ser anterior a hoy");
        }

        Material material = materialRepository.findById(materialId)
                .orElseThrow(() -> new MaterialNotFoundException("Material no encontrado"));

        alquiler.setMaterial(material);
        alquiler.setFechaAlquiler(LocalDate.now());
        alquiler.setDisponible(true);

        if (usuarioId != null) {
            Usuario usuario = usuarioRepository.findById(usuarioId).orElse(null);
            alquiler.setUsuario(usuario);
        }

        long dias = ChronoUnit.DAYS.between(alquiler.getFechaAlquiler(), alquiler.getFechaDevolucion());
        BigDecimal precioTotal = material.getPrecioBase().add(material.getPrecioPorDia().multiply(BigDecimal.valueOf(dias)));
        alquiler.setPrecioTotal(precioTotal);

        return alquileresRepository.save(alquiler);
    }
}
