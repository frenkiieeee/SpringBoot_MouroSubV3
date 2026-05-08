package com.mourosub.web.controller;

import com.mourosub.web.model.Usuario;
import com.mourosub.web.repository.UsuarioRepository;
import com.mourosub.web.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

// indicamos que es un controlador que devuelve texto o json directamente
@RestController
// la ruta raiz para entrar aqui sera /auth
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private JwtUtil jwtUtil;

    // creamos un endpoint post en /auth/login
    @PostMapping("/login")
    public String login(@RequestBody Usuario credenciales) {

        // buscamos al usuario en la base de datos usando el email que nos manda
        Usuario usuario = usuarioRepository.findByEmail(credenciales.getEmail());

        // comprobamos si el usuario existe y si su clave coincide con la que nos envia
        // nota en el mundo real las claves van encriptadas pero de momento lo hacemos en texto plano
        if (usuario != null && usuario.getPassword().equals(credenciales.getPassword())) {
            // si la clave es correcta le generamos su llave virtual y se la damos
            return jwtUtil.generarToken(credenciales.getEmail());
        }

        // si el email no existe o la clave esta mal devolvemos un error
        return "error credenciales incorrectas";
    }
}