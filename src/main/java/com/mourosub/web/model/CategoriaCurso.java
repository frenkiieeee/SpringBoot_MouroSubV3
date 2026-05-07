package com.mourosub.web.model;

import jakarta.persistence.*;

// mapeamos la clase contra la base de datos
@Entity
// nombre real de la tabla en sql
@Table(name = "categoria_cursos")
public class CategoriaCurso {

    // clave principal
    @Id
    // se genera de forma automatica
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCategoria;

    // titulo obligatorio de la categoria
    @Column(nullable = false, length = 100)
    private String nombre;

    // campo de texto largo
    @Column(columnDefinition = "TEXT")
    private String descripcion;

    // getters y setters


    public Long getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(Long idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}