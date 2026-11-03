package com.example.foro.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import com.example.foro.exception.ResourceNotFoundException;

// Se comunica con el microservicio de usuarios para saber si el autor existe
@Component
public class UsuarioClient {

    private static final Logger log = LoggerFactory.getLogger(UsuarioClient.class);

    @Autowired
    private RestTemplate restTemplate;

    @Value("${usuarios.service.url}")
    private String usuariosUrl;

    public void validarUsuario(Long idUsuario) {
        if (idUsuario == null) {
            throw new RuntimeException("El id del autor (autorId) es obligatorio");
        }
        log.info("Validando usuario {} en el microservicio de usuarios", idUsuario);
        try {
            restTemplate.getForObject(usuariosUrl + "/usuarios/" + idUsuario, String.class);
        } catch (HttpClientErrorException.NotFound e) {
            throw new ResourceNotFoundException("No existe el usuario (autor) con el id: " + idUsuario);
        } catch (ResourceAccessException e) {
            throw new RuntimeException("No se pudo conectar con el microservicio de usuarios en " + usuariosUrl);
        }
    }
}
