package com.mourosub.web.model;

import java.time.LocalDate;
import jakarta.persistence.*;


@Entity
@Table (name = "alquileres")

public class Alquileres {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long idAlquiler;

    @Column (nullable = false)
    private Double precioTotal;

    @Column (nullable = false)
    private LocalDate fechaAlquiler;
    private LocalDate fechaDevolucion;

    @Column (nullable = false)
    private Boolean disponible = true;
     // RELACIÓN CON MATERIAL
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_material")
    private Material material;

    // RELACIÓN CON SEGURO
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_seguro")
    private Seguros seguro;

    // RELACIÓN CON USUARIO
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    //Getters y setters
    public Long getIdAlquiler() {return idAlquiler;}
    public void setIdAlquiler(Long idAlquiler) {this.idAlquiler = idAlquiler;}

    public Double getPrecioTotal() {
        return precioTotal;
    }

    public void setPrecioTotal(Double precioTotal) {
        this.precioTotal = precioTotal;
    }

    public LocalDate getFechaAlquiler() {
        return fechaAlquiler;
    }

    public void setFechaAlquiler(LocalDate fechaAlquiler) {
        this.fechaAlquiler = fechaAlquiler;
    }

    public LocalDate getFechaDevolucion() {
        return fechaDevolucion;
    }

    public void setFechaDevolucion(LocalDate fechaDevolucion) {
        this.fechaDevolucion = fechaDevolucion;
    }

    public Boolean getDisponible() {
        return disponible;
    }

    public void setDisponible(Boolean disponible) {
        this.disponible = disponible;
    }
    public Material getMaterial() { return material; }
    public void setMaterial(Material material) { this.material = material; }

    public Seguros getSeguro() { return seguro; }
    public void setSeguro(Seguros seguro) { this.seguro = seguro; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
}
