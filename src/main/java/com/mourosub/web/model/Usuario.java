package com.mourosub.web.model;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

// Entidad que representa a un usuario registrado en la aplicacion.
@Entity
@Table(name = "usuarios")

public class Usuario {
    // @ManyToMany: un usuario puede estar en muchas reservas y una reserva tener muchos usuarios.
    // mappedBy="usuarios": el lado "dueño" de la relacion esta en la clase Reserva.
    @ManyToMany(mappedBy = "usuarios")
    private List<Reserva> reservas = new ArrayList<>();
    // Clave primaria; la genera la base de datos automaticamente.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUsuario;
    // Id del usuario en Supabase. unique=true: no se puede repetir. Es el enlace con el login.
    @Column(name = "supabase_user_id", unique = true, nullable = false)
    private UUID supabaseUserId;
    // Datos personales. nombre y apellidos son obligatorios (nullable=false).
    @Column(nullable = false, length = 100)
    private String nombre;
    @Column(nullable = false, length = 150)
    private String apellidos;
    @Column(length = 255)
    private String email;
    @Column(length = 20)
    private String dni;
    @Column(length = 20)
    private String telefono;
    @Column(length = 255)
    private String direccion;
    @Column(length = 10)
    private String codPostal;
    @Column(length = 100)
    private String localidad;
    @Column
    private LocalDate fechaNacimiento;

    // Marca si el usuario es administrador. Columna 'is_admin'. Por defecto false.
    @Column(name = "is_admin", nullable = false)
    private boolean isAdmin = false;

    // Un usuario puede tener varios alquileres asociados.
    @OneToMany(mappedBy = "usuario", fetch = FetchType.LAZY)
    private List<Alquileres> alquileres = new ArrayList<>();

    // Constructor vacio: JPA lo necesita para poder crear los objetos.
    public Usuario() {}

    // Getters y setters: JPA y Thymeleaf los usan para leer y escribir cada campo.
    // Nota: el nombre estandar de Java seria getIdUsuario / setIdUsuario (con I mayuscula).
    public Long getidUsuario() { return idUsuario; }
    public void setidUsuario(Long idUsuario) { this.idUsuario = idUsuario; }

    public UUID getSupabaseUserId() { return supabaseUserId; }
    public void setSupabaseUserId(UUID supabaseUserId) { this.supabaseUserId = supabaseUserId; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getCodPostal() { return codPostal; }
    public void setCodPostal(String codPostal) { this.codPostal = codPostal; }

    public String getLocalidad() { return localidad; }
    public void setLocalidad(String localidad) { this.localidad = localidad; }

    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }

    // isAdmin(): al ser boolean, el getter empieza por 'is' en vez de 'get'.
    public boolean isAdmin() { return isAdmin; }
    public void setAdmin(boolean isAdmin) { this.isAdmin = isAdmin; }

    public List<Reserva> getReservas() { return reservas; }
    public void setReservas(List<Reserva> reservas) { this.reservas = reservas; }

    public List<Alquileres> getAlquileres() { return alquileres; }
    public void setAlquileres(List<Alquileres> alquileres) { this.alquileres = alquileres; }
}
