package com.mourosub.web.controller;

import com.mourosub.web.model.Usuario;
import com.mourosub.web.repository.FicheroRepository;
import com.mourosub.web.repository.ReservaRepository;
import com.mourosub.web.repository.UsuarioRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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

    // GET /admin/usuarios/{id}/editar -> formulario de edicion del usuario para el admin.
    // Reutilizamos la plantilla admin/usuarios/editar que es un form sencillo con los campos basicos.
    @GetMapping("/admin/usuarios/{id}/editar")
    public String editarUsuario(@PathVariable Long id, Model model) {
        Usuario usuario = usuarioRepository.findById(id).orElse(null);
        if (usuario == null) return "redirect:/admin/usuarios";
        model.addAttribute("usuario", usuario);
        return "admin/usuarios/editar";
    }

    // POST /admin/usuarios/{id}/editar -> guarda los cambios desde el form de admin.
    @PostMapping("/admin/usuarios/{id}/editar")
    public String guardarUsuario(@PathVariable Long id, @ModelAttribute Usuario datos) {
        Usuario existente = usuarioRepository.findById(id).orElse(null);
        if (existente == null) return "redirect:/admin/usuarios";

        // Solo pisamos los campos que llegan del formulario; los demas (supabaseUserId, isAdmin, banned)
        // se mantienen como estaban para evitar que un cambio aqui afecte a la seguridad.
        existente.setNombre(datos.getNombre());
        existente.setApellidos(datos.getApellidos());
        existente.setEmail(datos.getEmail());
        existente.setDni(datos.getDni());
        existente.setTelefono(datos.getTelefono());
        existente.setDireccion(datos.getDireccion());
        existente.setCodPostal(datos.getCodPostal());
        existente.setLocalidad(datos.getLocalidad());
        existente.setFechaNacimiento(datos.getFechaNacimiento());
        usuarioRepository.save(existente);

        return "redirect:/admin/usuarios/" + id;
    }

    // POST /admin/usuarios/{id}/ban -> conmuta el flag de baneado del usuario.
    // Asi un mismo boton sirve para banear y para desbanear segun el estado actual.
    @PostMapping("/admin/usuarios/{id}/ban")
    public String toggleBan(@PathVariable Long id) {
        usuarioRepository.findById(id).ifPresent(u -> {
            u.setBanned(!u.isBanned());
            usuarioRepository.save(u);
        });
        return "redirect:/admin/usuarios";
    }

    // POST /admin/usuarios/{id}/eliminar -> borra al usuario de la base de datos.
    // Se hace por POST para evitar borrados accidentales desde un enlace.
    @PostMapping("/admin/usuarios/{id}/eliminar")
    public String eliminarUsuario(@PathVariable Long id) {
        usuarioRepository.deleteById(id);
        return "redirect:/admin/usuarios";
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