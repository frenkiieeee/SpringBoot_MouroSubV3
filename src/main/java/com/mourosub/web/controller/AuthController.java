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

// restcontroller es para que devuelva datos en crudo tipo json y no vistas html
// requestmapping hace que todas las rutas de este archivo cuelguen de la palabra auth
@RestController
@RequestMapping("/auth")
public class AuthController {

    // autowired inyecta la conexion con la base de datos de forma automatica
    @Autowired
    private UsuarioRepository usuarioRepository;

    // endpoint puente que no crea nada sino que busca al usuario despues de que supabase lo haya validado
    @PostMapping("/login")
    // requestbody pilla el json que llega de la peticion web y lo convierte en el objeto loginbridgerequest
    public ResponseEntity<?> login(@RequestBody LoginBridgeRequest request) {
        // optional sirve para manejar el caso de que el usuario exista o no y que no reviente el programa
        Optional<Usuario> usuario;

        // si nos llega el id de supabase buscamos por ahi porque es lo mas seguro y unico
        if (request.getSupabaseUserId() != null && !request.getSupabaseUserId().isBlank()) {
            try {
                // el id nos llega como un texto normal asi que lo pasamos al tipo uuid que usa java
                UUID id = UUID.fromString(request.getSupabaseUserId());
                usuario = usuarioRepository.findBySupabaseUserId(id);
            } catch (IllegalArgumentException ex) {
                // si el texto que nos pasan no tiene forma de uuid cortamos con un error 400 bad request
                return ResponseEntity.badRequest().body(Map.of("error", "supabaseUserId invalido"));
            }
        } else {
            // si por lo que sea no viene el id buscamos por el correo a ver si hay suerte
            usuario = usuarioRepository.findByEmail(request.getEmail());
        }

        // si el usuario no esta en nuestra base de datos devolvemos un 404 de no encontrado
        if (usuario.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of("error", "usuario no encontrado en dominio"));
        }

        // sacamos al usuario del optional y mandamos sus datos de vuelta usando el metodo de abajo que lo formatea
        return ResponseEntity.ok(toUsuarioPayload(usuario.get()));
    }

    // esto sirve para sincronizar el usuario de supabase con nuestra propia base de datos
    // basicamente es un buscar y si no existe lo creo para que todo cuadre
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
        
        // comprobamos si el id de supabase nos llega por el body o por parametro url usando un metodo auxiliar
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
            // si nos mandan basura en vez de un uuid paramos la ejecucion
            return ResponseEntity.badRequest().body(Map.of("error", "supabaseUserId invalido"));
        }

        // buscamos a ver si el usuario ya existe en nuestro mysql
        Optional<Usuario> existente = usuarioRepository.findBySupabaseUserId(id);
        Usuario usuario;
        if (existente.isPresent()) {
            // si ya estaba registrado nos lo quedamos y luego le actualizamos las cosas
            usuario = existente.get();
        } else {
            // si es la primera vez que entra creamos un usuario de cero y le enganchamos su id de supabase
            usuario = new Usuario();
            usuario.setSupabaseUserId(id);
        }

        // le mandamos el muerto a este metodo auxiliar para que rellene todos los datos personales y no ensuciar este codigo
        aplicarDatosUsuario(usuario, request, emailParam, nombreParam, apellidosParam, dniParam, telefonoParam,
                direccionParam, codPostalParam, localidadParam, fechaNacimientoParam);
        
        // guardamos los cambios en la base de datos y asi ya lo tenemos sincronizado
        usuario = usuarioRepository.save(usuario);

        return ResponseEntity.ok(toUsuarioPayload(usuario));
    }

    // este endpoint simplemente escupe los datos del usuario buscando por su id de supabase
    @GetMapping("/profile")
    public ResponseEntity<?> profile(@RequestParam("supabaseUserId") String supabaseUserId) {
        UUID id;
        try {
            id = UUID.fromString(supabaseUserId);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(Map.of("error", "supabaseUserId invalido"));
        }

        // busca al usuario y si lo encuentra devuelve sus datos formateados y si no lanza un error 404 muy limpio
        return usuarioRepository.findBySupabaseUserId(id)
                .<ResponseEntity<?>>map(usuario -> ResponseEntity.ok(toUsuarioPayload(usuario)))
                .orElseGet(() -> ResponseEntity.status(404).body(Map.of("error", "usuario no encontrado")));
    }

    // metodo auxiliar gigante que se encarga de ir campo por campo comprobando si hay datos nuevos y metiendoselos al usuario
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

    // convierte el tocho del objeto usuario en un mapa sencillito para que el frontend lo lea bien en json
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

    // comprueba dos textos y devuelve el primero que no este vacio o nulo
    private String firstNonBlank(String first, String second) {
        if (first != null && !first.isBlank()) {
            return first;
        }
        if (second != null && !second.isBlank()) {
            return second;
        }
        return null;
    }

    // funcion chula que coge el setter del usuario y le mete el valor solo si viene con texto de verdad limpiando los espacios
    private void setIfPresent(java.util.function.Consumer<String> setter, String value) {
        if (value != null && !value.isBlank()) {
            setter.accept(value.trim());
        }
    }

    // lo mismo que la de arriba pero intenta convertir el texto en una fecha y si explota pasa de largo sin romper nada
    private void setFechaNacimientoIfPresent(Usuario usuario, String value) {
        if (value != null && !value.isBlank()) {
            try {
                usuario.setFechaNacimiento(LocalDate.parse(value));
            } catch (DateTimeParseException ignored) {
                // si la fecha viene mal no petamos nada simplemente ignoramos la actualizacion
            }
        }
    }

    // clase tonta para atrapar el json que manda el frontend cuando intentan hacer login
    public static class LoginBridgeRequest {
        private String email;
        private String supabaseUserId;

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

    // otra clase tonta que sirve de molde para recoger todos los datos sueltos en el endpoint de sync
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