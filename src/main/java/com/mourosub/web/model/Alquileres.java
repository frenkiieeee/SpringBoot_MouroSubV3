package com.mourosub.web.model;

import java.time.LocalDate;
import jakarta.persistence.*;


@Entity
@Table (name = "Alquileres")

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

    public Long getIdAlquiler() {
        return idAlquiler;
    }

    public void setIdAlquiler(Long idAlquiler) {
        this.idAlquiler = idAlquiler;
    }

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
}