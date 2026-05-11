package com.mourosub.web.model;

import jakarta.persistence.*;

/**
 * Clase que representa un curso en el sistema, el cual es un tipo de actividad.
 * Los cursos comparten la clave primaria con la entidad Actividad y añaden
 * campos específicos como categoría, nivel requerido y duración en horas.
 */
@Entity
@Table(name = "cursos")
@PrimaryKeyJoinColumn(name = "id_actividad")
// Comparte PK con Actividad (JOINED) y agrega campos propios del curso.
public class Curso extends Actividad {

    @ManyToOne
    @JoinColumn(name = "id_categoria", nullable = false)
    private CategoriaCurso categoria;

    @Column(nullable = false, length = 50)
    private String nivelRequerido = "ninguno";

    @Column
    private Integer duracionHoras;

    public CategoriaCurso getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaCurso categoria) {
        this.categoria = categoria;
    }

    public String getNombre() {
        // Delegamos al campo privado definido en Actividad.
        return super.getNombre();
    }

    public void setNombre(String nombre) {
        // Delegamos al setter de Actividad para evitar acceso directo al campo.
        super.setNombre(nombre);
    }

    public String getDescripcion() {
        // Delegamos al campo privado definido en Actividad.
        return super.getDescripcion();
    }

    public void setDescripcion(String descripcion) {
        // Delegamos al setter de Actividad para evitar acceso directo al campo.
        super.setDescripcion(descripcion);
    }

    public String getNivelRequerido() {
        return nivelRequerido;
    }

    public void setNivelRequerido(String nivelRequerido) {
        this.nivelRequerido = nivelRequerido;
    }

    public Integer getDuracionHoras() {
        return duracionHoras;
    }

    public void setDuracionHoras(Integer duracionHoras) {
        this.duracionHoras = duracionHoras;
    }
}
