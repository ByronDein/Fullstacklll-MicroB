package com.example.foro.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.foro.exception.ResourceNotFoundException;
import com.example.foro.model.Categoria;
import com.example.foro.repository.CategoriaRepository;
import com.example.foro.repository.TemaRepository;

@Service
public class CategoriaServiceImpl implements CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private TemaRepository temaRepository;

    @Override
    public List<Categoria> getAllCategorias() {
        return categoriaRepository.findAll();
    }

    @Override
    public Optional<Categoria> getCategoriaById(Long id) {
        return categoriaRepository.findById(id);
    }

    @Override
    public Categoria saveCategoria(Categoria categoria) {
        if (categoriaRepository.existsByNombre(categoria.getNombre())) {
            throw new RuntimeException("Ya existe una categoria con el nombre: " + categoria.getNombre());
        }
        categoria.setId(null);
        return categoriaRepository.save(categoria);
    }

    @Override
    public Categoria updateCategoria(Long id, Categoria categoriaActualizada) {
        Optional<Categoria> categoriaOpt = categoriaRepository.findById(id);
        if (categoriaOpt.isEmpty()) {
            throw new ResourceNotFoundException("No se pudo actualizar la Categoria. No existe el id: " + id);
        }

        Categoria categoriaExistente = categoriaOpt.get();

        if (!categoriaExistente.getNombre().equals(categoriaActualizada.getNombre())
                && categoriaRepository.existsByNombre(categoriaActualizada.getNombre())) {
            throw new RuntimeException("Ya existe una categoria con el nombre: " + categoriaActualizada.getNombre());
        }

        categoriaExistente.setNombre(categoriaActualizada.getNombre());
        categoriaExistente.setDescripcion(categoriaActualizada.getDescripcion());
        return categoriaRepository.save(categoriaExistente);
    }

    @Override
    public void deleteCategoriaById(Long id) {
        if (!categoriaRepository.existsById(id)) {
            throw new ResourceNotFoundException("No se pudo eliminar la Categoria. No existe el id: " + id);
        }
        // no dejamos borrar categorias que todavia tienen temas
        if (!temaRepository.findByCategoriaId(id).isEmpty()) {
            throw new RuntimeException("No se puede eliminar la categoria porque tiene temas publicados");
        }
        categoriaRepository.deleteById(id);
    }
}
