package com.mourosub.web;

import com.mourosub.web.model.*;
import com.mourosub.web.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Component
@ConditionalOnProperty(prefix = "app.seed", name = "enabled", havingValue = "true")
public class DataLoader implements CommandLineRunner {

    private final UsuarioRepository usuarioRepo;
    private final ReservaRepository reservaRepo;
    private final InstructorRepository instructorRepo;
    private final InmersionRepository inmersionRepo;
    private final ActividadRepository actividadRepo;
    private final CursoRepository cursoRepo;

    public DataLoader(UsuarioRepository usuarioRepo, ReservaRepository reservaRepo,
                      InstructorRepository instructorRepo,
                      InmersionRepository inmersionRepo,
                      ActividadRepository actividadRepo,
                      CursoRepository cursoRepo) {
        this.usuarioRepo = usuarioRepo;
        this.reservaRepo = reservaRepo;
        this.instructorRepo = instructorRepo;
        this.inmersionRepo = inmersionRepo;
        this.actividadRepo = actividadRepo;
        this.cursoRepo = cursoRepo;
    }

    @Override
    public void run(String... args) {
        crearInstructor();
        crearUsuario();
        crearInmersiones();
        crearActividades();
        crearCursos();
        crearReservas();
        System.out.println("=== DATOS DE PRUEBA INSERTADOS ===");
    }

    private void crearInstructor() {
        if (instructorRepo.count() == 0) {
            Instructor instructor = new Instructor("María García", "12345678A", "PADI Divemaster, SSI Level 3");
            instructorRepo.save(instructor);
        }
    }

    private void crearUsuario() {
        if (usuarioRepo.count() == 0) {
            Usuario usuario = new Usuario();
            usuario.setSupabaseUserId(UUID.randomUUID());
            usuario.setNombre("Usuario Demo");
            usuario.setEmail("demo@mouroSub.com");
            usuario.setAdmin(true);
            usuarioRepo.save(usuario);
        }
    }

