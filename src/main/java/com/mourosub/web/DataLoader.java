package com.mourosub.web;

import com.mourosub.web.model.Reserva;
import com.mourosub.web.model.Usuario;
import com.mourosub.web.repository.ReservaRepository;
import com.mourosub.web.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final UsuarioRepository usuarioRepo;
    private final ReservaRepository reservaRepo;

    public DataLoader(UsuarioRepository usuarioRepo, ReservaRepository reservaRepo) {
        this.usuarioRepo = usuarioRepo;
        this.reservaRepo = reservaRepo;
    }

    @Override
    public void run(String... args) throws Exception {

        // 1. Crear usuario
        Usuario u = new Usuario("Carlos López", "carlos@email.com");
        usuarioRepo.save(u);

        // 2. Crear reservas ligadas a ese usuario
        Reserva r1 = new Reserva("inmersion", "confirmada", u);
        Reserva r2 = new Reserva("curso", "pendiente", u);
        reservaRepo.save(r1);
        reservaRepo.save(r2);

        // 3. Verificar en consola
        System.out.println("=== USUARIOS EN BD ===");
        usuarioRepo.findAll().forEach(usr ->
            System.out.println("► " + usr.getIdUsuario() + " - " + usr.getNombre())
        );

        System.out.println("=== RESERVAS EN BD ===");
        reservaRepo.findAll().forEach(res ->
            System.out.println("► Reserva " + res.getIdReserva()
                + " | Tipo: " + res.getTipoServicio()
                + " | Usuario: " + res.getUsuario().getNombre())
        );
    }
}
