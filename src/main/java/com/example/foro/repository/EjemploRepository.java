package com.example.foro.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.foro.model.Ejemplo;

@Repository
public interface EjemploRepository extends JpaRepository<Ejemplo, Long> {
}
