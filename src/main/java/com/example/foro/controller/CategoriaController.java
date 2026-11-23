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
import org.springframework.web.bind.annotation.RestController;

import com.example.foro.exception.ResourceNotFoundException;
import com.example.foro.model.Categoria;
import com.example.foro.service.CategoriaService;

import jakarta.validation.Valid;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class CategoriaController {

    private static final Logger log = LoggerFactory.getLogger(CategoriaController.class);

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping("/categorias")
    public ResponseEntity<CollectionModel<EntityModel<Categoria>>> getAllCategorias() {
        log.info("Obteniendo la lista de todas las categorias");
        List<EntityModel<Categoria>> categorias = categoriaService.getAllCategorias().stream()
                .map(this::toModel)
                .collect(Collectors.toList());

        return ResponseEntity.ok(CollectionModel.of(categorias,
                linkTo(methodOn(CategoriaController.class).getAllCategorias()).withSelfRel()));
    }

    @GetMapping("/categorias/{idCategoria}")
    public ResponseEntity<EntityModel<Categoria>> getCategoriaById(@PathVariable("idCategoria") Long idCategoria) {
        log.info("Buscando categoria con ID: {}", idCategoria);
        Optional<Categoria> categoriaOpt = categoriaService.getCategoriaById(idCategoria);
        if (categoriaOpt.isPresent()) {
            return ResponseEntity.ok(toModel(categoriaOpt.get()));
        }

        throw new ResourceNotFoundException("No se encontro la Categoria con el id: " + idCategoria);
    }

    @PostMapping("/categorias")
    public ResponseEntity<Categoria> createCategoria(@Valid @RequestBody Categoria categoria) {
        log.info("Creando una nueva categoria: {}", categoria.getNombre());
        Categoria savedCategoria = categoriaService.saveCategoria(categoria);
        return new ResponseEntity<>(savedCategoria, HttpStatus.CREATED);
    }

    @PutMapping("/categorias/{idCategoria}")
    public ResponseEntity<Categoria> updateCategoria(@PathVariable("idCategoria") Long idCategoria, @Valid @RequestBody Categoria categoria) {
        log.info("Actualizando categoria con ID: {}", idCategoria);
        return ResponseEntity.ok(categoriaService.updateCategoria(idCategoria, categoria));
    }

    @DeleteMapping("/categorias/{idCategoria}")
    public ResponseEntity<Void> deleteCategoria(@PathVariable("idCategoria") Long idCategoria) {
        log.info("Eliminando categoria con ID: {}", idCategoria);
        categoriaService.deleteCategoriaById(idCategoria);
        return ResponseEntity.noContent().build();
    }

    private EntityModel<Categoria> toModel(Categoria categoria) {
        return EntityModel.of(categoria,
                linkTo(methodOn(CategoriaController.class).getCategoriaById(categoria.getId())).withSelfRel(),
                linkTo(methodOn(CategoriaController.class).getAllCategorias()).withRel("categorias"),
                linkTo(methodOn(TemaController.class).getTemasPorCategoria(categoria.getId())).withRel("temas"));
    }
}
