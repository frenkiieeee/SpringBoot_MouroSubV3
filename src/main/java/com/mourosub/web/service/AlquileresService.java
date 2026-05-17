package com.mourosub.web.service;
import com.mourosub.web.dto.AlquileresFormDTO;
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
    private final  AlquileresRepository alquileresRepository;
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
    public Alquileres crearAlquiler(AlquileresFormDTO alquileresFormDTO) {
        validarDatosAlquiler(alquileresFormDTO);

        Material material = materialRepository.findById(alquileresFormDTO.getMaterialId())
                .orElseThrow(() -> new MaterialNotFoundException("Material no encontrado"));
        //Llamamos al repositorio de usuario para comprobar si existe el usuario
        Usuario usuario = usuarioRepository.findById(alquileresFormDTO.getUsuarioId())
                .orElseThrow(() -> new UsuarioNotFoundException("Usuario no encontrado"));

        Alquileres alquileres = new Alquileres();
        alquileres.setMaterial(material);
        alquileres.setUsuario(usuario);
        alquileres.setFechaAlquiler(LocalDate.now());
        alquileres.setFechaDevolucion(alquileresFormDTO.getFechaDevolucion());
        alquileres.setDisponible(true);
        //Chrono es una clase de java.time que representa unidades de tiempo (dias, meses, horas...) Y con el betewen calculamos la diferencia entre las dos fechas que querramos
        long dias = ChronoUnit.DAYS.between(alquileres.getFechaAlquiler(),
                                            alquileres.getFechaDevolucion()
        );
        /*En este caso al estars haciendo uso de BigDeciaml no podemos usar "*" por lo que debemos usar para sumar a precio base el precio por dias es lo siguiente
        * primero llamamos a la variable de precio base de material, con el .add que equivale a "+" y entre parentesis para la operacion de precioPorDia * los dias que se alquila
        * Con ello lo que se presenta dentro es la multiplicacion que se hace con el .multiply */
        BigDecimal precioTotal = material.getPrecioBase().add(material.getPrecioPorDia().multiply(BigDecimal.valueOf(dias)));
        alquileres.setPrecioTotal(precioTotal);

        //Calculamos el preico por dia
        return alquileresRepository.save(alquileres);
    }

    private void validarDatosAlquiler(AlquileresFormDTO alquileresFormDTO) {
        //Validaciones basicas, asi envitamos que se queden vacios los campos qu eno deberian de estar vacios
        if (alquileresFormDTO == null) {
            throw new IllegalArgumentException("El alquiler no puede estar vacio");
        }

        if (alquileresFormDTO.getMaterialId() == null) {
            throw new MaterialNotFoundException("Debe indicarse el material del alquiler");
        }

        if (alquileresFormDTO.getUsuarioId() == null) {
            throw new UsuarioNotFoundException("Debe indicarse el usuario del alquiler");
        }

        if (alquileresFormDTO.getFechaDevolucion() == null) {
            throw new IllegalArgumentException("Debe indicarse la fecha de devolucion");
        }

        if (alquileresFormDTO.getFechaDevolucion().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha de devolucion no puede ser anterior a hoy");
        }

    }
}
