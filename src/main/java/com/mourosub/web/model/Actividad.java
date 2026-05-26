package com.mourosub.web.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "actividades")
public class Actividad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idActividad;

    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(nullable = false, length = 50)
    private String tipo;

    @Column(length = 50)
    private String categoria;

    private Integer duracionMinutos;

    @Column(nullable = false)
    private Integer plazasMax = 10;

    @Column(nullable = false)
    private int plazasOcupadas;

    @Column(nullable = false)
    private BigDecimal precio;

    @Column(nullable = false)
    private LocalDate fechaActividad;

    @Column(nullable = false)
    private Boolean requiereNivel = false;

    @Column(nullable = false)
    private Boolean activo = true;

    @Column(name = "imagen_url", length = 500)
    private String imagenUrl;

    @OneToMany(mappedBy = "actividad")
    private List<Reserva> reservas = new ArrayList<>();

    public Long getIdActividad() {
        return idActividad;
    }

    public void setIdActividad(Long idActividad) {
        this.idActividad = idActividad;
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

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public Integer getDuracionMinutos() {
        return duracionMinutos;
    }

    public void setDuracionMinutos(Integer duracionMinutos) {
        this.duracionMinutos = duracionMinutos;
    }

    public Integer getPlazasMax() {
        return plazasMax;
    }

    public void setPlazasMax(Integer plazasMax) {
        this.plazasMax = plazasMax;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public Boolean getRequiereNivel() {
        return requiereNivel;
    }

    public void setRequiereNivel(Boolean requiereNivel) {
        this.requiereNivel = requiereNivel;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public String getImagenUrl() { return imagenUrl; }
    public void setImagenUrl(String imagenUrl) { this.imagenUrl = imagenUrl; }

    public int getplazasOcupadas () {return plazasOcupadas;}
    public void setplazasOcupadas (int plazasOcupadas){this.plazasOcupadas = plazasOcupadas;}

public LocalDate getFechaActividad () {return fechaActividad;}
    public void setFechaActividad (LocalDate fechaActividad){this.fechaActividad = fechaActividad;}
}