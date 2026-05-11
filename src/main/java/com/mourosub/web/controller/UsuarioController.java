package com.mourosub.web.controller;

import com.mourosub.web.model.Usuario;
import com.mourosub.web.service.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import java.time.LocalDate;
import org.springframework.web.bind.annotation.*;


//@Controller, indica que esta clase maneka peticiones web y devuelve vista Thymeleaf
@Controller
//Esto va a indicar que todas las lineas van a empezar por "/usuarios"
@RequestMapping("/usuarios")
public class UsuarioController {
    /*Es un mecanisco de spring que crea los objetos por nosotros y los entrega ya configurados
    En vez de hacer UsuarioService usuarioservice = new UsuarioService(); Spring lo hace automaticamente
    La siguiente linea "private final UsuarioService usuarioService;", representa el servicio que contiene la logica de negocio de los usuarios
    Guarda usuarios, listarlos,buscarlos por id y eliminarlos
    */
    private final UsuarioService usuarioService;
    /*Esto se llama constructor injection, aqui springd detecta que UsuarioService es un servicio, por lo que crear,
    automaticamente una instancia de dicho servicio, cuando creamos el UsuarioController, Spring inyecta el servicio dentro del constructor
    Con ello el atributo queda inicializado y listo para su uso*/
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // GET /usuarios → lista todos
    @GetMapping
    public String listar(Model model) {
        //Model, es como una bolsa donde guardamos todos los datos que queremos enviar a la vista thymealeaf, es decir lo que saldra en el HTML
        /*Aqui llamamos al servicio y el mismo servicio nos devuelve de la base de datos una lista de usuarios.                                                   
        Con ello, mete esa lista en el modelo, es decir, envia la variable usuarios que contiene la lista completa
        */
        model.addAttribute("usuarios", usuarioService.listarTodos());
        //Esto, no nos devuelve texto, esto devuleve el nombre de la plantilla HTML que spring renderizara, por ejemplo Spring buscara algo parecido a esto "src/main/resources/templates/usuarios/lista.html"
        return "usuarios/lista";   // → templates/usuarios/lista.html
    }

    // GET /usuarios/nuevo → formulario
    @GetMapping("/nuevo")
    /*Aqui obtenemos la lista anterior de los usuarios, ya que responde al anterio metodo
    Es decir, cuando el usuario quiere crear un nuevo usuario, este metodo ejecuta /usuarios/nuevo
    Este metodo simplemente nos sirve para mostrar el formulario de creacion de un usuario, ya que no guarda nada, no modifica anda y 
    solo prepara los datos necesario para el formulario
    */
    public String nuevo(Model model) {
        //Model, guarda los datos para que mas tarde thymealeaf lo suse en el HTML
        //new Usuario, aparece con datos pero solo es temporal para probar con datos de ejemplo y que no aparezca vacio
        //
        model.addAttribute("usuario", new Usuario("Carlos", "López Martínez", "carlos@email.com", "87654321B", "612345678", "Calle Mayor 10", "12540", "Vila-real", LocalDate.of(1990, 5, 15)));
        //Esto le dice a spring que renderize la plnatilla templates/usuarios/formularios.html, es decir, el HTML recibira el objeto usuairo y mostrara el formulario
        return "usuarios/formulario";
    }

    // POST /usuarios/guardar → guarda y redirige
    @PostMapping("/guardar")
    /*Aqui obtenemos la lista anteior de los usuarios, es decir, cuando el usuario envia el formulario (crear o editar), el metodo se ejecuta
    Aqui el ModelAttribute, hace que Sprign rellen el objeto Usuario con los datos del formulario
    */ 
    public String guardar(@ModelAttribute Usuario usuario) {
        //En este apartado lo que ocurre es que, si el usuario no tiene ID, crea uno nuevo, is el usuario tiene ID, actualiza el existente
        //este se encarga de validar, llamar y guardar en la base de datos
        usuarioService.guardar(usuario);
        /*El redirect evita que el usuari oreenvie el formulario si refresca la pagina, esto evita duplicar registros, esto sigue
        el patron PRG, que significa POST -> Redirect-> Get
        */
        return "redirect:/usuarios";
    }

    // GET /usuarios/editar/{id}
    @GetMapping("/editar/{id}")
    /*Este metodo lo que hace es que a la hora de editar, busca la Id que se quiere modificar 
    PathVariable, lee el id que viene detras de /editar/y lo mete en la variable id
    */
    public String editar(@PathVariable Long id, Model model) {
        //Llama al servicio para buscar el usuario por su ID, este servicio consulta la base de datos y devuelve el ususario correspondiente a la Id, con ello lo mete dentro del objeto usuario, que contiene los datos actuales del usuario
        model.addAttribute("usuario", usuarioService.buscarPorId(id));
        //Este devuelve el formulario con los datos ya cargados
        return "usuarios/formulario";
    }

    // GET /usuarios/eliminar/{id}
    @GetMapping("/eliminar/{id}")
    //Funciona igual que el anterior metodo, entramos en /usuarios/eliminar/id, este busca la id que se quiere eliminar de la BD
    //El Path, lee el id que viene detras de la barra "/" y lo mete en la variable Id
    public String eliminar(@PathVariable Long id) {
        /*Aqui el controlador no actua directamente eliminando, si no que delega la operacion, ya que el controlar no tiene la logica de negocio
        Entonces esto se delega a UsuarioServices que es quien contiene la logica de negocio.
        Entonces este lo que hace es buscar por Id (si no existe tira una excepcion), valida si se puede eliminar(por si alguien quiere eliminar un admin, salte una excepcion de que no se puede) y finalmente
        elimina el usuario de la base de datos llamando a usuarioService, que ejecuta usuarioRepository.deleteby(id)
        */
        usuarioService.eliminar(id);
        //En resumen, solo sabe que se elimina y ya
        return "redirect:/usuarios";
    }
}
