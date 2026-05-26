package com.mourosub.web.service;

import com.mourosub.web.model.Fichero;
import com.mourosub.web.model.Usuario;
import com.mourosub.web.repository.FicheroRepository;
import com.mourosub.web.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

// Logica de negocio de los ficheros (los certificados que suben los usuarios).
@Service
public class FicheroService {

    // Estados posibles de un certificado.
    public static final List<String> ESTADOS = List.of("PENDIENTE", "VALIDO", "NO_VALIDO");

    private final FicheroRepository ficheroRepository;
    private final UsuarioRepository usuarioRepository;

    public FicheroService(FicheroRepository ficheroRepository, UsuarioRepository usuarioRepository) {
        this.ficheroRepository = ficheroRepository;
        this.usuarioRepository = usuarioRepository;
    }

    // Guarda un fichero subido por un usuario. El certificado empieza en estado PENDIENTE.
    public Fichero subir(MultipartFile archivo, String supabaseUserId) throws IOException {
        Usuario usuario = resolverUsuario(supabaseUserId);
        if (archivo == null || archivo.isEmpty()) {
            throw new IllegalArgumentException("Debes seleccionar un fichero.");
        }
        Fichero fichero = new Fichero();
        fichero.setBucket("certificados");
        fichero.setNombre(archivo.getOriginalFilename());
        fichero.setTipoContenido(archivo.getContentType());
        fichero.setTamano(archivo.getSize());
        // getBytes() lee el contenido del fichero en memoria para guardarlo en la columna bytea.
        fichero.setDatos(archivo.getBytes());
        fichero.setUsuario(usuario);
        fichero.setEstado("PENDIENTE");
        fichero.setFechaSubida(LocalDateTime.now());
        return ficheroRepository.save(fichero);
    }

    // Lista los ficheros subidos por un usuario concreto (para su pagina "Mis certificados").
    public List<Fichero> listarDeUsuario(String supabaseUserId) {
        Usuario usuario = resolverUsuario(supabaseUserId);
        return ficheroRepository.findByUsuario_IdUsuario(usuario.getidUsuario());
    }

    // Lista todos los ficheros (para el panel admin).
    public List<Fichero> listarTodos() {
        return ficheroRepository.findAll();
    }

    // Lista ficheros de un bucket (ej: imagenes) ordenados por fecha desc.
    public List<Fichero> listarPorBucket(String bucket) {
        if (bucket == null || bucket.isBlank()) return List.of();
        return ficheroRepository.findByBucketOrderByFechaSubidaDesc(bucket.trim());
    }

    // Busca un fichero por su id; lanza error si no existe.
    public Fichero buscarPorId(Long id) {
        return ficheroRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Fichero no encontrado: " + id));
    }

    // Cambia el estado de validacion de un certificado (lo usa el admin).
    public void cambiarEstado(Long id, String estado) {
        if (estado == null || !ESTADOS.contains(estado)) {
            throw new IllegalArgumentException("Estado no valido");
        }
        Fichero fichero = buscarPorId(id);
        fichero.setEstado(estado);
        ficheroRepository.save(fichero);
    }

    // Subida de imagen para contenido (admin): se guarda en bucket "imagenes".
    public Fichero subirImagenAdmin(MultipartFile archivo) throws IOException {
        if (archivo == null || archivo.isEmpty()) {
            throw new IllegalArgumentException("Debes seleccionar una imagen.");
        }
        String tipo = archivo.getContentType() == null ? "" : archivo.getContentType().toLowerCase();
        if (!tipo.startsWith("image/")) {
            throw new IllegalArgumentException("El fichero debe ser una imagen.");
        }

        Fichero fichero = new Fichero();
        fichero.setBucket("imagenes");
        fichero.setNombre(archivo.getOriginalFilename());
        fichero.setTipoContenido(archivo.getContentType());
        fichero.setTamano(archivo.getSize());
        fichero.setDatos(archivo.getBytes());
        fichero.setEstado("VALIDO");
        fichero.setFechaSubida(LocalDateTime.now());
        return ficheroRepository.save(fichero);
    }

    // Resuelve el Usuario de la BD a partir de su id de Supabase. Lanza error si no hay sesion.
    private Usuario resolverUsuario(String supabaseUserId) {
        if (supabaseUserId == null || supabaseUserId.isBlank()) {
            throw new IllegalArgumentException("Necesitas iniciar sesion.");
        }
        UUID uuid;
        try {
            uuid = UUID.fromString(supabaseUserId.trim());
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Sesion no valida; vuelve a iniciar sesion.");
        }
        return usuarioRepository.findBySupabaseUserId(uuid)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Tu usuario no existe en la base de datos; vuelve a iniciar sesion."));
    }
}
