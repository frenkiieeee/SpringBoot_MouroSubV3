package com.mourosub.web.controller;

import com.mourosub.web.model.Actividad;
import com.mourosub.web.model.Alquileres;
import com.mourosub.web.model.Seguros;
import com.mourosub.web.service.MaterialService;
import com.mourosub.web.service.SegurosService;
import com.mourosub.web.service.ServicioActividades;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/servicios")
public class ServiciosCatalogoController {

    private final MaterialService materialService;
    private final SegurosService segurosService;
    private final ServicioActividades servicioActividades;

    public ServiciosCatalogoController(MaterialService materialService, SegurosService segurosService, ServicioActividades servicioActividades) {
        this.materialService = materialService;
        this.segurosService = segurosService;
        this.servicioActividades = servicioActividades;
    }

    @GetMapping("/cursos")
    public String cursos() {
        return "servicios/cursos";
    }

    @GetMapping("/actividades")
    public String actividades(Model model) {
        List<Actividad> actividadesList = servicioActividades.listarTodos().stream()
                .filter(Actividad::getActivo)
                .toList();
        model.addAttribute("actividades", actividadesList);
        return "servicios/actividades";
    }

    @GetMapping("/alquiler")
    public String alquiler(Model model) {
        model.addAttribute("alquiler", new Alquileres());
        model.addAttribute("seguro", new Seguros());
        model.addAttribute("materiales", materialService.listarTodos());
        return "servicios/alquiler";
    }

    @GetMapping("/seguros")
    public String seguros(Model model) {
        List<Seguros> segurosList = new java.util.ArrayList<>();

        Seguros defaultSeguro = new Seguros();
        defaultSeguro.setIdSeguro(0L);
        defaultSeguro.setNombre("Seguro Anual");
        defaultSeguro.setCompania("MouroSub Seguros");
        defaultSeguro.setCoberturaGastos(120.00);
        defaultSeguro.setDescripcion("Cobertura básica anual para buceo. Incluye asistencia médica internacional, responsabilidad civil y gastos de rescate.");
        defaultSeguro.setActivo(true);
        segurosList.add(defaultSeguro);

        segurosService.listarTodos().stream()
                .filter(Seguros::getActivo)
                .filter(s -> s.getIdSeguro() != 0)
                .forEach(segurosList::add);

        model.addAttribute("seguros", segurosList);
        return "servicios/seguros";
    }

    @PostMapping("/seguros/reservar")
    public String reservarSeguro(@RequestParam Long seguroId) {
        return "redirect:/servicios/alquiler";
    }

    @GetMapping("/material")
    public String material(Model model) {
        model.addAttribute("materiales", materialService.listarTodos());
        return "servicios/material";
    }
}