    private void crearInmersiones() {
        if (inmersionRepo.count() == 0) {
            Inmersiones i1 = new Inmersiones();
            i1.setNombre("Isla de Mouro");
            i1.setTipo("INMERSION");
            i1.setDescripcion("Declarada Reserva natural en 1986, la Isla de Mouro está situada a la salida de la Bahía de Santander. Contiene una gran biodiversidad con más de 39 especies diferentes de peces. Formada por roca caliza en estratos con escasa vida vegetal y colonizada por miles de aves.");
            i1.setUbicacion("Cantabria");
            i1.setNivelReq("Todos los niveles");
            i1.setDificultad("Todos los niveles");
            i1.setProfundidadMax(30);
            i1.setVisibilidad(15);
            i1.setCorriente("Leve");
            i1.setTemperaturaAgua("16-20°C");
            i1.setEquipamientoIncluido("Botella 12L, regulador, chalecoBCD, traje 7mm");
            i1.setRequisitos("Certificación básica de buceo o bautismo previo");
            i1.setPrecio(new BigDecimal("65.00"));
            i1.setPlazasMax(8);
            i1.setplazasOcupadas(0);
            i1.setFechaActividad(LocalDate.now().plusDays(7));
            i1.setDuracionMinutos(45);
            i1.setActivo(true);
            inmersionRepo.save(i1);

            Inmersiones i2 = new Inmersiones();
            i2.setNombre("El Palacio");
            i2.setTipo("INMERSION");
            i2.setDescripcion("El Palacio es una formación rocosa espectacular que ofrece inmersiones memorables con abundante vida marina. Sus paredes verticales y grietas profundas son el hogar de congrios, pulpos y grandes decipolos.");
            i2.setUbicacion("Cantabria");
            i2.setNivelReq("Medio-Avanzado");
            i2.setDificultad("Medio");
            i2.setProfundidadMax(25);
            i2.setVisibilidad(20);
            i2.setCorriente("Moderada");
            i2.setTemperaturaAgua("15-19°C");
            i2.setEquipamientoIncluido("Botella 15L, regulador, chalecoBCD, traje 7mm");
            i2.setRequisitos("Open Water o equivalente con al menos 10 inmersiones");
            i2.setPrecio(new BigDecimal("75.00"));
            i2.setPlazasMax(6);
            i2.setplazasOcupadas(0);
            i2.setFechaActividad(LocalDate.now().plusDays(10));
            i2.setDuracionMinutos(50);
            i2.setActivo(true);
            inmersionRepo.save(i2);

            Inmersiones i3 = new Inmersiones();
            i3.setNombre("Isla de Santa Marina");
            i3.setTipo("INMERSION");
            i3.setDescripcion("La escollera de la isla de Santa Marina ofrece refugio a una gran variedad de especies marinas en un entorno único. Sus fondos de roca y arena albergan desde pequeños crustáceos hasta grandes peces pelágicos.");
            i3.setUbicacion("Cantabria");
            i3.setNivelReq("Todos los niveles");
            i3.setDificultad("Todos los niveles");
            i3.setProfundidadMax(20);
            i3.setVisibilidad(12);
            i3.setCorriente("Ninguna");
            i3.setTemperaturaAgua("17-21°C");
            i3.setEquipamientoIncluido("Botella 12L, snorkel, gafas, aletas");
            i3.setRequisitos("Ninguno - perfecta para principiantes");
            i3.setPrecio(new BigDecimal("60.00"));
            i3.setPlazasMax(10);
            i3.setplazasOcupadas(0);
            i3.setFechaActividad(LocalDate.now().plusDays(14));
            i3.setDuracionMinutos(40);
            i3.setActivo(true);
            inmersionRepo.save(i3);

            Inmersiones i4 = new Inmersiones();
            i4.setNombre("Cabo Menor y Cabo Mayor");
            i4.setTipo("INMERSION");
            i4.setDescripcion("Acantilados espectaculares que caen directamente hacia aguas profundas, creando un paisaje submarino impresionante. Estas formaciones ofrecen la oportunidad de ver grandes peces pelágicos y especies de aguas profundas.");
            i4.setUbicacion("Cantabria");
            i4.setNivelReq("Avanzado");
            i4.setDificultad("Avanzado");
            i4.setProfundidadMax(40);
            i4.setVisibilidad(25);
            i4.setCorriente("Fuerte");
            i4.setTemperaturaAgua("12-16°C");
            i4.setEquipamientoIncluido("Botella 15L, traje seco, regulador, lastre");
            i4.setRequisitos("Advanced Open Water, +50 inmersiones, traje seco");
            i4.setPrecio(new BigDecimal("85.00"));
            i4.setPlazasMax(4);
            i4.setplazasOcupadas(0);
            i4.setFechaActividad(LocalDate.now().plusDays(18));
            i4.setDuracionMinutos(55);
            i4.setActivo(true);
            inmersionRepo.save(i4);

            Inmersiones i5 = new Inmersiones();
            i5.setNombre("Bajos y Cabezos");
            i5.setTipo("INMERSION");
            i5.setDescripcion("Formaciones subacuáticas de gran interés biológico donde convergen múltiples especies de vida marina. Ideal para buceadores que disfrutan de la observación de fauna y la fotografía submarina.");
            i5.setUbicacion("Cantabria");
            i5.setNivelReq("Medio-Avanzado");
            i5.setDificultad("Medio");
            i5.setProfundidadMax(30);
            i5.setVisibilidad(18);
            i5.setCorriente("Leve");
            i5.setTemperaturaAgua("15-19°C");
            i5.setEquipamientoIncluido("Botella 12L, regulador, chalecoBCD, traje 5mm");
            i5.setRequisitos("Open Water con 20+ inmersiones");
            i5.setPrecio(new BigDecimal("70.00"));
            i5.setPlazasMax(6);
            i5.setplazasOcupadas(0);
            i5.setFechaActividad(LocalDate.now().plusDays(21));
            i5.setDuracionMinutos(45);
            i5.setActivo(true);
            inmersionRepo.save(i5);

            Inmersiones i6 = new Inmersiones();
            i6.setNombre("Pecios");
            i6.setTipo("INMERSION");
            i6.setDescripcion("Restos de barcos hundidos que han creado ecosistemas artificiales llenos de historia y vida. Cada inmersión en un pecio es una aventura diferente, donde la historia y la naturaleza se unen.");
            i6.setUbicacion("Cantabria");
            i6.setNivelReq("Avanzado");
            i6.setDificultad("Avanzado");
            i6.setProfundidadMax(45);
            i6.setVisibilidad(20);
            i6.setCorriente("Moderada");
            i6.setTemperaturaAgua("14-18°C");
            i6.setEquipamientoIncluido("Botella 15L, traje seco, regulador, foco");
            i6.setRequisitos("Rescue Diver, +100 inmersiones,-nitrox recomendado");
            i6.setPrecio(new BigDecimal("90.00"));
            i6.setPlazasMax(4);
            i6.setplazasOcupadas(0);
            i6.setFechaActividad(LocalDate.now().plusDays(25));
            i6.setDuracionMinutos(60);
            i6.setActivo(true);
            inmersionRepo.save(i6);
        }
    }

