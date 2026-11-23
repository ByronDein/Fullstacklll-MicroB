package com.example.foro.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.foro.model.Comentario;
import com.example.foro.service.ComentarioService;

import jakarta.validation.Valid;

@RestController
public class ComentarioController {

    private static final Logger log = LoggerFactory.getLogger(ComentarioController.class);

    @Autowired
    private ComentarioService comentarioService;

    @GetMapping("/temas/{idTema}/comentarios")
    public ResponseEntity<List<Comentario>> getComentariosPorTema(@PathVariable("idTema") Long idTema) {
        log.info("Obteniendo comentarios del tema ID: {}", idTema);
        return ResponseEntity.ok(comentarioService.getComentariosPorTema(idTema));
    }

    @PostMapping("/temas/{idTema}/comentarios")
    public ResponseEntity<Comentario> crearComentario(@PathVariable("idTema") Long idTema, @Valid @RequestBody Comentario comentario) {
        log.info("Creando nuevo comentario en el tema ID: {}", idTema);
        Comentario nuevoComentario = comentarioService.crearComentario(idTema, comentario);
        return new ResponseEntity<>(nuevoComentario, HttpStatus.CREATED);
    }

    @PutMapping("/comentarios/{idComentario}")
    public ResponseEntity<Comentario> actualizarComentario(@PathVariable("idComentario") Long idComentario, @Valid @RequestBody Comentario comentario) {
        log.info("Actualizando comentario ID: {}", idComentario);
        return ResponseEntity.ok(comentarioService.actualizarComentario(idComentario, comentario));
    }

    @DeleteMapping("/comentarios/{idComentario}")
    public ResponseEntity<Void> eliminarComentario(@PathVariable("idComentario") Long idComentario) {
        log.info("Eliminando comentario ID: {}", idComentario);
        comentarioService.eliminarComentario(idComentario);
        return ResponseEntity.noContent().build();
    }
}
