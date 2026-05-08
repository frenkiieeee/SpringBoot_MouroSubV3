package com.mourosub.web.model;

import jakarta.persistence.*;

// convertimos la clase en una entidad de jpa
@Entity
// la conectamos con su tabla correspondiente
@Table(name = "inmersiones")
public class Inmersiones {

    // identificador principal
    @Id
    // autoincremental
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idInmersion;

    // el nombre del punto de buceo es obligatorio
    @Column(nullable = false, length = 150)
    private String nombre;

    // getters y setters


    public Long getIdInmersion() {
        return idInmersion;
    }

    public void setIdInmersion(Long idInmersion) {
        this.idInmersion = idInmersion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}