package com.mourosub.web.model;
import java.math.BigDecimal;
import java.util.*;
import java.util.ArrayList;

import jakarta.persistence.*;

@Entity
@Table (name = "material")
public class Material {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long idMaterial;

    @Column (nullable = false, length = 150)
    private String nombre;

    @Column (nullable = false, length = 100)
    private String categoria;

    @Column(nullable = false, length = 50)
    private String modelo;

    @Column (name = "estado_conservacion", nullable = false, length = 100)
    private String estadoConservacion;

    @Column (name = "num_serie", nullable = false, length = 50)
    private String numSerie;

    @Column(nullable = false)
    private BigDecimal precioBase;

    @Column(nullable = false)
    private BigDecimal precioPorDia;

    @Column (nullable = false, length = 50)
    private String marca;
    @OneToMany(mappedBy = "material", fetch = FetchType.LAZY)
    private List<Alquileres> alquileres = new ArrayList<>();

    public List<Alquileres> getAlquileres() { return alquileres; }
    public void setAlquileres(List<Alquileres> alquileres) { this.alquileres = alquileres; }


    //Getter y Setter
    public Long getIdMaterial() { return idMaterial;}
    public void setIdMaterial(Long idMaterial) {this.idMaterial = idMaterial;}

    public String getNombre() {return nombre;}
    public void setNombre(String nombre) {this.nombre = nombre;}

    public String getCategoria() {return categoria;}
    public void setCategoria(String categoria) {this.categoria = categoria;}

    public String getModelo() {return modelo;}
    public void setModelo(String modelo) {this.modelo = modelo;}

    public String getEstadoConservacion() {return estadoConservacion;}
    public void setEstadoConservacion(String estadoConservacion) {this.estadoConservacion = estadoConservacion;}

    public String getNumSerie() {return numSerie;}
    public void setNumSerie(String numSerie) {this.numSerie = numSerie;}

    public String getMarca() {return marca;}
    public void setMarca(String marca) {this.marca = marca;}

    public BigDecimal getPrecioBase() {
        return precioBase;
    }

    public void setPrecioBase(BigDecimal precioBase) {
        this.precioBase = precioBase;
    }

    public BigDecimal getPrecioPorDia() {
        return precioPorDia;
    }

    public void setPrecioPorDia(BigDecimal precioPorDia) {
        this.precioPorDia = precioPorDia;
    }
}