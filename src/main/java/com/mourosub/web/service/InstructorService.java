package com.mourosub.web.service;

import com.mourosub.web.exception.exceptions.*;
import com.mourosub.web.model.*;
import com.mourosub.web.repository.*;

import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class InstructorService {

    private final InstructorRepository instructorRepo;

    public InstructorService(InstructorRepository instructorRepo) {
        this.instructorRepo = instructorRepo;
    }

    // crear o actualizar instructor
    @Transactional
    public Instructor guardarInstructor(Instructor instructor) {
        // validacion basica antes de guardar en bd
        if (instructor.getDni() == null || instructor.getDni().isEmpty()) {
            throw new RuntimeException("el dni del instructor es obligatorio");
        }
        if (instructor.getNombre() == null || instructor.getNombre().isEmpty()) {
            throw new RuntimeException("el nombre del instructor es obligatorio");
        }

        // guardamos el instructor
        return instructorRepo.save(instructor);
    }

    // obtener todos los instructores
    public List<Instructor> listarTodos() {
        return instructorRepo.findAll();
    }

    // buscar un instructor especifico
    public Instructor buscarPorId(Long idInstructor) {
        return instructorRepo.findById(idInstructor)
                .orElseThrow(() -> new RuntimeException("instructor no encontrado con id: " + idInstructor));
    }

    /* eliminar instructor */
    @Transactional
    public void eliminarInstructor(Long idInstructor) {
        // buscar al instructor o lanzar excepcion
        Instructor instructor = instructorRepo.findById(idInstructor)
                .orElseThrow(() -> new RuntimeException("instructor no encontrado con id: " + idInstructor));

        // comprobacion critica: no borrar si tiene reservas asignadas
        if (instructor.getReservas() != null && !instructor.getReservas().isEmpty()) {
            throw new RuntimeException("no se puede eliminar el instructor porque tiene reservas asignadas");
        }

        // si esta libre, lo eliminamos de la base de datos
        instructorRepo.delete(instructor);
    }
}