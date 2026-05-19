package com.mourosub.web.model;

import java.util.*;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "reservas")
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idReserva;

    @Column(nullable = false, length = 50)
    private String tipoServicio;

    @Column(length = 100)
    private String refServicio;

    @Column(length = 100)
    private String refServicioId;

    @Column(nullable = false)
    private Integer numParticipantes;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precioTotal;

    @Column(length = 20)
    private String estado;

    @Column(nullable = false)
    private LocalDate fechaReserva;

    @Column(nullable = false)
    private LocalDate fechaActividad;

    @ManyToMany
    @JoinTable(
            name = "usuario_reserva",
            joinColumns = @JoinColumn(name = "id_reserva"),
            inverseJoinColumns = @JoinColumn(name = "id_usuario")
    )
    private List<Usuario> usuarios = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "instructor_reserva",
            joinColumns = @JoinColumn(name = "id_reserva"),
            inverseJoinColumns = @JoinColumn(name = "id_instructor")
    )
    private List<Instructor> instructores = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "id_actividad")
    private Actividad actividad;

    public Reserva() {}

    public Reserva(String tipoServicio, String refServicio, String refServicioId,
                   Integer numParticipantes, BigDecimal precioTotal, String estado,
                   LocalDate fechaActividad, Usuario usuario, Instructor instructor) {
        this.tipoServicio = tipoServicio;
        this.refServicio = refServicio;
        this.refServicioId = refServicioId;
        this.numParticipantes = numParticipantes;
        this.precioTotal = precioTotal;
        this.estado = estado;
        this.fechaReserva = LocalDate.now();
        this.fechaActividad = fechaActividad;
        this.usuarios.add(usuario);
        this.instructores.add(instructor);
    }

    public Long getIdReserva() { return idReserva; }

    public String getTipoServicio() { return tipoServicio; }
    public void setTipoServicio(String tipoServicio) { this.tipoServicio = tipoServicio; }

    public String getRefServicio() { return refServicio; }
    public void setRefServicio(String refServicio) { this.refServicio = refServicio; }

    public String getRefServicioId() { return refServicioId; }
    public void setRefServicioId(String refServicioId) { this.refServicioId = refServicioId; }

    public Integer getNumParticipantes() { return numParticipantes; }
    public void setNumParticipantes(Integer numParticipantes) { this.numParticipantes = numParticipantes; }

    public BigDecimal getPrecioTotal() { return precioTotal; }
    public void setPrecioTotal(BigDecimal precioTotal) { this.precioTotal = precioTotal; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public LocalDate getFechaReserva() { return fechaReserva; }
    public void setFechaReserva(LocalDate fechaReserva) { this.fechaReserva = fechaReserva; }

    public LocalDate getFechaActividad() { return fechaActividad; }
    public void setFechaActividad(LocalDate fechaActividad) { this.fechaActividad = fechaActividad; }

    public List<Usuario> getUsuarios() { return usuarios; }
    public void setUsuarios(List<Usuario> usuarios) { this.usuarios = usuarios; }

    public List<Instructor> getInstructores() { return instructores; }
    public void setInstructores(List<Instructor> instructores) { this.instructores = instructores; }

    public Actividad getActividad() { return actividad; }
    public void setActividad(Actividad actividad) { this.actividad = actividad; }
}