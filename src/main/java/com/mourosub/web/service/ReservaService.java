package com.mourosub.web.service;
import com.mourosub.web.exception.exceptions.*;
import com.mourosub.web.model.*;
import com.mourosub.web.repository.*;

import jakarta.transaction.Transactional;

import java.math.*;
import java.time.*;
import java.util.*;
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
    @Transactional
    public Reserva crearReserva (Reserva reserva, List<Long> usuarioIds){

        //Obtener actividad
        Actividad actividad = actividadRepo.findById(reserva.getActividad().getIdActividad())
            .orElseThrow(()-> new RuntimeException("Actividad no encontrada"));
        //Le asginamos la actividad a la reserva
            reserva.setActividad(actividad);

        //Comprobar usuarios de la reserva
        if (!comprobarDisponibilidad(actividad, reserva.getNumParticipantes())){
            throw new RuntimeException("No hay plazas disponibles");
        }
        
        //Asignar instructores disponibles
        Instructor instructor = asignarInstructorDisponible();
        reserva.getInstructores().add(instructor);
        
        //Añadir ususarios (N:M)
        List<Usuario> usuarios = usuarioRepo.findAllById(usuarioIds);
        if(usuarios.size() != usuarioIds.size()){
            throw new RuntimeException("Uno o mas usuarios no se encontraron");
        }
        reserva.setUsuario(usuarios);

        //Calcular precio
        reserva.setPrecioTotal(calcularPrecio(actividad, reserva.getNumParticipantes()));

        //Estado inicial y fecha
        reserva.setEstado("Activa");
        reserva.setFechaReserva(LocalDate.now());

        //Ocupamos las plazas ocupadas de la actividad
        actividad.setplazasOcupadas(actividad.getplazasOcupadas() + reserva.getNumParticipantes());
        

        //Guardamos la reserva
        actividadRepo.save(actividad);

     return reservaRepo.save(reserva);
    }
    
    /*===================
      Cancelar reserva
      ===================
    */
    @Transactional
    public void cancelarReserva (Long idReserva){

        //Obtener reserva o lanzar excepcion

        Reserva reserva = reservaRepo.findById(idReserva)
            .orElseThrow(() -> new ReservaNotFoundException("Reserva no encontrada con id: " + idReserva));
        
        //Comprobar que no este ya cancelada

        if("cancelada".equals(reserva.getEstado())){
            throw new ReservaYaCanceladaException ("Esta reserva esta cancelada");
        }

        //Comprobar plazo; si la cancelacion es el dia de la actividad o ya paso la actividad y como minimo 24h para cancelar

        LocalDate fechaActividad = reserva.getActividad().getFechaActividad();
            if(fechaActividad.isBefore(LocalDate.now().plusDays(1))){
                throw new FueraDePlazoException("No se puede cancelar la reserva con menos de 24h de antelacion");
            }
        //Cambiar estado
        reserva.setEstado("cancelada");

        //Designar instructor
        reserva.getInstructores().clear();

        //Liberar plazas de la actividad
        //LLamamos a la actividad 
        Actividad actividad = reserva.getActividad();
        /*Guardamos en una variable el numero de participantes, para mas tarde usarlo, en caso de que haya 2 personas que reservaron,
        estas se guardan en la variable y se usa mas adelante en la resta para liberar las plazas*/
        int plazasLiberadas = reserva.getNumParticipantes();
        /*En este punto vamos a actualizar las plazas ocupadas de la actividad, en primero lugar con el Math.max, evitamos que pueda quedar negativo el numero a la hora de restar
        con ello ya definido, llamamos a la tabla "actividad" para saber las plazas ocupadas de dicha actividad y con las personas guardadas anteriormente en la variable "plazasLiberadas"
        Realizamos la resta, con lo que se actualizaria la variable de plazasOcupadas y guardamos con "actividadRepo.save(actividad);" */
        actividad.setplazasOcupadas(Math.max(0,actividad.getplazasOcupadas() - plazasLiberadas));
        actividadRepo.save(actividad);

        //Eliminar usuarios asociados a la reserva
        /*reserva.getUsuarios().clear();*/

        //Guardar cambios

        reservaRepo.save(reserva);

    }

    
    //Metodos

    //Metodo de comprobar disponibilidad
    public boolean comprobarDisponibilidad (Actividad actividad, int participantes){
        return actividad.getPlazasMax() - actividad.getplazasOcupadas() >= participantes;
    }

    //Metodo para asginar instructores

    public Instructor asignarInstructorDisponible(){
        List<Instructor> instructores = instructorRepo.findAll();
        
        if(instructores.isEmpty()){
            throw new RuntimeException ("No hay instructores disponibles");
        }
        
        return instructores.get(0);
    }

    //Metodo para calcular el precio
    public BigDecimal calcularPrecio(Actividad actividad, int participantes){
        return actividad.getPrecio().multiply(BigDecimal.valueOf(participantes));
    }

}
