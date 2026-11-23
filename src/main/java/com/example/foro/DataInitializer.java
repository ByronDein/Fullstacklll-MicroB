package com.example.foro;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.foro.model.Categoria;
import com.example.foro.model.Comentario;
import com.example.foro.model.Tema;
import com.example.foro.repository.CategoriaRepository;
import com.example.foro.repository.ComentarioRepository;
import com.example.foro.repository.TemaRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private TemaRepository temaRepository;

    @Autowired
    private ComentarioRepository comentarioRepository;

    @Override
    public void run(String... args) {
        if (categoriaRepository.count() > 0) {
            System.out.println("Datos del foro ya existen. No se reinicializa la base.");
            return;
        }

        Categoria c1 = crearCategoria("Tecnologia", "Noticias, programacion y gadgets");
        Categoria c2 = crearCategoria("Videojuegos", "Todo sobre consolas y juegos de PC");
        Categoria c3 = crearCategoria("Deportes", "Futbol, tenis y mas");
        Categoria c4 = crearCategoria("Musica", "Artistas, discos y conciertos");
        Categoria c5 = crearCategoria("Cine y Series", "Estrenos, criticas y recomendaciones");

        // los autores (1, 2 y 3) son los usuarios que crea el microservicio de usuarios
        Tema t1 = new Tema();
        t1.setTitulo("Cual es el mejor lenguaje para aprender backend?");
        t1.setContenido("Estoy partiendo en programacion y no se si irme por Java, Python o Node.");
        t1.setAutorId(3L);
        t1.setFechaCreacion(LocalDateTime.now());
        t1.setCategoria(c1);

        Tema t2 = new Tema();
        t2.setTitulo("Que juegos recomiendan para este fin de semana?");
        t2.setContenido("Busco algo para jugar con amigos, de preferencia cooperativo.");
        t2.setAutorId(2L);
        t2.setFechaCreacion(LocalDateTime.now());
        t2.setCategoria(c2);

        Tema t3 = new Tema();
        t3.setTitulo("Prediccion para la fecha del campeonato");
        t3.setContenido("Quien creen que va a ganar el clasico este fin de semana?");
        t3.setAutorId(1L);
        t3.setFechaCreacion(LocalDateTime.now());
        t3.setCategoria(c3);

        temaRepository.save(t1);
        temaRepository.save(t2);
        temaRepository.save(t3);

        crearComentario(t1, 1L, "Yo te recomiendo Java con Spring Boot, es lo que se usa harto en empresas.");
        crearComentario(t2, 3L, "Prueba It Takes Two, es muy bueno para jugar de a dos.");
        crearComentario(t3, 2L, "Yo creo que gana el local, juega mucho mejor en casa.");

        System.out.println("Datos del foro inicializados con exito.");
    }

    private Categoria crearCategoria(String nombre, String descripcion) {
        Categoria categoria = new Categoria();
        categoria.setNombre(nombre);
        categoria.setDescripcion(descripcion);
        return categoriaRepository.save(categoria);
    }

    private void crearComentario(Tema tema, Long autorId, String contenido) {
        Comentario comentario = new Comentario();
        comentario.setTema(tema);
        comentario.setAutorId(autorId);
        comentario.setContenido(contenido);
        comentario.setFechaCreacion(LocalDateTime.now());
        comentarioRepository.save(comentario);
    }
}
