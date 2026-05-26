package com.mourosub.web.controller;

import com.mourosub.web.model.Usuario;
import com.mourosub.web.repository.FicheroRepository;
import com.mourosub.web.repository.ReservaRepository;
import com.mourosub.web.repository.UsuarioRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;
import java.util.UUID;

@Controller
public class AdminHomeController {

    private final UsuarioRepository usuarioRepository;
    private final ReservaRepository reservaRepository;
    private final FicheroRepository ficheroRepository;

    public AdminHomeController(
            UsuarioRepository usuarioRepository,
            ReservaRepository reservaRepository,
            FicheroRepository ficheroRepository
    ) {
        this.usuarioRepository = usuarioRepository;
        this.reservaRepository = reservaRepository;
        this.ficheroRepository = ficheroRepository;
    }

    @GetMapping("/admin")
    public String index() {
        return "admin/index";
    }

    @GetMapping("/admin/reservas")
    public String reservas(Model model) {
        model.addAttribute("reservas", reservaRepository.findAll());
        return "admin/reservas/lista";
    }

    @GetMapping("/admin/usuarios")
    public String usuarios(Model model) {
        model.addAttribute("usuarios", usuarioRepository.findAll());
        return "admin/usuarios/lista";
    }

    @GetMapping("/admin/usuarios/{id}")
    public String usuarioDetalle(@PathVariable Long id, Model model) {
        Usuario usuario = usuarioRepository.findById(id).orElse(null);
        model.addAttribute("usuario", usuario);

        if (usuario != null) {
            model.addAttribute("ficheros", ficheroRepository.findByUsuario_IdUsuario(id));
        }
        return "admin/usuarios/detalle";
    }

    @GetMapping("/auth/admin-check")
    @ResponseBody
    public Map<String, Object> adminCheck(@RequestParam("supabaseUserId") String supabaseUserId) {
        boolean isAdmin = usuarioRepository.findBySupabaseUserId(UUID.fromString(supabaseUserId))
                .map(u -> u.isAdmin())
                .orElse(false);

        return Map.of("isAdmin", isAdmin);
    }
}