package com.mourosub.web.repository;

import com.mourosub.web.model.ServicioTecnico;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

// Repositorio de servicios tecnicos.
// Al extender JpaRepository ya tenemos gratis: save, findById, findAll, deleteById, etc.
// Spring Data crea la implementacion solo; nosotros solo declaramos los metodos.
public interface ServicioTecnicoRepository extends JpaRepository<ServicioTecnico, Long> {

    // Spring genera la consulta a partir del NOMBRE del metodo (query derivada):
    // "ByActivoTrue" -> WHERE activo = true ; "OrderByOrdenAscTituloAsc" -> ordenar por orden y titulo.
    // Devuelve solo los servicios activos, ordenados.
    List<ServicioTecnico> findByActivoTrueOrderByOrdenAscTituloAsc();

    // Igual que el anterior pero sin filtro: todos los servicios, ordenados por orden y titulo.
    List<ServicioTecnico> findAllByOrderByOrdenAscTituloAsc();
}
