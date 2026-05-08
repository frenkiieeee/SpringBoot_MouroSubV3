package com.mourosub.web;

import com.mourosub.web.model.Instructor;
import com.mourosub.web.model.Reserva;
import com.mourosub.web.model.Usuario;
import com.mourosub.web.repository.InstructorRepository;
import com.mourosub.web.repository.ReservaRepository;
import com.mourosub.web.repository.UsuarioRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
@ConditionalOnProperty(prefix = "app.seed", name = "enabled", havingValue = "true")
public class DataLoader implements CommandLineRunner {

    private final UsuarioRepository usuarioRepo;
    private final ReservaRepository reservaRepo;
    private final InstructorRepository instructorRepo;

    public DataLoader(UsuarioRepository usuarioRepo, ReservaRepository reservaRepo,
                      InstructorRepository instructorRepo) {
        this.usuarioRepo = usuarioRepo;
        this.reservaRepo = reservaRepo;
        this.instructorRepo = instructorRepo;
    }

    @Override
    public void run(String... args) throws Exception {

        // 1. Crear instructor
        Instructor instructor = new Instructor("María García", "12345678A", "PADI Divemaster, SSI Level 3");
        instructorRepo.save(instructor);

        // 2. Crear usuario
        Usuario usuario = new Usuario(
            "Carlos", "López Martínez", "carlos@email.com",
            "87654321B", "612345678", "Calle Mayor 10",
            "12540", "Vila-real", LocalDate.of(1990, 5, 15)
        );
        usuarioRepo.save(usuario);

        // 3. Crear reservas
        Reserva r1 = new Reserva(
            "inmersion", "bautismo", "SRV-001",
            2, new BigDecimal("120.00"), "confirmada",
            LocalDate.of(2026, 6, 10), usuario, instructor
        );
        Reserva r2 = new Reserva(
            "curso", "open_water", "SRV-002",
            1, new BigDecimal("350.00"), "pendiente",
            LocalDate.of(2026, 6, 20), usuario, instructor
        );
        reservaRepo.save(r1);
        reservaRepo.save(r2);

        // 4. Verificar en consola
        System.out.println("=== INSTRUCTORES EN BD ===");
        instructorRepo.findAll().forEach(i ->
            System.out.println("► " + i.getIdInstructor() + " - " + i.getNombre())
        );

        System.out.println("=== USUARIOS EN BD ===");
        usuarioRepo.findAll().forEach(u ->
            System.out.println("► " + u.getIdUsuario() + " - " + u.getNombre() + " " + u.getApellidos())
        );

        System.out.println("=== RESERVAS EN BD ===");
        reservaRepo.findAll().forEach(r ->
            System.out.println("► Reserva " + r.getIdReserva()
                + " | Tipo: " + r.getTipoServicio()
                + " | Precio: " + r.getPrecioTotal() + "€"
                + " | Instructor: " + r.getInstructor().getNombre()
                + " | Usuario: " + r.getUsuario().getNombre())
        );
    }
}
