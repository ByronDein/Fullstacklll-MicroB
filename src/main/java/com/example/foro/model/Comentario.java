package com.example.foro.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "comentarios")
public class Comentario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "tema_id", nullable = false)
    @JsonIgnore
    private Tema tema;

    @NotBlank(message = "El contenido del comentario es obligatorio")
    @Column(name = "contenido", length = 1000)
    private String contenido;

    // id del usuario que comenta (vive en el microservicio de usuarios)
    @Column(name = "autor_id", nullable = false)
    private Long autorId;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    public Long getId() {
        return id;
    }

    public Tema getTema() {
        return tema;
    }

    public String getContenido() {
        return contenido;
    }

    public Long getAutorId() {
        return autorId;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTema(Tema tema) {
        this.tema = tema;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public void setAutorId(Long autorId) {
        this.autorId = autorId;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

}
