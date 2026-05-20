package com.mourosub.web.controller;

import com.mourosub.web.model.Usuario;
import com.mourosub.web.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

// @RestController: como @Controller, pero todos sus metodos devuelven DATOS (JSON),
// no plantillas. Es un controlador de "API": el frontend lo llama con fetch().
// @RequestMapping("/auth"): todas las rutas de esta clase empiezan por /auth.
@RestController
@RequestMapping("/auth")
public class AuthController {

    // Repositorio para leer/escribir en la tabla 'usuarios'.
    // @Autowired: Spring inyecta aqui la instancia el solo (inyeccion por campo).
    @Autowired
    private UsuarioRepository usuarioRepository;

    // ───────────────────────────────────────────────────────────────────────
    // POST /auth/login  ->  endpoint "puente".
    // Supabase ya ha autenticado al usuario; esto solo BUSCA su fila de dominio
    // y la devuelve. No crea nada: si no existe, responde 404.
    // ───────────────────────────────────────────────────────────────────────
    @PostMapping("/login")
    // @RequestBody: los datos llegan en el cuerpo de la peticion como JSON y
    // Spring los convierte en un objeto LoginBridgeRequest (la clase de abajo).
    public ResponseEntity<?> login(@RequestBody LoginBridgeRequest request) {
        // Optional<Usuario>: "puede haber un usuario o no"; obliga a comprobar el caso vacio.
        Optional<Usuario> usuario;

        // Si vino el id de Supabase, buscamos por ese id (es el identificador fiable).
        if (request.getSupabaseUserId() != null && !request.getSupabaseUserId().isBlank()) {
            try {
                // El id viaja como texto; lo convertimos al tipo UUID.
                UUID id = UUID.fromString(request.getSupabaseUserId());
                usuario = usuarioRepository.findBySupabaseUserId(id);
            } catch (IllegalArgumentException ex) {
                // Si el texto no es un UUID valido, respondemos 400 (peticion incorrecta).
                return ResponseEntity.badRequest().body(Map.of("error", "supabaseUserId invalido"));
            }
        } else {
            // Si no vino el id, buscamos por email como alternativa.
            usuario = usuarioRepository.findByEmail(request.getEmail());
        }

        // Si el Optional esta vacio, el usuario no existe en nuestra BD -> 404.
        if (usuario.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of("error", "usuario no encontrado en dominio"));
        }

