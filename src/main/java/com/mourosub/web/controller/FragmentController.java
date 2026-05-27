package com.mourosub.web.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

// @RestController lo que hace es que devuelve directamente el contenido (texto HTML), no plantillas.
// Este controlador sirve el header y el footer como trozos de HTML, para que las
// paginas estaticas los puedan cargar con fetch() y haya una unica copia de cada uno
@RestController
public class FragmentController {

    // ResourceLoader: utilidad de Spring para abrir archivos, ya esten en el classpath o en disco
    private final ResourceLoader resourceLoader;

    // Misma carpeta base que usa Thymeleaf: en Docker apunta al disco montado, si no, al classpath
    @Value("${THYMELEAF_PREFIX:classpath:/templates/}")
    private String templatesBase;

    // Spring inyecta el ResourceLoader por el constructor
    public FragmentController(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    // GET /fragments/header -> devuelve el HTML del header
    @GetMapping(value = "/fragments/header", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> header() throws IOException {
        return readFragment("fragments/header.html");
    }

    // GET /fragments/footer -> devuelve el HTML del footer.
    @GetMapping(value = "/fragments/footer", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> footer() throws IOException {
        return readFragment("fragments/footer.html");
    }
    // GET /fragments/footerBlack -> devuelve el HTML del footer.
    @GetMapping(value = "/fragments/footerBlack", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> footerBlack() throws IOException {
        return readFragment("fragments/footerBlack.html");
    }

    // Metodo auxiliar: abre el archivo indicado y devuelve su contenido como texto.
private ResponseEntity<String> readFragment(String relativePath) throws IOException {
    String base = templatesBase;
    if (!base.endsWith("/")) {
        base = base + "/";
    }

    String[] candidates = new String[] {
            base + relativePath,
            "classpath:/templates/" + relativePath,
            "classpath:/templates/Fragments/" + relativePath.substring(relativePath.lastIndexOf('/') + 1)
    };

    for (String candidate : candidates) {
        Resource resource = resourceLoader.getResource(candidate);
        if (resource.exists()) {
            String html = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
            return ResponseEntity.ok(html);
        }
    }
    return ResponseEntity.notFound().build();
}
}
