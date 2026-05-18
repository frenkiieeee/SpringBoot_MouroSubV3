package com.mourosub.web.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

// @Entity: esta clase es una entidad JPA; cada objeto de esta clase = una fila de la tabla.
@Entity
// @Table: nombre real de la tabla en la base de datos.
@Table(name = "servicio_tecnico")
public class ServicioTecnico {

    // Clave primaria (id). @GeneratedValue IDENTITY: la base de datos genera el id automaticamente.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idServicioTecnico;

    // Titulo del servicio. nullable=false -> obligatorio. length=150 -> maximo de caracteres.
    @Column(nullable = false, length = 150)
    private String titulo;

    // Descripcion larga. columnDefinition="TEXT" -> tipo TEXT en la BD (sin limite practico).
    @Column(columnDefinition = "TEXT")
    private String descripcion;

    // Categoria del servicio (campo opcional).
    @Column(length = 80)
    private String categoria;

    // Indica si el servicio esta activo (visible para el cliente). Por defecto true.
    @Column(nullable = false)
    private Boolean activo = true;

    // Orden de aparicion en los listados. Por defecto 0.
    @Column(nullable = false)
    private Integer orden = 0;

    // RELACION CON LOS TICKETS (uno a muchos).
    // Un servicio tecnico puede tener muchos tickets asociados. mappedBy="servicioTecnico"
    // indica que la clave foranea (columna id_servicio_tecnico) vive en TicketServicioTecnico:
    // es ESE lado el dueño de la relacion. Aqui solo navegamos la lista; NO se crea ninguna
    // columna nueva en servicio_tecnico, asi la referencia va en un unico sentido:
    // ticket -> servicio (el ticket apunta al servicio, no al reves).
    @OneToMany(mappedBy = "servicioTecnico", fetch = FetchType.LAZY)
    private List<TicketServicioTecnico> tickets = new ArrayList<>();

    // Getters y setters: JPA y Thymeleaf los usan para leer y escribir cada campo.
    public Long getIdServicioTecnico() {
        return idServicioTecnico;
    }

    public void setIdServicioTecnico(Long idServicioTecnico) {
        this.idServicioTecnico = idServicioTecnico;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public Integer getOrden() {
        return orden;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }

    public List<TicketServicioTecnico> getTickets() {
        return tickets;
    }

    public void setTickets(List<TicketServicioTecnico> tickets) {
        this.tickets = tickets;
    }
}
