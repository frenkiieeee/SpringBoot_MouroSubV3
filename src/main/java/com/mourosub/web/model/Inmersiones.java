package com.mourosub.web.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "inmersiones")
public class Inmersiones {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idInmersion;

    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(nullable = false, length = 50)
    private String tipo;

    @Column(length = 50)
    private String ubicacion;

    @Column(length = 50)
    private String nivelReq;

    @Column(length = 50)
    private String dificultad;

    @Column(name = "profundidad_max")
    private Integer profundidadMax;

    @Column
    private Integer visibilidad;

    @Column(length = 50)
    private String corriente;

    @Column(name = "temperatura_agua", length = 50)
    private String temperaturaAgua;

    @Column(name = "equipamiento_incluido", length = 500)
    private String equipamientoIncluido;

    @Column(columnDefinition = "TEXT")
    private String requisitos;

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
    private Boolean activo = true;

    @Column(name = "imagen_url", length = 500)
    private String imagenUrl;

    public Long getIdInmersion() { return idInmersion; }
    public void setIdInmersion(Long idInmersion) { this.idInmersion = idInmersion; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getUbicacion() { return ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }

    public String getNivelReq() { return nivelReq; }
    public void setNivelReq(String nivelReq) { this.nivelReq = nivelReq; }

    public String getDificultad() { return dificultad; }
    public void setDificultad(String dificultad) { this.dificultad = dificultad; }

    public Integer getProfundidadMax() { return profundidadMax; }
    public void setProfundidadMax(Integer profundidadMax) { this.profundidadMax = profundidadMax; }

    public Integer getVisibilidad() { return visibilidad; }
    public void setVisibilidad(Integer visibilidad) { this.visibilidad = visibilidad; }

    public String getCorriente() { return corriente; }
    public void setCorriente(String corriente) { this.corriente = corriente; }

    public String getTemperaturaAgua() { return temperaturaAgua; }
    public void setTemperaturaAgua(String temperaturaAgua) { this.temperaturaAgua = temperaturaAgua; }

    public String getEquipamientoIncluido() { return equipamientoIncluido; }
    public void setEquipamientoIncluido(String equipamientoIncluido) { this.equipamientoIncluido = equipamientoIncluido; }

    public String getRequisitos() { return requisitos; }
    public void setRequisitos(String requisitos) { this.requisitos = requisitos; }

    public Integer getDuracionMinutos() { return duracionMinutos; }
    public void setDuracionMinutos(Integer duracionMinutos) { this.duracionMinutos = duracionMinutos; }

    public Integer getPlazasMax() { return plazasMax; }
    public void setPlazasMax(Integer plazasMax) { this.plazasMax = plazasMax; }

    public int getPlazasOcupadas() { return plazasOcupadas; }
    public void setPlazasOcupadas(int plazasOcupadas) { this.plazasOcupadas = plazasOcupadas; }

    public BigDecimal getPrecio() { return precio; }
    public void setPrecio(BigDecimal precio) { this.precio = precio; }

    public LocalDate getFechaActividad() { return fechaActividad; }
    public void setFechaActividad(LocalDate fechaActividad) { this.fechaActividad = fechaActividad; }

    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }

    public String getImagenUrl() { return imagenUrl; }
    public void setImagenUrl(String imagenUrl) { this.imagenUrl = imagenUrl; }
}