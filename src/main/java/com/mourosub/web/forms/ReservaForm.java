package com.mourosub.web.forms;

import java.time.LocalDate;
import java.util.List;

public class ReservaForm {

    private String tipoServicio;
    private String refServicio;
    private String refServicioId;
    private Integer numParticipantes;
    private LocalDate fechaActividad;
    private Long idActividad;
    private List<Long> usuarioIds;

    public String getTipoServicio() {
        return tipoServicio;
    }

    public void setTipoServicio(String tipoServicio) {
        this.tipoServicio = tipoServicio;
    }

    public String getRefServicio() {
        return refServicio;
    }

    public void setRefServicio(String refServicio) {
        this.refServicio = refServicio;
    }

    public String getRefServicioId() {
        return refServicioId;
    }

    public void setRefServicioId(String refServicioId) {
        this.refServicioId = refServicioId;
    }

    public Integer getNumParticipantes() {
        return numParticipantes;
    }

    public void setNumParticipantes(Integer numParticipantes) {
        this.numParticipantes = numParticipantes;
    }

    public LocalDate getFechaActividad() {
        return fechaActividad;
    }

    public void setFechaActividad(LocalDate fechaActividad) {
        this.fechaActividad = fechaActividad;
    }

    public Long getIdActividad() {
        return idActividad;
    }

    public void setIdActividad(Long idActividad) {
        this.idActividad = idActividad;
    }

    public List<Long> getUsuarioIds() {
        return usuarioIds;
    }

    public void setUsuarioIds(List<Long> usuarioIds) {
        this.usuarioIds = usuarioIds;
    }
}