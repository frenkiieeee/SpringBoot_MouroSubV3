package com.mourosub.web.model;
import java.util.*;
import java.util.ArrayList;

import jakarta.persistence.*;

@Entity
@Table (name = "seguro")

public class Seguros {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idSeguro;

    @Column (nullable = false)
    private Boolean activo = false;

    @Column (nullable = false, length = 100)
    private String nombre;

    @Column (nullable = false, length = 100)
    private String compania;

    @Column (name = "cobertura_gastos", nullable = false)
    private Double coberturaGastos;

    @Column (columnDefinition = "TEXT")
    private String descripcion;
    //FetchType.LAZY, cachea la informacion que no se esta solicitando en el momento, por lo que no muestra todo al instante, esto ayuda al redimiento de la BD
    @OneToMany(mappedBy = "seguro", fetch = FetchType.LAZY)
    private List<Alquileres> alquileres = new ArrayList<>();

    public List<Alquileres> getAlquileres() { return alquileres; }
    public void setAlquileres(List<Alquileres> alquileres) { this.alquileres = alquileres; }


    public Long getIdSeguro() {return idSeguro;}
    public void setIdSeguro(Long idSeguro) {this.idSeguro = idSeguro;}

    public Boolean getActivo() {return activo;}
    public void setActivo(Boolean activo) { this.activo = activo;}

    public String getNombre() { return nombre;}
    public void setNombre(String nombre) {this.nombre = nombre;}

    public String getCompania() { return compania;}
    public void setCompania(String compania) {this.compania = compania;}

    public Double getCoberturaGastos() {return coberturaGastos;}
    public void setCoberturaGastos(Double coberturaGastos) {this.coberturaGastos = coberturaGastos;}

    public String getDescripcion() {return descripcion;}
    public void setDescripcion(String descripcion) {this.descripcion = descripcion;}
}

