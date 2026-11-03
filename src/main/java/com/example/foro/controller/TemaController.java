package com.example.foro.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.foro.exception.ResourceNotFoundException;
import com.example.foro.model.Tema;
import com.example.foro.service.TemaService;

import jakarta.validation.Valid;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class TemaController {

    private static final Logger log = LoggerFactory.getLogger(TemaController.class);

    @Autowired
    private TemaService temaService;

    @GetMapping("/temas")
    public ResponseEntity<CollectionModel<EntityModel<Tema>>> getAllTemas() {
        log.info("Obteniendo la lista de todos los temas");
        List<EntityModel<Tema>> temas = temaService.getAllTemas().stream()
                .map(this::toModel)
                .collect(Collectors.toList());

        return ResponseEntity.ok(CollectionModel.of(temas,
                linkTo(methodOn(TemaController.class).getAllTemas()).withSelfRel()));
    }

    @GetMapping("/temas/{idTema}")
    public ResponseEntity<EntityModel<Tema>> getTemaById(@PathVariable("idTema") Long idTema) {
        log.info("Buscando tema con ID: {}", idTema);
        Optional<Tema> temaOpt = temaService.getTemaById(idTema);
        if (temaOpt.isPresent()) {
            return ResponseEntity.ok(toModel(temaOpt.get()));
        }

        throw new ResourceNotFoundException("No se encontro el Tema con el id: " + idTema);
    }

    // busca el texto en el titulo o en el contenido del tema
    @GetMapping("/temas/buscar")
    public ResponseEntity<List<Tema>> buscarTemas(@RequestParam("texto") String texto) {
        log.info("Buscando temas que contengan: {}", texto);
        return ResponseEntity.ok(temaService.buscarTemas(texto));
    }

    @GetMapping("/categorias/{idCategoria}/temas")
    public ResponseEntity<CollectionModel<EntityModel<Tema>>> getTemasPorCategoria(@PathVariable("idCategoria") Long idCategoria) {
        log.info("Obteniendo temas de la categoria ID: {}", idCategoria);
        List<EntityModel<Tema>> temas = temaService.getTemasPorCategoria(idCategoria).stream()
                .map(this::toModel)
                .collect(Collectors.toList());

        return ResponseEntity.ok(CollectionModel.of(temas,
                linkTo(methodOn(TemaController.class).getTemasPorCategoria(idCategoria)).withSelfRel(),
                linkTo(methodOn(CategoriaController.class).getCategoriaById(idCategoria)).withRel("categoria")));
    }

    @PostMapping("/categorias/{idCategoria}/temas")
    public ResponseEntity<Tema> crearTema(@PathVariable("idCategoria") Long idCategoria, @Valid @RequestBody Tema tema) {
        log.info("Creando nuevo tema '{}' en la categoria ID: {}", tema.getTitulo(), idCategoria);
        Tema nuevoTema = temaService.crearTema(idCategoria, tema);
        return new ResponseEntity<>(nuevoTema, HttpStatus.CREATED);
    }

    @PutMapping("/temas/{idTema}")
    public ResponseEntity<Tema> actualizarTema(@PathVariable("idTema") Long idTema, @Valid @RequestBody Tema tema) {
        log.info("Actualizando tema ID: {}", idTema);
        return ResponseEntity.ok(temaService.actualizarTema(idTema, tema));
    }

    @DeleteMapping("/temas/{idTema}")
    public ResponseEntity<Void> eliminarTema(@PathVariable("idTema") Long idTema) {
        log.info("Eliminando tema ID: {}", idTema);
        temaService.eliminarTema(idTema);
        return ResponseEntity.noContent().build();
    }

    private EntityModel<Tema> toModel(Tema tema) {
        return EntityModel.of(tema,
                linkTo(methodOn(TemaController.class).getTemaById(tema.getId())).withSelfRel(),
                linkTo(methodOn(TemaController.class).getAllTemas()).withRel("temas"),
                linkTo(methodOn(ComentarioController.class).getComentariosPorTema(tema.getId())).withRel("comentarios"));
    }
}
