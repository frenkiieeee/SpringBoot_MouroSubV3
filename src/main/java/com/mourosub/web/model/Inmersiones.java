package com.mourosub.web.model;

import jakarta.persistence.*;


@Entity
@Table(name = "inmersiones")
@PrimaryKeyJoinColumn(name = "id_actividad") // Comparte PK con Actividad y agrega campos propios de inmersiones.
public class Inmersiones extends Actividad {

    @Column(length = 50)
    // Nivel requerido para la inmersion, si aplica.
    private String nivelReq;

    public String getNivelReq() {
        return nivelReq;
    }

    public void setNivelReq(String nivelReq) {
        this.nivelReq = nivelReq;
    }
}
