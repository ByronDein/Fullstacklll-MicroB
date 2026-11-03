package com.example.foro.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.foro.client.UsuarioClient;
import com.example.foro.exception.ResourceNotFoundException;
import com.example.foro.model.Categoria;
import com.example.foro.model.Tema;
import com.example.foro.repository.CategoriaRepository;
import com.example.foro.repository.TemaRepository;

@Service
public class TemaServiceImpl implements TemaService {

    @Autowired
    private TemaRepository temaRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private UsuarioClient usuarioClient;

    @Override
    public List<Tema> getAllTemas() {
        return temaRepository.findAll();
    }

    @Override
    public Optional<Tema> getTemaById(Long id) {
        return temaRepository.findById(id);
    }

    @Override
    public List<Tema> getTemasPorCategoria(Long idCategoria) {
        if (!categoriaRepository.existsById(idCategoria)) {
            throw new ResourceNotFoundException("No se encontro la Categoria con el id: " + idCategoria);
        }
        return temaRepository.findByCategoriaId(idCategoria);
    }

    @Override
    public List<Tema> buscarTemas(String texto) {
        return temaRepository.findByTituloContainingIgnoreCaseOrContenidoContainingIgnoreCase(texto, texto);
    }

    @Override
    public Tema crearTema(Long idCategoria, Tema tema) {
        Optional<Categoria> categoriaOpt = categoriaRepository.findById(idCategoria);
        if (categoriaOpt.isEmpty()) {
            throw new ResourceNotFoundException("No se encontro la Categoria con el id: " + idCategoria);
        }

        // el autor tiene que existir en el microservicio de usuarios
        usuarioClient.validarUsuario(tema.getAutorId());

        tema.setId(null);
        tema.setCategoria(categoriaOpt.get());
        tema.setFechaCreacion(LocalDateTime.now());
        return temaRepository.save(tema);
    }

    @Override
    public Tema actualizarTema(Long id, Tema temaActualizado) {
        Optional<Tema> temaOpt = temaRepository.findById(id);
        if (temaOpt.isEmpty()) {
            throw new ResourceNotFoundException("No se pudo actualizar el Tema. No existe el id: " + id);
        }

        Tema temaExistente = temaOpt.get();
        temaExistente.setTitulo(temaActualizado.getTitulo());
        temaExistente.setContenido(temaActualizado.getContenido());

        // la categoria solo cambia si viene en el body
        if (temaActualizado.getCategoria() != null && temaActualizado.getCategoria().getId() != null) {
            Long idCategoria = temaActualizado.getCategoria().getId();
            Categoria categoria = categoriaRepository.findById(idCategoria)
                    .orElseThrow(() -> new ResourceNotFoundException("No se encontro la Categoria con el id: " + idCategoria));
            temaExistente.setCategoria(categoria);
        }

        return temaRepository.save(temaExistente);
    }

    @Override
    public void eliminarTema(Long id) {
        if (!temaRepository.existsById(id)) {
            throw new ResourceNotFoundException("No se pudo eliminar el Tema. No existe el id: " + id);
        }
        // al borrar el tema se borran tambien sus comentarios (cascade)
        temaRepository.deleteById(id);
    }
}
