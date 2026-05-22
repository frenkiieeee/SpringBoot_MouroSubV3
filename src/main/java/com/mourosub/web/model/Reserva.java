package com.mourosub.web.model;

import java.util.*;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

// le decimos a spring que esto es una tabla que se llama reservas en la base de datos
@Entity
@Table(name = "reservas")
public class Reserva {

    // clave primaria autoincremental para que cada reserva tenga un numero unico
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idReserva;

    // guardamos el tipo de servicio que es y no dejamos que este en blanco
    @Column(nullable = false, length = 50)
    private String tipoServicio;

    // el nombre de la actividad que han reservado
    @Column(length = 100)
    private String refServicio;

    // guardamos tambien el id del servicio por si necesitamos buscarlo luego
    @Column(length = 100)
    private String refServicioId;

    // cuanta gente va a ir a la actividad
    @Column(nullable = false)
    private Integer numParticipantes;

    // bigdecimal se usa siempre para dinero porque no pierde decimales como los float
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precioTotal;

    // para saber si esta activa o cancelada
    @Column(length = 20)
    private String estado;

    // la fecha en la que el usuario hizo click en comprar
    @Column(nullable = false)
    private LocalDate fechaReserva;

    // el dia que realmente se van a tirar al agua
    @Column(nullable = false)
    private LocalDate fechaActividad;

    // muchos a muchos porque una reserva tiene usuarios y un usuario tiene reservas
    // jointable crea una tabla intermedia en sql para unir ids de reservas con ids de usuarios
    @ManyToMany
    @JoinTable(
            name = "usuario_reserva",
            joinColumns = @JoinColumn(name = "id_reserva"),
            inverseJoinColumns = @JoinColumn(name = "id_usuario")
    )
    private List<Usuario> usuarios = new ArrayList<>();

    // lo mismo de arriba pero uniendo la reserva con los instructores que dan la clase
    @ManyToMany
    @JoinTable(
            name = "instructor_reserva",
            joinColumns = @JoinColumn(name = "id_reserva"),
            inverseJoinColumns = @JoinColumn(name = "id_instructor")
    )
    private List<Instructor> instructores = new ArrayList<>();

    // muchos a uno porque muchas reservas pueden apuntar a la misma actividad de las 10 de la manana
    @ManyToOne
    @JoinColumn(name = "id_actividad")
    private Actividad actividad;

    // el constructor vacio que exige jpa para funcionar por debajo
    public Reserva() {}

    // constructor con parametros para crear reservas del tiron pasandole los datos
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

    // getters y setters para poder leer y escribir las variables
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