    private void crearActividades() {
        if (actividadRepo.count() == 0) {
            Actividad a1 = new Actividad();
            a1.setNombre("Niños y Buceo");
            a1.setTipo("ACTIVIDAD");
            a1.setCategoria("INFANTIL");
            a1.setDescripcion("Actividades diseñadas para introducir a los más pequeños en el mundo submarino. Una experiencia educativa y divertida para descubrir el entorno marino mediante juegos y actividades adaptadas. Todas las actividades están supervisadas por profesionales especializados.");
            a1.setPrecio(new BigDecimal("75.00"));
            a1.setPlazasMax(6);
            a1.setplazasOcupadas(0);
            a1.setFechaActividad(LocalDate.now().plusDays(7));
            a1.setDuracionMinutos(120);
            a1.setActivo(true);
            a1.setImagenUrl("/img/Actividades/Imagen2.jpg");
            actividadRepo.save(a1);

            Actividad a2 = new Actividad();
            a2.setNombre("Snorkelling");
            a2.setTipo("ACTIVIDAD");
            a2.setCategoria("EXPLORACIÓN");
            a2.setDescripcion("Explora la costa y descubre la fauna marina desde la superficie. Una actividad perfecta para todas las edades y niveles que quieran disfrutar del mar. Descubrirás zonas de gran biodiversidad acompañado por profesionales.");
            a2.setPrecio(new BigDecimal("45.00"));
            a2.setPlazasMax(12);
            a2.setplazasOcupadas(0);
            a2.setFechaActividad(LocalDate.now().plusDays(5));
            a2.setDuracionMinutos(120);
            a2.setActivo(true);
            a2.setImagenUrl("/img/Actividades/Imagen3.jpg");
            actividadRepo.save(a2);

            Actividad a3 = new Actividad();
            a3.setNombre("Bautismos de Buceo");
            a3.setTipo("ACTIVIDAD");
            a3.setCategoria("INICIACIÓN");
            a3.setDescripcion("Vive tu primera inmersión respirando bajo el agua. Descubre el submarinismo de forma segura acompañado por instructores especializados. Una experiencia emocionante para iniciarse en el buceo.");
            a3.setPrecio(new BigDecimal("89.00"));
            a3.setPlazasMax(8);
            a3.setplazasOcupadas(0);
            a3.setFechaActividad(LocalDate.now().plusDays(3));
            a3.setDuracionMinutos(180);
            a3.setActivo(true);
            a3.setImagenUrl("/img/Actividades/Imagen4.jpg");
            actividadRepo.save(a3);

            Actividad a4 = new Actividad();
            a4.setNombre("Club Mourosub");
            a4.setTipo("ACTIVIDAD");
            a4.setCategoria("COMUNIDAD");
            a4.setDescripcion("Comparte experiencias y forma parte de nuestra comunidad submarina. Participa en actividades, inmersiones y encuentros durante todo el año. Un espacio para seguir aprendiendo y disfrutando del mar.");
            a4.setPrecio(new BigDecimal("50.00"));
            a4.setPlazasMax(20);
            a4.setplazasOcupadas(0);
            a4.setFechaActividad(LocalDate.now().plusDays(1));
            a4.setDuracionMinutos(240);
            a4.setActivo(true);
            a4.setImagenUrl("/img/Actividades/Imagen5.jpg");
            actividadRepo.save(a4);

            Actividad a5 = new Actividad();
            a5.setNombre("Rutas en Barco");
            a5.setTipo("ACTIVIDAD");
            a5.setCategoria("NATURALEZA");
            a5.setDescripcion("Navega y descubre espacios únicos del Cantábrico. Disfruta de rutas guiadas por la bahía y el littoral. Una experiencia perfecta para familias y grupos.");
            a5.setPrecio(new BigDecimal("65.00"));
            a5.setPlazasMax(10);
            a5.setplazasOcupadas(0);
            a5.setFechaActividad(LocalDate.now().plusDays(6));
            a5.setDuracionMinutos(180);
            a5.setActivo(true);
            a5.setImagenUrl("/img/Actividades/Imagen6.jpg");
            actividadRepo.save(a5);

            Actividad a6 = new Actividad();
            a6.setNombre("Naturaleza y Medio Ambiente");
            a6.setTipo("ACTIVIDAD");
            a6.setCategoria("ECO");
            a6.setDescripcion("Conservación y divulgación marina para todas las edades. Descubre la biodiversidad marina mediante actividades educativas. Aprende sobre protección y conservación del entorno marino.");
            a6.setPrecio(new BigDecimal("40.00"));
            a6.setPlazasMax(15);
            a6.setplazasOcupadas(0);
            a6.setFechaActividad(LocalDate.now().plusDays(8));
            a6.setDuracionMinutos(120);
            a6.setActivo(true);
            a6.setImagenUrl("/img/Actividades/Imagen7.jpg");
            actividadRepo.save(a6);

            Actividad a7 = new Actividad();
            a7.setNombre("Talleres Técnicos");
            a7.setTipo("ACTIVIDAD");
            a7.setCategoria("FORMACIÓN");
            a7.setDescripcion("Aprende técnicas, seguridad y mantenimiento especializado. Talleres prácticos impartidos por profesionales especializados. Formación para mejorar conocimientos y habilidades técnicas.");
            a7.setPrecio(new BigDecimal("55.00"));
            a7.setPlazasMax(8);
            a7.setplazasOcupadas(0);
            a7.setFechaActividad(LocalDate.now().plusDays(10));
            a7.setDuracionMinutos(180);
            a7.setActivo(true);
            a7.setImagenUrl("/img/Actividades/Imagen8.jpg");
            actividadRepo.save(a7);
        }
    }

