package com.lucianodev.controlecertificado.services;

import com.lucianodev.controlecertificado.dtos.request.CursoRequest;
import com.lucianodev.controlecertificado.dtos.response.CursoResponse;
import com.lucianodev.controlecertificado.entities.Curso;
import com.lucianodev.controlecertificado.exceptions.ConflictException;
import com.lucianodev.controlecertificado.exceptions.ResourceNotFoundException;
import com.lucianodev.controlecertificado.mapper.CursoMapper;
import com.lucianodev.controlecertificado.repositories.CursoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CursoService {

    private final CursoMapper mapper;
    private final CursoRepository repository;


    @Transactional
    public CursoResponse create(CursoRequest request) {
        if (repository.existsByNomeIgnoreCase(request.getNome())) {
            throw new ConflictException("Você possui um curso cadastrado com esse nome: " + request.getNome());
        }

        Curso criado = mapper.toEntity(request);
        return mapper.toResponse(repository.save(criado));
    }

    @Transactional(readOnly = true)
    public CursoResponse findById(Long id) {
        Curso curso = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Curso não encontrado com o código informado: " + id));
        return mapper.toResponse(curso);
    }

    @Transactional(readOnly = true)
    public List<CursoResponse> findAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Transactional
    public void deleteById(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Curso não encontrado.");
        }
        try {
            repository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException("Esse não pode ser excluído pois já existem solicitações vinculadas a ele.");
        }
    }
}
