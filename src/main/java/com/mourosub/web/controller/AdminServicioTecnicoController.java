package com.mourosub.web.controller;

import com.mourosub.web.model.ServicioTecnico;
import com.mourosub.web.service.ServicioTecnicoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

// Controlador del CRUD de servicios tecnicos dentro del panel admin
@Controller
// @RequestMapping a nivel de clase: todas las rutas de aqui empiezan por /admin/servicios/tecnico
@RequestMapping("/admin/servicios/tecnico")
public class AdminServicioTecnicoController {

    // Servicio que contiene la logica de negocio de los servicios tecnicos
    private final ServicioTecnicoService servicioTecnicoService;

    // Spring inyecta el servicio por el constructor.
    public AdminServicioTecnicoController(ServicioTecnicoService servicioTecnicoService) {
        this.servicioTecnicoService = servicioTecnicoService;
    }

    // GET /admin/servicios/tecnico -> tabla con todos los servicios tecnicos
    @GetMapping
    public String listar(Model model) {
        // Metemos la lista completa en el modelo para que la pinte la plantilla.
        model.addAttribute("servicios", servicioTecnicoService.listarTodos());
        return "admin/servicios/tecnico/lista";
    }

    // GET /admin/servicios/tecnico/nuevo -> formulario vacio para crear uno nuevo.
    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        // Pasamos un objeto vacio: el formulario lo rellenara el administrador.
        model.addAttribute("servicio", new ServicioTecnico());
        return "admin/servicios/tecnico/formulario";
    }

    // GET /admin/servicios/tecnico/editar/{id} -> el mismo formulario pero con los datos ya cargados.
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        // @PathVariable lee el id de la URL; buscamos ese servicio y lo mandamos al formulario.
        model.addAttribute("servicio", servicioTecnicoService.buscarPorId(id));
        return "admin/servicios/tecnico/formulario";
    }

    // POST /admin/servicios/tecnico/guardar -> recibe el formulario (crear o editar) y lo guarda.
    @PostMapping("/guardar")
    public String guardar(@ModelAttribute("servicio") ServicioTecnico servicioTecnico) {
        // @ModelAttribute construye el objeto ServicioTecnico con los campos enviados en el formulario.
        servicioTecnicoService.guardar(servicioTecnico);
        // Redirige a la lista. Es el patron POST-Redirect-GET: evita reenviar el formulario al refrescar.
        return "redirect:/admin/servicios/tecnico";
    }

    // POST /admin/servicios/tecnico/eliminar/{id} -> borra el servicio indicado.
    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        servicioTecnicoService.eliminar(id);
        return "redirect:/admin/servicios/tecnico";
    }
}
