package com.mourosub.web.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

// creamos la entidad curso
@Entity
// vinculamos con la tabla cursos
@Table(name = "cursos")
public class Curso {

    // id del curso
    @Id
    // autoincremental
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCurso;

    // esta es la magia para crear una clave foranea muchos a uno
    @ManyToOne
    // le indicamos que columna fisica guarda el id de la categoria
    @JoinColumn(name = "id_categoria", nullable = false)
    private CategoriaCurso categoria;

    // nombre obligatorio del curso
    @Column(nullable = false, length = 150)
    private String nombre;

    // texto largo para la explicacion
    @Column(columnDefinition = "TEXT")
    private String descripcion;

    // nivel base por defecto
    @Column(nullable = false, length = 50)
    private String nivelRequerido = "ninguno";

    // horas totales del curso
    private Integer duracionHoras;

    // en buceo los grupos suelen ser pequeños por defecto 6
    @Column(nullable = false)
    private Integer plazasMax = 6;

    // precio con soporte para decimales
    @Column(nullable = false)
    private BigDecimal precioBase;

    // estado del curso activo o inactivo
    @Column(nullable = false)
    private Boolean activo = true;

    // getters y setters


    public Long getIdCurso() {
        return idCurso;
    }

    public void setIdCurso(Long idCurso) {
        this.idCurso = idCurso;
    }

    public CategoriaCurso getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaCurso categoria) {
        this.categoria = categoria;
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

    public Integer getPlazasMax() {
        return plazasMax;
    }

    public void setPlazasMax(Integer plazasMax) {
        this.plazasMax = plazasMax;
    }

    public BigDecimal getPrecioBase() {
        return precioBase;
    }

    public void setPrecioBase(BigDecimal precioBase) {
        this.precioBase = precioBase;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
}