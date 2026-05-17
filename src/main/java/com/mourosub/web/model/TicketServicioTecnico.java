package com.mourosub.web.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// Entidad que representa un ticket (solicitud) de servicio tecnico enviado por un cliente.
@Entity
@Table(name = "ticket_servicio_tecnico")
public class TicketServicioTecnico {

    // Clave primaria; la genera la base de datos.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTicket;

    // Datos de contacto de quien hace la solicitud (nombre y email obligatorios).
    @Column(nullable = false, length = 120)
    private String nombre;

    @Column(nullable = false, length = 120)
    private String email;

    @Column(length = 30)
    private String telefono;

    // Datos del equipo a reparar/revisar (todos opcionales).
    @Column(length = 120)
    private String equipo;

    @Column(length = 120)
    private String marca;

    @Column(length = 120)
    private String modelo;

    // @Column(name=...): la columna en la BD se llama 'numero_serie' aunque el campo sea numeroSerie.
    @Column(name = "numero_serie", length = 120)
    private String numeroSerie;

    // Descripcion del problema. TEXT -> texto largo sin limite practico.
    @Column(columnDefinition = "TEXT")
    private String descripcionProblema;

    @Column(length = 80)
    private String categoria;

    // Estado del ticket. Empieza en "NUEVO" si no se indica otra cosa.
    @Column(nullable = false, length = 20)
    private String estado = "NUEVO";

    // Fecha y hora de creacion. Por defecto, el momento en que se crea el objeto.
    @Column(nullable = false)
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    // RELACIÓN CON USUARIO
    // @ManyToOne: muchos tickets pueden pertenecer a un mismo usuario.
    // @JoinColumn: la columna 'id_usuario' guarda a que usuario pertenece el ticket.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    // Relacion con el servicio tecnico al que corresponde este ticket.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_servicio_tecnico")
    private ServicioTecnico servicioTecnico;


    // Getters y setters: JPA y Thymeleaf los usan para leer y escribir cada campo.
    public Long getIdTicket() {
        return idTicket;
    }

    public void setIdTicket(Long idTicket) {
        this.idTicket = idTicket;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEquipo() {
        return equipo;
    }

    public void setEquipo(String equipo) {
        this.equipo = equipo;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getNumeroSerie() {
        return numeroSerie;
    }

    public void setNumeroSerie(String numeroSerie) {
        this.numeroSerie = numeroSerie;
    }

    public String getDescripcionProblema() {
        return descripcionProblema;
    }

    public void setDescripcionProblema(String descripcionProblema) {
        this.descripcionProblema = descripcionProblema;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public ServicioTecnico getServicioTecnico() {
        return servicioTecnico;
    }

    public void setServicioTecnico(ServicioTecnico servicioTecnico) {
        this.servicioTecnico = servicioTecnico;
    }

}
