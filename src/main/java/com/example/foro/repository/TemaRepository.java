package com.example.foro.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.foro.model.Tema;

@Repository
public interface TemaRepository extends JpaRepository<Tema, Long> {

    List<Tema> findByCategoriaId(Long idCategoria);

    List<Tema> findByTituloContainingIgnoreCaseOrContenidoContainingIgnoreCase(String titulo, String contenido);
}
