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

// Es el controlador del panel de administracion
@Controller
public class AdminHomeController {

    // Repositorios para leer datos de la base de datos.
    // 'final' = una vez que Spring los inyecta en el constructor, ya no cambian
    private final UsuarioRepository usuarioRepository;
    private final ReservaRepository reservaRepository;
    private final FicheroRepository ficheroRepository;

    // Constructor: Spring detecta estos repositorios y nos los pasa solo (inyeccion de dependencias)
    public AdminHomeController(UsuarioRepository usuarioRepository,
                               ReservaRepository reservaRepository,
                               FicheroRepository ficheroRepository) {
        this.usuarioRepository = usuarioRepository;
        this.reservaRepository = reservaRepository;
        this.ficheroRepository = ficheroRepository;
    }

    // GET /admin -> muestra la pagina principal del panel (las 4 secciones)
    @GetMapping("/admin")
    public String index() {
        // Devuelve la plantilla templates/admin/index.html
        return "admin/index";
    }

    // GET /admin/reservas -> lista todas las reservas
    @GetMapping("/admin/reservas")
    public String reservas(Model model) {
        // Pedimos al repositorio todas las reservas y las metemos en el modelo para la vista.
        model.addAttribute("reservas", reservaRepository.findAll());
        return "admin/reservas/lista";
    }

    // GET /admin/usuarios -> lista todos los usuarios registrados
    @GetMapping("/admin/usuarios")
    public String usuarios(Model model) {
        model.addAttribute("usuarios", usuarioRepository.findAll());
        return "admin/usuarios/lista";
    }

    // GET /admin/usuarios/{id} -> ficha detallada de un usuario con sus certificados.
    @GetMapping("/admin/usuarios/{id}")
    public String usuarioDetalle(@PathVariable Long id, Model model) {
        // Buscamos el usuario por su id. orElse(null): si no existe, queda null.
        Usuario usuario = usuarioRepository.findById(id).orElse(null);
        model.addAttribute("usuario", usuario);
        // Si el usuario existe, anyadimos al modelo los certificados que ha subido.
        if (usuario != null) {
            model.addAttribute("ficheros", ficheroRepository.findByUsuario_IdUsuario(id));
        }
        return "admin/usuarios/detalle";
    }

    // GET /auth/admin-check -> lo usa el login para saber si un usuario es administrador
    // @ResponseBody: en vez de devolver una plantilla, devuelve los datos directamente (en JSON)
    @GetMapping("/auth/admin-check")
    @ResponseBody
    public Map<String, Object> adminCheck(@RequestParam("supabaseUserId") String supabaseUserId) {
        // Buscamos al usuario por su id de Supabase y miramos su campo is_admin
        // Si ese usuario no existe, orElse(false) hace que el resultado sea 'false'
        boolean isAdmin = usuarioRepository.findBySupabaseUserId(UUID.fromString(supabaseUserId))
                .map(u -> u.isAdmin())
                .orElse(false);
        // Devolvemos un JSON con la forma {"isAdmin": true/false}.
        return Map.of("isAdmin", isAdmin);
    }
}