    private void crearCursos() {
        if (cursoRepo.count() > 0) return;

        // APNEA
        Curso c1 = new Curso();
        c1.setNombre("Try Freediving");
        c1.setTipo("CURSO");
        c1.setCategoria("APNEA");
        c1.setDescripcion("Programa de introducción a la apnea SSI. Aprende los conceptos básicos de la apnea en un entorno seguro y controlado mientras te diviertes.");
        c1.setPrecio(new BigDecimal("80.00"));
        c1.setPlazasMax(6);
        c1.setplazasOcupadas(0);
        c1.setFechaActividad(LocalDate.now().plusDays(5));
        c1.setDuracionMinutos(240);
        c1.setActivo(true);
        c1.setImagenUrl("/img/Cursos/598-11.jpg");
        c1.setNivelRequerido("Ninguno");
        c1.setDuracionHoras(4);
        c1.setTemario("Técnicas básicas de respiración, relajamiento, equalización básica, seguridad en apnea, práctica en aguas confinadas");
        c1.setIncluye("Instructor certificado, equipo completo");
        c1.setCertificacionIncluida("SSI Try Freediving");
        c1.setNumModulos(1);
        c1.setTeoriaOnline(false);
        cursoRepo.save(c1);

        Curso c2 = new Curso();
        c2.setNombre("Apnea Básica");
        c2.setTipo("CURSO");
        c2.setCategoria("APNEA");
        c2.setDescripcion("Habilidades para realizar inmersiones en apnea hasta 5 metros en piscina. Incluye técnicas de respiración correcta, control de la flotabilidad y seguridad básica.");
        c2.setPrecio(new BigDecimal("180.00"));
        c2.setPlazasMax(8);
        c2.setplazasOcupadas(0);
        c2.setFechaActividad(LocalDate.now().plusDays(10));
        c2.setDuracionMinutos(480);
        c2.setActivo(true);
        c2.setImagenUrl("/img/Cursos/598-11.jpg");
        c2.setNivelRequerido("Ninguno");
        c2.setDuracionHoras(8);
        c2.setTemario("Técnicas avanzadas de respiración, control de relajamiento, equalización avanzada, conceptos de seguridad en apnea, prácticas en piscina");
        c2.setIncluye("Instructor certificado, equipo completo, material digital");
        c2.setCertificacionIncluida("SSI Apnea Basic");
        c2.setNumModulos(4);
        c2.setTeoriaOnline(false);
        cursoRepo.save(c2);

        Curso c3 = new Curso();
        c3.setNombre("Apnea Nivel 1");
        c3.setTipo("CURSO");
        c3.setCategoria("APNEA");
        c3.setDescripcion("Cualifica para inmersiones hasta 20 metros en aguas abiertas. Combina formación teórica sobre física y fisiología de la apnea con prácticas intensas.");
        c3.setPrecio(new BigDecimal("280.00"));
        c3.setPlazasMax(8);
        c3.setplazasOcupadas(0);
        c3.setFechaActividad(LocalDate.now().plusDays(15));
        c3.setDuracionMinutos(960);
        c3.setActivo(true);
        c3.setImagenUrl("/img/Cursos/598-11.jpg");
        c3.setNivelRequerido("Ninguno");
        c3.setDuracionHoras(16);
        c3.setTemario("Física de la apnea, fisiología básica, técnicas de equalización avanzadas, protocolos de descenso y ascenso, prácticas en aguas abiertas");
        c3.setIncluye("Instructor certificado, equipo completo, material digital, inmersiones en aguas abiertas");
        c3.setCertificacionIncluida("SSI Apnea Level 1");
        c3.setNumModulos(4);
        c3.setTeoriaOnline(true);
        cursoRepo.save(c3);

        Curso c4 = new Curso();
        c4.setNombre("Apnea Nivel 2");
        c4.setTipo("CURSO");
        c4.setCategoria("APNEA");
        c4.setDescripcion("Cualifica para inmersiones hasta 30 metros. Profundiza en técnicas de relajamiento profundo, recuperación entre inmersiones y protocolos de seguridad avanzados.");
        c4.setPrecio(new BigDecimal("380.00"));
        c4.setPlazasMax(6);
        c4.setplazasOcupadas(0);
        c4.setFechaActividad(LocalDate.now().plusDays(20));
        c4.setDuracionMinutos(1440);
        c4.setActivo(true);
        c4.setImagenUrl("/img/Cursos/598-11.jpg");
        c4.setNivelRequerido("Apnea Nivel 1");
        c4.setDuracionHoras(24);
        c4.setTemario("Técnicas de relajamiento profundo, módulo de resistencia a la hipoxia (LMC), técnicas de recuperación, protocolos de seguridad avanzados, prácticas intensivas en aguas abiertas");
        c4.setIncluye("Instructor certificado, equipo completo, material digital");
        c4.setCertificacionIncluida("SSI Apnea Level 2");
        c4.setNumModulos(5);
        c4.setTeoriaOnline(false);
        cursoRepo.save(c4);

        Curso c5 = new Curso();
        c5.setNombre("Apnea Nivel 3");
        c5.setTipo("CURSO");
        c5.setCategoria("APNEA");
        c5.setDescripcion("Nivel más alto de formación recreativa en apnea SSI, hasta 40 metros. Requiere dominio absoluto de técnicas de equalización, control del reflejo de inmersión y gestión del estrés fisiológico.");
        c5.setPrecio(new BigDecimal("480.00"));
        c5.setPlazasMax(4);
        c5.setplazasOcupadas(0);
        c5.setFechaActividad(LocalDate.now().plusDays(25));
        c5.setDuracionMinutos(1920);
        c5.setActivo(true);
        c5.setImagenUrl("/img/Cursos/598-11.jpg");
        c5.setNivelRequerido("Apnea Nivel 2");
        c5.setDuracionHoras(32);
        c5.setTemario("Técnicas de equalización Frenzel masterizadas, control del reflejo de inmersión, módulo de recuperación activa, gestión del medio hipotérmico, prácticas extremas en aguas abiertas");
        c5.setIncluye("Instructor certificado, equipo completo, material digital, certificación internacional");
        c5.setCertificacionIncluida("SSI Apnea Level 3");
        c5.setNumModulos(5);
        c5.setTeoriaOnline(false);
        cursoRepo.save(c5);

        // RECREATIVO
        Curso c6 = new Curso();
        c6.setNombre("Open Water Diver");
        c6.setTipo("CURSO");
        c6.setCategoria("RECREATIVO");
        c6.setDescripcion("Certificación internacional que permite bucear hasta 18 metros. Formación teórica online + prácticas en agua.");
        c6.setPrecio(new BigDecimal("450.00"));
        c6.setPlazasMax(6);
        c6.setplazasOcupadas(0);
        c6.setFechaActividad(LocalDate.now().plusDays(12));
        c6.setDuracionMinutos(1440);
        c6.setActivo(true);
        c6.setImagenUrl("/img/Cursos/598-21.jpg");
        c6.setNivelRequerido("Ninguno");
        c6.setDuracionHoras(24);
        c6.setTemario("Equipamiento, física del buceo, fisiología, entorno submarino, comunicaciones, planificación de inmersión");
        c6.setIncluye("Material digital, inmersiones en aguas abiertas, certificación internacional");
        c6.setCertificacionIncluida("SSI Open Water Diver");
        c6.setNumModulos(5);
        c6.setTeoriaOnline(true);
        cursoRepo.save(c6);

        Curso c7 = new Curso();
        c7.setNombre("Advanced Open Water Diver");
        c7.setTipo("CURSO");
        c7.setCategoria("RECREATIVO");
        c7.setDescripcion("Certificación avanzada para buceadores con experiencia. 5 inmersiones de especialización y formación teórica avanzada.");
        c7.setPrecio(new BigDecimal("400.00"));
        c7.setPlazasMax(6);
        c7.setplazasOcupadas(0);
        c7.setFechaActividad(LocalDate.now().plusDays(25));
        c7.setDuracionMinutos(960);
        c7.setActivo(true);
        c7.setImagenUrl("/img/Cursos/598-21.jpg");
        c7.setNivelRequerido("Open Water");
        c7.setDuracionHoras(16);
        c7.setTemario("Inmersión profunda, navegación, flotabilidad avanzada, fauna marina, inmersión en corriente");
        c7.setIncluye("Material digital, 5 inmersiones, certificación Advanced");
        c7.setCertificacionIncluida("SSI Advanced Open Water Diver");
        c7.setNumModulos(5);
        c7.setTeoriaOnline(true);
        cursoRepo.save(c7);

        Curso c8 = new Curso();
        c8.setNombre("Buceador Stress & Rescue");
        c8.setTipo("CURSO");
        c8.setCategoria("RECREATIVO");
        c8.setDescripcion("Aprende a prevenir y gestionar emergencias bajo el agua. Te convierte en un buceador consciente y responsable, capaz de ayudar a otros.");
        c8.setPrecio(new BigDecimal("420.00"));
        c8.setPlazasMax(6);
        c8.setplazasOcupadas(0);
        c8.setFechaActividad(LocalDate.now().plusDays(22));
        c8.setDuracionMinutos(1200);
        c8.setActivo(true);
        c8.setImagenUrl("/img/Cursos/598-21.jpg");
        c8.setNivelRequerido("Advanced Open Water");
        c8.setDuracionHoras(20);
        c8.setTemario("Prevención de accidentes, técnicas de búsqueda y recuperación, gestión de emergencias, primeros auxilios subacuáticos");
        c8.setIncluye("Material digital, 10 inmersiones de rescate, certificación Rescue");
        c8.setCertificacionIncluida("SSI Buceador Stress & Rescue");
        c8.setNumModulos(4);
        c8.setTeoriaOnline(false);
        cursoRepo.save(c8);

        Curso c9 = new Curso();
        c9.setNombre("Speciality Diver");
        c9.setTipo("CURSO");
        c9.setCategoria("RECREATIVO");
        c9.setDescripcion("Especialízate en áreas específicas del buceo: profundidad, navegación, fotografía, nitrox y más.");
        c9.setPrecio(new BigDecimal("250.00"));
        c9.setPlazasMax(8);
        c9.setplazasOcupadas(0);
        c9.setFechaActividad(LocalDate.now().plusDays(18));
        c9.setDuracionMinutos(960);
        c9.setActivo(true);
        c9.setImagenUrl("/img/Cursos/598-21.jpg");
        c9.setNivelRequerido("Open Water");
        c9.setDuracionHoras(16);
        c9.setTemario("Módulos variables según especialidad elegida");
        c9.setIncluye("Material digital, inmersiones de especialidad");
        c9.setCertificacionIncluida("SSI Specialty Diver");
        c9.setNumModulos(2);
        c9.setTeoriaOnline(true);
        cursoRepo.save(c9);

        Curso c10 = new Curso();
        c10.setNombre("Aire Enriquecido Nitrox");
        c10.setTipo("CURSO");
        c10.setCategoria("RECREATIVO");
        c10.setDescripcion("Certificación para bucear con mezclas de oxígeno superiores al aire. Reduce los tiempos de recuperación y aumenta los límites de profundidad.");
        c10.setPrecio(new BigDecimal("220.00"));
        c10.setPlazasMax(8);
        c10.setplazasOcupadas(0);
        c10.setFechaActividad(LocalDate.now().plusDays(8));
        c10.setDuracionMinutos(480);
        c10.setActivo(true);
        c10.setImagenUrl("/img/Cursos/598-11.jpg");
        c10.setImagenUrl("/img/Cursos/598-21.jpg");
        c10.setNivelRequerido("Open Water");
        c10.setDuracionHoras(8);
        c10.setTemario("Fisiología del nitrox, planificación de inmersión con EAN, gestión de oxígeno, puntos críticos");
        c10.setIncluye("Material digital, 2 inmersiones con nitrox, certificación Nitrox");
        c10.setCertificacionIncluida("SSI Nitrox Diver");
        c10.setNumModulos(2);
        c10.setTeoriaOnline(true);
        cursoRepo.save(c10);

        // EMERGENCIA
        Curso e1 = new Curso();
        e1.setNombre("React Right");
        e1.setTipo("CURSO");
        e1.setCategoria("EMERGENCIA");
        e1.setDescripcion("Programa integral de primeros auxilios y respuesta a emergencias. Certificación reconocida internacionalmente para buceadores.");
        e1.setPrecio(new BigDecimal("150.00"));
        e1.setPlazasMax(10);
        e1.setplazasOcupadas(0);
        e1.setFechaActividad(LocalDate.now().plusDays(5));
        e1.setDuracionMinutos(480);
        e1.setActivo(true);
        e1.setImagenUrl("/img/Cursos/598-48.jpg");
        e1.setNivelRequerido("Ninguno");
        e1.setDuracionHoras(8);
        e1.setTemario("Primeros auxilios, RCP, uso de oxígeno, evaluación neurológica");
        e1.setIncluye("Instructor certificado, material, certificación internacional");
        e1.setCertificacionIncluida("SSI React Right");
        e1.setNumModulos(4);
        e1.setTeoriaOnline(false);
        cursoRepo.save(e1);

        Curso e2 = new Curso();
        e2.setNombre("Basic Life Support DAN");
        e2.setTipo("CURSO");
        e2.setCategoria("EMERGENCIA");
        e2.setDescripcion("Soporte vital básico con énfasis en situaciones acuáticas. Técnicas de RCP y manejo de vías respiratorias.");
        e2.setPrecio(new BigDecimal("120.00"));
        e2.setPlazasMax(12);
        e2.setplazasOcupadas(0);
        e2.setFechaActividad(LocalDate.now().plusDays(6));
        e2.setDuracionMinutos(360);
        e2.setActivo(true);
        e2.setImagenUrl("/img/Cursos/598-48.jpg");
        e2.setNivelRequerido("Ninguno");
        e2.setDuracionHoras(6);
        e2.setTemario("RCP, manejo de vías respiratorias, desfibrilador");
        e2.setIncluye("Instructor certificado, manual");
        e2.setCertificacionIncluida("DAN Basic Life Support");
        e2.setNumModulos(2);
        e2.setTeoriaOnline(false);
        cursoRepo.save(e2);

        Curso e3 = new Curso();
        e3.setNombre("Oxygen First Aid for Scuba Diving Injuries DAN");
        e3.setTipo("CURSO");
        e3.setCategoria("EMERGENCIA");
        e3.setDescripcion("Primeros auxilios con oxígeno para lesiones de buceo. Tratamiento de enfermedades de descompresión.");
        e3.setPrecio(new BigDecimal("200.00"));
        e3.setPlazasMax(8);
        e3.setplazasOcupadas(0);
        e3.setFechaActividad(LocalDate.now().plusDays(8));
        e3.setDuracionMinutos(480);
        e3.setActivo(true);
        e3.setImagenUrl("/img/Cursos/598-48.jpg");
        e3.setNivelRequerido("Ninguno");
        e3.setDuracionHoras(8);
        e3.setTemario("Tratamiento con oxígeno, enfermedades de descompresión, lesión por presión");
        e3.setIncluye("Instructor certificado, equipo de oxígeno");
        e3.setCertificacionIncluida("DAN Oxygen Provider");
        e3.setNumModulos(2);
        e3.setTeoriaOnline(false);
        cursoRepo.save(e3);

        // TÉCNICO
        Curso t1 = new Curso();
        t1.setNombre("Extended Range Nitrox Diving");
        t1.setTipo("CURSO");
        t1.setCategoria("TÉCNICO");
        t1.setDescripcion("Introducción al buceo técnico con nitrox. Formación para extensiones de rango hasta 40 metros con mezclas de aire enriquecido.");
        t1.setPrecio(new BigDecimal("450.00"));
        t1.setPlazasMax(4);
        t1.setplazasOcupadas(0);
        t1.setFechaActividad(LocalDate.now().plusDays(15));
        t1.setDuracionMinutos(1440);
        t1.setActivo(true);
        t1.setImagenUrl("/img/Cursos/598-55.jpg");
        t1.setImagenUrl("/img/Cursos/598-31.jpg");
        t1.setNivelRequerido("Advanced Open Water");
        t1.setDuracionHoras(24);
        t1.setTemario("Mezclas EAN, planificación de inmersión con nitrox, gestión de oxígeno, límites de profundidad");
        t1.setIncluye("Instructor técnico, material especializado, inmersiones en profundidad");
        t1.setCertificacionIncluida("SSI Extended Range Nitrox");
        t1.setNumModulos(5);
        t1.setTeoriaOnline(true);
        cursoRepo.save(t1);

        Curso t2 = new Curso();
        t2.setNombre("Extended Range");
        t2.setTipo("CURSO");
        t2.setCategoria("TÉCNICO");
        t2.setDescripcion("Certificación para buceo técnico extendido hasta 50 metros. Manejo de múltiples gases y planificación de inmersiones técnicas.");
        t2.setPrecio(new BigDecimal("650.00"));
        t2.setPlazasMax(4);
        t2.setplazasOcupadas(0);
        t2.setFechaActividad(LocalDate.now().plusDays(20));
        t2.setDuracionMinutos(1920);
        t2.setActivo(true);
        t2.setImagenUrl("/img/Cursos/598-55.jpg");
        t2.setImagenUrl("/img/Cursos/598-31.jpg");
        t2.setNivelRequerido("Extended Range Nitrox");
        t2.setDuracionHoras(32);
        t2.setTemario("Gestión de múltiples gases, planificación descompresiva, procedimientos técnicos");
        t2.setIncluye("Instructor técnico, gas mezclas, inmersiones profundas");
        t2.setCertificacionIncluida("SSI Extended Range");
        t2.setNumModulos(6);
        t2.setTeoriaOnline(true);
        cursoRepo.save(t2);

        Curso t3 = new Curso();
        t3.setNombre("Trimix Hipóxico");
        t3.setTipo("CURSO");
        t3.setCategoria("TÉCNICO");
        t3.setDescripcion("Mezclas trimix para profundidades técnicas. Aprende a planificar y ejecutar inmersiones con gases hipotóxicos.");
        t3.setPrecio(new BigDecimal("750.00"));
        t3.setPlazasMax(4);
        t3.setplazasOcupadas(0);
        t3.setFechaActividad(LocalDate.now().plusDays(22));
        t3.setDuracionMinutos(1920);
        t3.setActivo(true);
        t3.setImagenUrl("/img/Cursos/598-55.jpg");
        t3.setImagenUrl("/img/Cursos/598-31.jpg");
        t3.setNivelRequerido("Extended Range");
        t3.setDuracionHoras(32);
        t3.setTemario("Mezclas trimix, cálculos de gas, planificación descompresiva, procedimientos de seguridad");
        t3.setIncluye("Instructor técnico, material especializado");
        t3.setCertificacionIncluida("SSI Trimix");
        t3.setNumModulos(6);
        t3.setTeoriaOnline(true);
        cursoRepo.save(t3);

        // PROFESIONAL
        Curso p1 = new Curso();
        p1.setNombre("Dive Guide");
        p1.setTipo("CURSO");
        p1.setCategoria("PROFESIONAL");
        p1.setDescripcion("Introducción a la liderazgo de buceo. Aprende a guiar grupos de buceadores en entornos controlados.");
        p1.setPrecio(new BigDecimal("500.00"));
        p1.setPlazasMax(6);
        p1.setplazasOcupadas(0);
        p1.setFechaActividad(LocalDate.now().plusDays(20));
        p1.setDuracionMinutos(1440);
        p1.setActivo(true);
        p1.setImagenUrl("/img/Cursos/598-66.jpg");
        p1.setImagenUrl("/img/Cursos/598-50.jpg");
        p1.setNivelRequerido("Advanced Open Water");
        p1.setDuracionHoras(24);
        p1.setTemario("Liderazgo, guía de grupos, planificación de inmersiones, seguridad");
        p1.setIncluye("Instructor, inmersiones de guía");
        p1.setCertificacionIncluida("SSI Dive Guide");
        p1.setNumModulos(5);
        p1.setTeoriaOnline(true);
        cursoRepo.save(p1);

        Curso p2 = new Curso();
        p2.setNombre("Dive Control Specialist");
        p2.setTipo("CURSO");
        p2.setCategoria("PROFESIONAL");
        p2.setDescripcion("Certificación profesional para control y supervisión de actividades de buceo. Gestión de grupos, resolución de problemas y seguridad.");
        p2.setPrecio(new BigDecimal("600.00"));
        p2.setPlazasMax(6);
        p2.setplazasOcupadas(0);
        p2.setFechaActividad(LocalDate.now().plusDays(25));
        p2.setDuracionMinutos(1680);
        p2.setActivo(true);
        p2.setImagenUrl("/img/Cursos/598-66.jpg");
        p2.setImagenUrl("/img/Cursos/598-50.jpg");
        p2.setNivelRequerido("Dive Guide");
        p2.setDuracionHoras(28);
        p2.setTemario("Control de grupos, supervisión, resolución de emergencias, gestión de centro de buceo");
        p2.setIncluye("Instructor, prácticas de control");
        p2.setCertificacionIncluida("SSI Dive Control Specialist");
        p2.setNumModulos(6);
        p2.setTeoriaOnline(true);
        cursoRepo.save(p2);

        Curso p3 = new Curso();
        p3.setNombre("Instructor Training Course");
        p3.setTipo("CURSO");
        p3.setCategoria("PROFESIONAL");
        p3.setDescripcion("Curso de formación de instructores SSI. Preparación para la certificación de instructor de buceo.");
        p3.setPrecio(new BigDecimal("1200.00"));
        p3.setPlazasMax(4);
        p3.setplazasOcupadas(0);
        p3.setFechaActividad(LocalDate.now().plusDays(30));
        p3.setDuracionMinutos(2400);
        p3.setActivo(true);
        p3.setImagenUrl("/img/Cursos/598-66.jpg");
        p3.setImagenUrl("/img/Cursos/598-50.jpg");
        p3.setNivelRequerido("Dive Control Specialist");
        p3.setDuracionHoras(40);
        p3.setTemario("Metodología de enseñanza, presentación de temas, evaluación de estudiantes, estándares SSI");
        p3.setIncluye("Instructor principal, material de ITC, evaluación");
        p3.setCertificacionIncluida("SSI Instructor Training Course");
        p3.setNumModulos(8);
        p3.setTeoriaOnline(true);
        cursoRepo.save(p3);
    }

    private void crearReservas() {
        if (reservaRepo.count() == 0) {
            Usuario usuario = usuarioRepo.findAll().stream().findFirst().orElse(null);
            Instructor instructor = instructorRepo.findAll().stream().findFirst().orElse(null);

            if (usuario != null && instructor != null) {
                Reserva r1 = new Reserva(
                    "inmersion", "bautismo", "SRV-001",
                    2, new BigDecimal("120.00"), "confirmada",
                    LocalDate.of(2026, 6, 10), usuario, instructor
                );
                reservaRepo.save(r1);

                Reserva r2 = new Reserva(
                    "curso", "open_water", "SRV-002",
                    1, new BigDecimal("450.00"), "pendiente",
                    LocalDate.of(2026, 6, 20), usuario, instructor
                );
                reservaRepo.save(r2);
            }
        }
    }
}