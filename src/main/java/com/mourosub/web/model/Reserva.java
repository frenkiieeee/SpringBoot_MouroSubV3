package com.mourosub.web.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "reservas")
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idReserva;

    private String tipoServicio;
    private LocalDate fechaReserva;
    private String estado;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    public Reserva() {}

    public Reserva(String tipoServicio, String estado, Usuario usuario) {
        this.tipoServicio = tipoServicio;
        this.fechaReserva = LocalDate.now();
        this.estado = estado;
        this.usuario = usuario;
    }

    // Getters y Setters
    public Long getIdReserva() { return idReserva; }
    public String getTipoServicio() { return tipoServicio; }
    public void setTipoServicio(String tipoServicio) { this.tipoServicio = tipoServicio; }
    public LocalDate getFechaReserva() { return fechaReserva; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
}
