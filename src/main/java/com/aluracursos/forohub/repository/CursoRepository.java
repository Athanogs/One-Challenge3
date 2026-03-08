package com.aluracursos.forohub.repository;

import com.aluracursos.forohub.domain.curso.Curso;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CursoRepository extends JpaRepository<Curso, Long> {
}