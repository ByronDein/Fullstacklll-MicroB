package com.example.foro.service;

import java.util.List;

import com.example.foro.model.Comentario;

public interface ComentarioService {

    List<Comentario> getComentariosPorTema(Long idTema);

    Comentario crearComentario(Long idTema, Comentario comentario);

    Comentario actualizarComentario(Long id, Comentario comentario);

    void eliminarComentario(Long id);
}
