package com.mourosub.web.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

// le decimos a spring que esta clase es una tabla de la base de datos
@Entity
// indicamos el nombre exacto de la tabla en postgres
@Table(name = "actividades")
public class Actividad {

    // marcamos este campo como la clave primaria
    @Id
    // esto es el equivalente al serial o auto increment
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idActividad;

    // es una columna obligatoria y de maximo 150 caracteres
    @Column(nullable = false, length = 150)
    private String nombre;

    // lo definimos como text para poder escribir parrafos largos
    @Column(columnDefinition = "TEXT")
    private String descripcion;

    // columna para guardar si es infantil o de otro tipo
    @Column(nullable = false, length = 50)
    private String tipo;

    // si no ponemos anotacion spring lo crea como un numero normal
    private Integer duracionMinutos;

    // establecemos que por defecto el limite son 10 plazas
    @Column(nullable = false)
    private Integer plazasMax = 10;

    // bigdecimal es la mejor clase de java para manejar dinero
    @Column(nullable = false)
    private BigDecimal precio;

    // un booleano para el true o false de si exige nivel
    @Column(nullable = false)
    private Boolean requiereNivel = false;

    // sirve para dar de baja la actividad sin borrarla del todo
    @Column(nullable = false)
    private Boolean activo = true;

    // getter y setters


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


}