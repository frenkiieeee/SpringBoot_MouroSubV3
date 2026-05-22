package com.mourosub.web.controller;

import com.mourosub.web.model.Usuario;
import com.mourosub.web.service.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

// controller significa que esto devuelve vistas html o plantillas y no datos en crudo
@Controller
// todas las rutas de este archivo van a ir detras de la barra usuarios
@RequestMapping("/usuarios")
public class UsuarioController {
    
    // nos traemos el servicio para poder hacer cosas y manejar a los usuarios
    private final UsuarioService usuarioService;
    
    // spring inyecta el servicio automaticamente en el constructor
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // si alguien entra sin nada mas lo mandamos directo al panel de administrador
    @GetMapping
    public String listar() {
        return "redirect:/admin/usuarios";
    }

    // cuando mandan el formulario relleno por metodo post
    @PostMapping("/guardar")
    // modelattribute pilla todos los datos del html y los mete de golpe en el objeto usuario
    public String guardar(@ModelAttribute Usuario usuario) {
        // si no tiene id es que hay algo raro porque el registro va por supabase asi que lo echamos al login
        if (usuario.getidUsuario() == null) {
            return "redirect:/login";
        }
        // buscamos al usuario en la bd y le pisamos los datos viejos con los que acaban de meter
        Usuario existente = usuarioService.buscarPorId(usuario.getidUsuario());
        existente.setNombre(usuario.getNombre());
        existente.setApellidos(usuario.getApellidos());
        existente.setEmail(usuario.getEmail());
        existente.setDni(usuario.getDni());
        existente.setTelefono(usuario.getTelefono());
        existente.setDireccion(usuario.getDireccion());
        existente.setCodPostal(usuario.getCodPostal());
        existente.setLocalidad(usuario.getLocalidad());
        existente.setFechaNacimiento(usuario.getFechaNacimiento());
        
        // mandamos guardar todo el tocho de datos actualizados
        usuarioService.guardar(existente);
        
        // le redirigimos a su cuenta para que vea que se han guardado los cambios
        return "redirect:/cuenta";
    }

    // ruta para editar donde la id es una variable metida directamente en la url
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        // el model es una especie de mochila que le pasamos al html para que pinte los datos del usuario
        model.addAttribute("usuario", usuarioService.buscarPorId(id));
        return "auth/registro";
    }

    // ruta para borrar donde pillamos la id de la url y le decimos al servicio que lo elimine
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        usuarioService.eliminar(id);
        return "redirect:/usuarios";
    }
}