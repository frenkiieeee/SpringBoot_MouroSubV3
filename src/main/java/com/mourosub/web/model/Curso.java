package com.mourosub.web.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "cursos")
public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCurso;

    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(nullable = false, length = 50)
    private String tipo;

    @Column(length = 50)
    private String categoria;

    @Column(length = 50)
    private String nivelRequerido = "ninguno";

    private Integer duracionHoras;

    private Integer duracionMinutos;

    @Column(length = 500)
    private String temario;

    @Column(columnDefinition = "TEXT")
    private String incluye;

    @Column(name = "imagen_url", length = 500)
    private String imagenUrl;

    @Column(name = "certificacion_incluida", length = 100)
    private String certificacionIncluida;

    @Column(name = "num_modulos")
    private Integer numModulos;

    private Boolean teoriaOnline = false;

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

    public Long getIdCurso() { return idCurso; }
    public void setIdCurso(Long idCurso) { this.idCurso = idCurso; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public String getNivelRequerido() { return nivelRequerido; }
    public void setNivelRequerido(String nivelRequerido) { this.nivelRequerido = nivelRequerido; }

    public Integer getDuracionHoras() { return duracionHoras; }
    public void setDuracionHoras(Integer duracionHoras) { this.duracionHoras = duracionHoras; }

    public Integer getDuracionMinutos() { return duracionMinutos; }
    public void setDuracionMinutos(Integer duracionMinutos) { this.duracionMinutos = duracionMinutos; }

    public String getTemario() { return temario; }
    public void setTemario(String temario) { this.temario = temario; }

    public String getIncluye() { return incluye; }
    public void setIncluye(String incluye) { this.incluye = incluye; }

    public String getImagenUrl() { return imagenUrl; }
    public void setImagenUrl(String imagenUrl) { this.imagenUrl = imagenUrl; }

    public String getCertificacionIncluida() { return certificacionIncluida; }
    public void setCertificacionIncluida(String certificacionIncluida) { this.certificacionIncluida = certificacionIncluida; }

    public Integer getNumModulos() { return numModulos; }
    public void setNumModulos(Integer numModulos) { this.numModulos = numModulos; }

    public Boolean getTeoriaOnline() { return teoriaOnline; }
    public void setTeoriaOnline(Boolean teoriaOnline) { this.teoriaOnline = teoriaOnline; }

    public Integer getPlazasMax() { return plazasMax; }
    public void setPlazasMax(Integer plazasMax) { this.plazasMax = plazasMax; }

    public int getplazasOcupadas() { return plazasOcupadas; }
    public void setplazasOcupadas(int plazasOcupadas) { this.plazasOcupadas = plazasOcupadas; }

    public BigDecimal getPrecio() { return precio; }
    public void setPrecio(BigDecimal precio) { this.precio = precio; }

    public LocalDate getFechaActividad() { return fechaActividad; }
    public void setFechaActividad(LocalDate fechaActividad) { this.fechaActividad = fechaActividad; }

    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }
}