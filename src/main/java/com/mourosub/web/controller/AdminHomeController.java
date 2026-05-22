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

// controla la seccion donde manda el jefe
@Controller
public class AdminHomeController {

    // los tres repositorios necesarios para extraer los datos crudos
    private final UsuarioRepository usuarioRepository;
    private final ReservaRepository reservaRepository;
    private final FicheroRepository ficheroRepository;

    // inyeccion por constructor de toda la vida
    public AdminHomeController(UsuarioRepository usuarioRepository,
                               ReservaRepository reservaRepository,
                               FicheroRepository ficheroRepository) {
        this.usuarioRepository = usuarioRepository;
        this.reservaRepository = reservaRepository;
        this.ficheroRepository = ficheroRepository;
    }

    // carga el dashboard de inicio del admin
    @GetMapping("/admin")
    public String index() {
        return "admin/index";
    }

    // manda todas las reservas de la base de datos de golpe a la vista para el listado
    @GetMapping("/admin/reservas")
    public String reservas(Model model) {
        model.addAttribute("reservas", reservaRepository.findAll());
        return "admin/reservas/lista";
    }

    // saca un listado general de todos los clientes registrados
    @GetMapping("/admin/usuarios")
    public String usuarios(Model model) {
        model.addAttribute("usuarios", usuarioRepository.findAll());
        return "admin/usuarios/lista";
    }

    // carga el perfil detallado de un usuario concreto tirando de su id
    @GetMapping("/admin/usuarios/{id}")
    public String usuarioDetalle(@PathVariable Long id, Model model) {
        Usuario usuario = usuarioRepository.findById(id).orElse(null);
        model.addAttribute("usuario", usuario);
        
        // si lo encuentra le saca tambien los ficheros o certificados que haya subido
        if (usuario != null) {
            model.addAttribute("ficheros", ficheroRepository.findByUsuario_IdUsuario(id));
        }
        return "admin/usuarios/detalle";
    }

    // endpoint rest oculto que sirve para que supabase pregunte si alguien tiene rango de admin al loguearse
    @GetMapping("/auth/admin-check")
    @ResponseBody
    public Map<String, Object> adminCheck(@RequestParam("supabaseUserId") String supabaseUserId) {
        // pilla la id de supabase comprueba el boolean isadmin de la tabla y devuelve true o false
        boolean isAdmin = usuarioRepository.findBySupabaseUserId(UUID.fromString(supabaseUserId))
                .map(u -> u.isAdmin())
                .orElse(false);
        
        // escupe un map que spring convierte en un json con la respuesta automatica
        return Map.of("isAdmin", isAdmin);
    }
}