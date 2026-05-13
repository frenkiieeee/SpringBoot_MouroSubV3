package com.mourosub.web.model;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "usuarios")

public class Usuario {
    @ManyToMany
    @JoinTable(
        name = "usuario_reserva",
        joinColumns = @JoinColumn(name = "id_usuario"),
        inverseJoinColumns = @JoinColumn(name = "id_reserva")
    )
    private List<Reserva> reservas = new ArrayList<>();
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long IdUsuario;
    @Column(name = "supabase_user_id", unique = true, nullable = false)
    private UUID supabaseUserId;
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

    
    @OneToMany(mappedBy = "usuario", fetch = FetchType.LAZY)
    private List<Alquileres> alquileres = new ArrayList<>();

    public Usuario() {}
    public Long getidUsuario() { return IdUsuario; }
    public void setidUsuario(Long IdUsuario) {this.IdUsuario = IdUsuario; }
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
    public List<Reserva> getReservas() { return reservas; }
    public void setReservas(List<Reserva> reservas) { this.reservas = reservas; }
    public List<Alquileres> getAlquileres() { return alquileres; }
    public void setAlquileres(List<Alquileres> alquileres) { this.alquileres = alquileres; }
}
