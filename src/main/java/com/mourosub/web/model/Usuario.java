package com.mourosub.web.model;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

// entity le dice a spring que esta clase es una tabla de la base de datos
@Entity
// con table le decimos el nombre exacto de la tabla en sql
@Table(name = "usuarios")
public class Usuario {
    
    // mappedby significa que la configuracion principal de esta relacion la lleva la clase reserva
    @ManyToMany(mappedBy = "usuarios")
    private List<Reserva> reservas = new ArrayList<>();
    
    // id indica que esta es la clave primaria o primary key
    // generatedvalue hace que la base de datos genere el id automaticamente sin que lo toquemos
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUsuario;
    
    // este id es el que nos da supabase cuando el usuario se registra alli
    // unique a true significa que no puede haber dos iguales y es nuestro puente con supabase
    @Column(name = "supabase_user_id", unique = true, nullable = false)
    private UUID supabaseUserId;
    
    // el resto son columnas normales y les ponemos limite de caracteres para no petar la bd
    @Column(length = 100)
    private String nombre;
    @Column(length = 150)
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

    // esta columna es para saber si es admin y por defecto es false para que nadie se cuele
    @Column(name = "is_admin", nullable = false)
    private boolean isAdmin = false;

    // relacion de 1 a muchos porque un usuario puede tener muchisimos alquileres
    @OneToMany(mappedBy = "usuario", fetch = FetchType.LAZY)
    private List<Alquileres> alquileres = new ArrayList<>();

    // jpa necesita un constructor vacio si o si para poder crear el objeto por debajo
    public Usuario() {}

    // a partir de aqui solo hay getters y setters para leer y modificar las variables desde fuera
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
    public boolean isAdmin() { return isAdmin; }
    public void setAdmin(boolean isAdmin) { this.isAdmin = isAdmin; }
    public List<Reserva> getReservas() { return reservas; }
    public void setReservas(List<Reserva> reservas) { this.reservas = reservas; }
    public List<Alquileres> getAlquileres() { return alquileres; }
    public void setAlquileres(List<Alquileres> alquileres) { this.alquileres = alquileres; }
}