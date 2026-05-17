package com.mourosub.web.service;

import com.mourosub.web.model.ServicioTecnico;
import com.mourosub.web.repository.ServicioTecnicoRepository;
import org.springframework.stereotype.Service;
import java.util.List;

// @Service: clase con la logica de negocio de los servicios tecnicos.
// Los controladores la llaman a ella, y ella habla con el repositorio.
@Service
public class ServicioTecnicoService {

    // Repositorio para acceder a la tabla de servicios tecnicos.
    private final ServicioTecnicoRepository servicioTecnicoRepository;

    // Spring inyecta el repositorio por el constructor.
    public ServicioTecnicoService(ServicioTecnicoRepository servicioTecnicoRepository) {
        this.servicioTecnicoRepository = servicioTecnicoRepository;
    }

    // Devuelve solo los servicios activos (los que ve el cliente).
    public List<ServicioTecnico> listarActivos() {
        return servicioTecnicoRepository.findByActivoTrueOrderByOrdenAscTituloAsc();
    }

    // Devuelve todos los servicios (los que ve el admin, activos o no).
    public List<ServicioTecnico> listarTodos() {
        return servicioTecnicoRepository.findAllByOrderByOrdenAscTituloAsc();
    }

    // Guarda un servicio (crea uno nuevo o actualiza uno existente).
    public ServicioTecnico guardar(ServicioTecnico servicioTecnico) {
        // Si no viene el orden, lo ponemos a 0 para evitar un valor nulo.
        if (servicioTecnico.getOrden() == null) {
            servicioTecnico.setOrden(0);
        }
        // Si no viene el campo activo, lo dejamos activo por defecto.
        if (servicioTecnico.getActivo() == null) {
            servicioTecnico.setActivo(true);
        }
        return servicioTecnicoRepository.save(servicioTecnico);
    }

    // Busca un servicio por su id. Si no existe, lanza una excepcion.
    public ServicioTecnico buscarPorId(Long id) {
        return servicioTecnicoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Servicio tecnico no encontrado: " + id));
    }

    // Elimina un servicio por su id.
    public void eliminar(Long id) {
        servicioTecnicoRepository.deleteById(id);
    }
}
