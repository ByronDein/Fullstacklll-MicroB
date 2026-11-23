package com.example.foro.service;

import java.util.List;
import java.util.Optional;

import com.example.foro.model.Categoria;

public interface CategoriaService {

    List<Categoria> getAllCategorias();

    Optional<Categoria> getCategoriaById(Long id);

    Categoria saveCategoria(Categoria categoria);

    Categoria updateCategoria(Long id, Categoria categoria);

    void deleteCategoriaById(Long id);
}
