package com.mourosub.web.model;
import java.util.*;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "instructores")
public class Instructor {
    @ManyToMany(mappedBy = "instructores")
    private List<Reserva> reservas = new ArrayList <>();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_instructor")
    private Long IdInstructor;
    @Column(nullable = false, length = 150)
    private String nombre;
    @Column(length = 20)
    private String dni;
    @Column(length = 255)
    private String certificaciones;



    public Instructor() {}

    public Instructor(String nombre, String dni, String certificaciones) {
        this.nombre = nombre;
        this.dni = dni;
        this.certificaciones = certificaciones;
    }

    public Long getidInstructor() { return IdInstructor; }
    public void setidInstructor(Long IdInstructor){this.IdInstructor = IdInstructor;}
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }
    public String getCertificaciones() { return certificaciones; }
    public void setCertificaciones(String certificaciones) { this.certificaciones = certificaciones; }
    public List<Reserva> getReservas() { return reservas; }
    public void setReservas(List<Reserva> reservas) { this.reservas = reservas; }
}
