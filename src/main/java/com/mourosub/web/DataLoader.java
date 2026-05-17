package com.mourosub.web;

import com.mourosub.web.model.Instructor;
import com.mourosub.web.model.Reserva;
import com.mourosub.web.model.Usuario;
import com.mourosub.web.repository.InstructorRepository;
import com.mourosub.web.repository.ReservaRepository;
import com.mourosub.web.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

// Clase que inserta datos de prueba en la base de datos al arrancar la aplicacion.
// @Component: Spring la detecta y la gestiona como un bean.
@Component
// @ConditionalOnProperty: esta clase SOLO se activa si en la configuracion app.seed.enabled = true.
// Asi los datos de prueba no se insertan cuando no queremos (por ejemplo en produccion).
@ConditionalOnProperty(prefix = "app.seed", name = "enabled", havingValue = "true")
public class DataLoader implements CommandLineRunner {
    // implements CommandLineRunner: su metodo run() se ejecuta una sola vez, justo al arrancar la app.

    // Repositorios que necesitamos para guardar los datos de prueba.
    private final UsuarioRepository usuarioRepo;
    private final ReservaRepository reservaRepo;
    private final InstructorRepository instructorRepo;

    // Spring inyecta los repositorios por el constructor.
    public DataLoader(UsuarioRepository usuarioRepo, ReservaRepository reservaRepo,
                      InstructorRepository instructorRepo) {
        this.usuarioRepo = usuarioRepo;
        this.reservaRepo = reservaRepo;
        this.instructorRepo = instructorRepo;
    }

    // Este metodo se ejecuta automaticamente al arrancar la app (si el seed esta activado).
    @Override
    public void run(String... args) throws Exception {
        // 1. Crear instructor
        Instructor instructor = new Instructor("María García", "12345678A", "PADI Divemaster, SSI Level 3");
        instructorRepo.save(instructor);

        // 2. Crear usuario de prueba
        Usuario usuario = new Usuario();
        usuarioRepo.save(usuario);

        // 3. Crear reservas
        // Dos reservas de ejemplo, asociadas al usuario y al instructor creados arriba.
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

        // Mensaje en consola para confirmar que los datos se insertaron.
        System.out.println("=== DATOS DE PRUEBA INSERTADOS ===");
    }
}
