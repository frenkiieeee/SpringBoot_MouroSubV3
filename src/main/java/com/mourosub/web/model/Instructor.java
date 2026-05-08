package com.mourosub.web.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "instructores")
public class Instructor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idInstructor;

    private String nombre;
    private String dni;
    private String certificaciones;

    @OneToMany(mappedBy = "instructor", cascade = CascadeType.ALL)
    private List<Reserva> reservas;

    public Instructor() {}

    public Instructor(String nombre, String dni, String certificaciones) {
        this.nombre = nombre;
        this.dni = dni;
        this.certificaciones = certificaciones;
    }

    public Long getIdInstructor() { return idInstructor; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }
    public String getCertificaciones() { return certificaciones; }
    public void setCertificaciones(String certificaciones) { this.certificaciones = certificaciones; }
    public List<Reserva> getReservas() { return reservas; }
    public void setReservas(List<Reserva> reservas) { this.reservas = reservas; }
}