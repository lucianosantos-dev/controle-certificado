package com.lucianodev.controlecertificado.repositories;

import com.lucianodev.controlecertificado.entities.Curso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CursoRepository extends JpaRepository<Curso, Long> {
    boolean existsByNomeIgnoreCase(String nome);
}
