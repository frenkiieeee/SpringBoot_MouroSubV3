package com.mourosub.web.service;
import  com.mourosub.web.model.Actividad;
import com.mourosub.web.model.Reserva;
import com.mourosub.web.repository.ActividadRepository;
import com.mourosub.web.repository.InstructorRepository;
import com.mourosub.web.repository.ReservaRepository;
import com.mourosub.web.repository.UsuarioRepository;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ReservaService {
    
    private final ReservaRepository reservaRepo;
    private final ActividadRepository actividadRepo;
    private final UsuarioRepository usuarioRepo;
    private final InstructorRepository instructorRepo;

    public ReservaService (ReservaRepository reservaRepo,
                           ActividadRepository actividadRepo,
                           UsuarioRepository usuarioRepo,
                           InstructorRepository instructorRepo) {
        this.reservaRepo = reservaRepo;
        this.actividadRepo = actividadRepo;
        this.usuarioRepo = usuarioRepo;
        this.instructorRepo = instructorRepo;
    }

    //Crear reserva

    public Reserva crearReserva (Reserva reserva, List<Long> usuarioIds){

        //Obtener actividad
        Actividad actividad = actividadRepo.findById(reserva.getActividad().getIdActividad())
        .orElseThrow(()-> new RuntimeException("Actividad no encontrada"));
    }

     return reservaRepo.save(reserva);



}