        // Sacamos el usuario del Optional y devolvemos sus datos como JSON.
        return ResponseEntity.ok(toUsuarioPayload(usuario.get()));
    }

    // ───────────────────────────────────────────────────────────────────────
    // POST /auth/sync  ->  asegura que el usuario exista en nuestra tabla 'usuarios'.
    //
    // El problema que resuelve: cuando alguien se registra, Supabase Auth lo guarda
    // en SU tabla (auth.users), pero nuestra app necesita ademas una fila propia en
    // mourosub.usuarios (para enlazar reservas, certificados, etc.). Nada las conecta
    // automaticamente, asi que este endpoint lo hace.
    //
    // Patron "find-or-create" (buscar o crear): si la fila ya existe no toca nada;
    // si no existe, la crea. Lo llama login.html justo despues de registrarse o
    // iniciar sesion, asi que el usuario siempre acaba teniendo su fila de dominio.
    // ───────────────────────────────────────────────────────────────────────
    @PostMapping("/sync")
    public ResponseEntity<?> sync(@RequestParam(value = "supabaseUserId", required = false) String supabaseUserIdParam,
                                  @RequestParam(value = "email", required = false) String emailParam,
                                  @RequestParam(value = "nombre", required = false) String nombreParam,
                                  @RequestParam(value = "apellidos", required = false) String apellidosParam,
                                  @RequestParam(value = "dni", required = false) String dniParam,
                                  @RequestParam(value = "telefono", required = false) String telefonoParam,
                                  @RequestParam(value = "direccion", required = false) String direccionParam,
                                  @RequestParam(value = "codPostal", required = false) String codPostalParam,
                                  @RequestParam(value = "localidad", required = false) String localidadParam,
                                  @RequestParam(value = "fechaNacimiento", required = false) String fechaNacimientoParam,
                                  @RequestBody(required = false) SyncUsuarioRequest request) {
        // 1) El id de Supabase llega como texto; lo convertimos al tipo UUID.
        String supabaseUserId = firstNonBlank(
                request != null ? request.getSupabaseUserId() : null,
                supabaseUserIdParam
        );
        if (supabaseUserId == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "supabaseUserId obligatorio"));
        }

        UUID id;
        try {
            id = UUID.fromString(supabaseUserId);
        } catch (IllegalArgumentException ex) {
            // Si no es un UUID valido, cortamos aqui con un 400.
            return ResponseEntity.badRequest().body(Map.of("error", "supabaseUserId invalido"));
        }

        // 2) Buscar o crear (find-or-create):
        //    findBySupabaseUserId(id) devuelve un Optional<Usuario>: puede traer al
        //    usuario o venir vacio. Comprobamos cual es el caso con un if normal.
        Optional<Usuario> existente = usuarioRepository.findBySupabaseUserId(id);
        Usuario usuario;
        if (existente.isPresent()) {
            // El usuario YA existe en nuestra tabla: lo usamos tal cual, no creamos nada.
            usuario = existente.get();
        } else {
            // El usuario NO existe todavia: creamos su fila nueva.
            usuario = new Usuario();
            // Guardamos el id de Supabase: es el enlace entre auth.users y nuestra fila.
            usuario.setSupabaseUserId(id);
        }

        aplicarDatosUsuario(usuario, request, emailParam, nombreParam, apellidosParam, dniParam, telefonoParam,
                direccionParam, codPostalParam, localidadParam, fechaNacimientoParam);
        // save(...) hace INSERT o UPDATE y devuelve el usuario persistido.
        usuario = usuarioRepository.save(usuario);

        return ResponseEntity.ok(toUsuarioPayload(usuario));
    }

    // GET /auth/profile -> devuelve los datos personales guardados en nuestra tabla usuarios.
    @GetMapping("/profile")
    public ResponseEntity<?> profile(@RequestParam("supabaseUserId") String supabaseUserId) {
        UUID id;
        try {
            id = UUID.fromString(supabaseUserId);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(Map.of("error", "supabaseUserId invalido"));
        }

        return usuarioRepository.findBySupabaseUserId(id)
                .<ResponseEntity<?>>map(usuario -> ResponseEntity.ok(toUsuarioPayload(usuario)))
                .orElseGet(() -> ResponseEntity.status(404).body(Map.of("error", "usuario no encontrado")));
    }

    private void aplicarDatosUsuario(Usuario usuario, SyncUsuarioRequest request, String emailParam,
                                     String nombreParam, String apellidosParam, String dniParam,
                                     String telefonoParam, String direccionParam, String codPostalParam,
                                     String localidadParam, String fechaNacimientoParam) {
        if (request == null) {
            setIfPresent(usuario::setEmail, emailParam);
            setIfPresent(usuario::setNombre, nombreParam);
            setIfPresent(usuario::setApellidos, apellidosParam);
            setIfPresent(usuario::setDni, dniParam);
            setIfPresent(usuario::setTelefono, telefonoParam);
            setIfPresent(usuario::setDireccion, direccionParam);
            setIfPresent(usuario::setCodPostal, codPostalParam);
            setIfPresent(usuario::setLocalidad, localidadParam);
            setFechaNacimientoIfPresent(usuario, fechaNacimientoParam);
            return;
        }

        setIfPresent(usuario::setEmail, request.getEmail());
        setIfPresent(usuario::setNombre, request.getNombre());
        setIfPresent(usuario::setApellidos, request.getApellidos());
        setIfPresent(usuario::setDni, request.getDni());
        setIfPresent(usuario::setTelefono, request.getTelefono());
        setIfPresent(usuario::setDireccion, request.getDireccion());
        setIfPresent(usuario::setCodPostal, request.getCodPostal());
        setIfPresent(usuario::setLocalidad, request.getLocalidad());

        setFechaNacimientoIfPresent(usuario, request.getFechaNacimiento());
    }

    private Map<String, Object> toUsuarioPayload(Usuario usuario) {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("idUsuario", usuario.getidUsuario());
        payload.put("supabaseUserId", usuario.getSupabaseUserId().toString());
        payload.put("email", usuario.getEmail());
        payload.put("nombre", usuario.getNombre());
        payload.put("apellidos", usuario.getApellidos());
        payload.put("dni", usuario.getDni());
        payload.put("telefono", usuario.getTelefono());
        payload.put("direccion", usuario.getDireccion());
        payload.put("codPostal", usuario.getCodPostal());
        payload.put("localidad", usuario.getLocalidad());
        payload.put("fechaNacimiento", usuario.getFechaNacimiento() != null ? usuario.getFechaNacimiento().toString() : null);
        payload.put("isAdmin", usuario.isAdmin());
        return payload;
    }

    private String firstNonBlank(String first, String second) {
        if (first != null && !first.isBlank()) {
            return first;
        }
        if (second != null && !second.isBlank()) {
            return second;
        }
        return null;
    }

    private void setIfPresent(java.util.function.Consumer<String> setter, String value) {
        if (value != null && !value.isBlank()) {
            setter.accept(value.trim());
        }
    }

    private void setFechaNacimientoIfPresent(Usuario usuario, String value) {
        if (value != null && !value.isBlank()) {
            try {
                usuario.setFechaNacimiento(LocalDate.parse(value));
            } catch (DateTimeParseException ignored) {
                // Si la fecha llega mal formada, no rompemos el alta; simplemente no la actualizamos.
            }
        }
    }

    // DTO (objeto simple de transporte de datos): representa el JSON que recibe
    // /auth/login. Spring rellena estos campos a partir del cuerpo de la peticion.
    public static class LoginBridgeRequest {
        private String email;
        private String supabaseUserId;

        // Getters y setters: Spring los usa para leer/escribir cada campo del JSON.
        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getSupabaseUserId() {
            return supabaseUserId;
        }

        public void setSupabaseUserId(String supabaseUserId) {
            this.supabaseUserId = supabaseUserId;
        }
    }

    public static class SyncUsuarioRequest {
        private String supabaseUserId;
        private String email;
        private String nombre;
        private String apellidos;
        private String dni;
        private String telefono;
        private String direccion;
        private String codPostal;
        private String localidad;
        private String fechaNacimiento;

        public String getSupabaseUserId() { return supabaseUserId; }
        public void setSupabaseUserId(String supabaseUserId) { this.supabaseUserId = supabaseUserId; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }

        public String getApellidos() { return apellidos; }
        public void setApellidos(String apellidos) { this.apellidos = apellidos; }

        public String getDni() { return dni; }
        public void setDni(String dni) { this.dni = dni; }

        public String getTelefono() { return telefono; }
        public void setTelefono(String telefono) { this.telefono = telefono; }

        public String getDireccion() { return direccion; }
        public void setDireccion(String direccion) { this.direccion = direccion; }

        public String getCodPostal() { return codPostal; }
        public void setCodPostal(String codPostal) { this.codPostal = codPostal; }

        public String getLocalidad() { return localidad; }
        public void setLocalidad(String localidad) { this.localidad = localidad; }

        public String getFechaNacimiento() { return fechaNacimiento; }
        public void setFechaNacimiento(String fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }
    }
}
