package com.example.foro.service;

import java.util.List;
import java.util.Optional;

import com.example.foro.model.Tema;

public interface TemaService {

    List<Tema> getAllTemas();

    Optional<Tema> getTemaById(Long id);

    List<Tema> getTemasPorCategoria(Long idCategoria);

    List<Tema> buscarTemas(String texto);

    Tema crearTema(Long idCategoria, Tema tema);

    Tema actualizarTema(Long id, Tema tema);

    void eliminarTema(Long id);
}
