package com.lucianodev.controlecertificado.controllers;

import com.lucianodev.controlecertificado.dtos.request.CursoRequest;
import com.lucianodev.controlecertificado.dtos.response.CursoResponse;
import com.lucianodev.controlecertificado.services.CursoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin("*")
@Tag(name = "Cursos", description = "Endpoints para o fluxo de curso")
public class CursoController {

    private final CursoService service;

    @PostMapping("/cursos")
    @PreAuthorize("hasAnyAuthority('SECRETARIA', 'PEDAGOGICO')")
    @Operation(summary = "Cria um novo curso", description = "Permite que um admin crie um novo curso para ser selecionado no momento da solicitação feita por um aluno")
    @ApiResponse(responseCode = "201", description = "Curso criado com sucesso")
    @ApiResponse(responseCode = "400", description = "Erro de validação nos campos")
    @ApiResponse(responseCode = "409", description = "Curso já possui cadastro")
    public ResponseEntity<CursoResponse> novoCurso(@RequestBody @Valid CursoRequest request) {
        CursoResponse response = service.create(request);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .path("/{id}")
                .buildAndExpand(response.getId())
                .toUri();

        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping("/cursos/{id}")
    @PreAuthorize("hasAnyAuthority('SECRETARIA', 'PEDAGOGICO')")
    @Operation(summary = "Recupera um curso pelo ID", description = "Permite que um admin do sistema recupere um curso pelo ID")
    @ApiResponse(responseCode = "200", description = "Retorna sucesso ao recuperar um curso pelo ID")
    @ApiResponse(responseCode = "404", description = "Curso não encontrado com id fornecido")
    public ResponseEntity<CursoResponse> buscarPeloId(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping("/cursos")
    @Operation(summary = "Retorna uma lista de cursos", description = "Permite retornar uma lista de curso para um admin ou um aluno no momento da solicitação")
    @ApiResponse(responseCode = "200", description = "Retorna sucesso ao recuperar todos os cursos")
    public ResponseEntity<List<CursoResponse>> listarTodos() {
        return ResponseEntity.ok(service.findAll());
    }

    @DeleteMapping("/cursos/{id}")
    @PreAuthorize("hasAnyAuthority('SECRETARIA', 'PEDAGOGICO')")
    @Operation(summary = "Deleta um curso informando o ID", description = "Permite fazer a deleção de um curso por um admin do sistema")
    @ApiResponse(responseCode = "204", description = "Executa a ação sem retorno")
    @ApiResponse(responseCode = "404", description = "Curso não encontrado com id fornecido")
    @ApiResponse(responseCode = "409", description = "Possui solicitações vinculadas ao curso selecionado para deleção")
    public ResponseEntity<Void> deletarPorId(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
