package com.example.foro.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.foro.client.UsuarioClient;
import com.example.foro.exception.ResourceNotFoundException;
import com.example.foro.model.Comentario;
import com.example.foro.model.Tema;
import com.example.foro.repository.ComentarioRepository;
import com.example.foro.repository.TemaRepository;

@Service
public class ComentarioServiceImpl implements ComentarioService {

    @Autowired
    private ComentarioRepository comentarioRepository;

    @Autowired
    private TemaRepository temaRepository;

    @Autowired
    private UsuarioClient usuarioClient;

    @Override
    public List<Comentario> getComentariosPorTema(Long idTema) {
        if (!temaRepository.existsById(idTema)) {
            throw new ResourceNotFoundException("No se encontro el Tema con el id: " + idTema);
        }
        return comentarioRepository.findByTemaId(idTema);
    }

    @Override
    public Comentario crearComentario(Long idTema, Comentario comentario) {
        Optional<Tema> temaOpt = temaRepository.findById(idTema);
        if (temaOpt.isEmpty()) {
            throw new ResourceNotFoundException("No se encontro el Tema con el id: " + idTema);
        }

        // el autor tiene que existir en el microservicio de usuarios
        usuarioClient.validarUsuario(comentario.getAutorId());

        comentario.setId(null);
        comentario.setTema(temaOpt.get());
        comentario.setFechaCreacion(LocalDateTime.now());
        return comentarioRepository.save(comentario);
    }

    @Override
    public Comentario actualizarComentario(Long id, Comentario comentarioActualizado) {
        Optional<Comentario> comentarioOpt = comentarioRepository.findById(id);
        if (comentarioOpt.isEmpty()) {
            throw new ResourceNotFoundException("No se pudo actualizar el Comentario. No existe el id: " + id);
        }

        Comentario comentarioExistente = comentarioOpt.get();
        comentarioExistente.setContenido(comentarioActualizado.getContenido());
        return comentarioRepository.save(comentarioExistente);
    }

    @Override
    public void eliminarComentario(Long id) {
        if (!comentarioRepository.existsById(id)) {
            throw new ResourceNotFoundException("No se pudo eliminar el Comentario. No existe el id: " + id);
        }
        comentarioRepository.deleteById(id);
    }
}
