package com.mourosub.web.dto;

public class SegurosFormDTO {
    private Long idSeguro;
    private String nombre;
    private String compania;
    private Double coberturaGastos;
    private String descripcion;
    private Boolean activo;

    public Long getIdSeguro() { return idSeguro; }
    public void setIdSeguro(Long idSeguro) { this.idSeguro = idSeguro; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getCompania() { return compania; }
    public void setCompania(String compania) { this.compania = compania; }

    public Double getCoberturaGastos() { return coberturaGastos; }
    public void setCoberturaGastos(Double coberturaGastos) { this.coberturaGastos = coberturaGastos; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }
}