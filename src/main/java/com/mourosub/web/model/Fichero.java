package com.mourosub.web.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

// Entidad que funciona como un "storage": cada fila es un fichero guardado en la base
// de datos, imitando un bucket de Supabase Storage pero como tabla propia del proyecto.
// Al estar default_schema=mourosub, la tabla 'ficheros' se crea en el esquema mourosub.
@Entity
@Table(name = "ficheros")
public class Fichero {

    // Clave primaria; la genera la base de datos.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idFichero;

    // Nombre del "bucket" o carpeta logica donde se agrupa el fichero
    // (imita los buckets de Supabase Storage: documentos, imagenes, etc.).
    @Column(nullable = false, length = 100)
    private String bucket;

    // Nombre del fichero, p. ej. "manual.pdf".
    @Column(nullable = false, length = 255)
    private String nombre;

    // Tipo de contenido MIME, p. ej. "application/pdf" o "image/png".
    @Column(length = 150)
    private String tipoContenido;

    // Tamanyo del fichero en bytes.
    @Column
    private Long tamano;

    // Contenido binario del fichero. Un byte[] sin @Lob se mapea a BYTEA en PostgreSQL,
    // que es lo adecuado para guardar archivos de tamanyo moderado en una columna.
    @Column(name = "datos")
    private byte[] datos;

    // Fecha y hora de subida. Por defecto, el momento en que se crea el objeto.
    @Column(nullable = false)
    private LocalDateTime fechaSubida = LocalDateTime.now();

    // Constructor vacio: JPA lo necesita para crear los objetos.
    public Fichero() {}

    // Getters y setters: JPA los usa para leer y escribir cada campo.
    public Long getIdFichero() { return idFichero; }
    public void setIdFichero(Long idFichero) { this.idFichero = idFichero; }

    public String getBucket() { return bucket; }
    public void setBucket(String bucket) { this.bucket = bucket; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getTipoContenido() { return tipoContenido; }
    public void setTipoContenido(String tipoContenido) { this.tipoContenido = tipoContenido; }

    public Long getTamano() { return tamano; }
    public void setTamano(Long tamano) { this.tamano = tamano; }

    public byte[] getDatos() { return datos; }
    public void setDatos(byte[] datos) { this.datos = datos; }

    public LocalDateTime getFechaSubida() { return fechaSubida; }
    public void setFechaSubida(LocalDateTime fechaSubida) { this.fechaSubida = fechaSubida; }
}
