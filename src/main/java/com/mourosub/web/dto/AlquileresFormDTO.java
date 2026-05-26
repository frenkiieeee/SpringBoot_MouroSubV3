package com.mourosub.web.dto;
import java.time.*;
//Añadir una targeta de red nueva, añdir una ip nueva nat y hacer un puente ( buscar una solucion), ajustar el DTP
public class AlquileresFormDTO {
    private Long productoId;
    private Long servicioId;
    private LocalDate fechaInicio;
    private LocalDate fechaDevolucion;
    private Long materialId;
    private Long usuarioId;

    public Long getProductoId() {
        return productoId;
    }

    public void setProductoId(Long productoId) {
        this.productoId = productoId;
    }

    public Long getServicioId() {
        return servicioId;
    }

    public void setServicioId(Long servicioId) {
        this.servicioId = servicioId;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaDevolucion() {
        return fechaDevolucion;
    }

    public void setFechaDevolucion(LocalDate fechaDevolucion) {
        this.fechaDevolucion = fechaDevolucion;
    }
    public Long getMaterialId() {return materialId;}
    public void setMaterialId(Long materialId) {this.materialId = materialId;}

    public Long getUsuarioId() {return usuarioId;}
    public void setUsuarioId(Long usuarioId) {this.usuarioId = usuarioId;}
}
