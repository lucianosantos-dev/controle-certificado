package com.lucianodev.controlecertificado.controllers;

import com.lucianodev.controlecertificado.dtos.request.AtualizarStatusRequest;
import com.lucianodev.controlecertificado.dtos.request.SolicitacaoRequest;
import com.lucianodev.controlecertificado.dtos.response.SolicitacaoListagemResponse;
import com.lucianodev.controlecertificado.dtos.response.SolicitacaoResponse;
import com.lucianodev.controlecertificado.services.SolicitacaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
public class SolicitacaoController {

    private final SolicitacaoService solicitacaoService;


    @PostMapping("/solicitacoes")
    public ResponseEntity<SolicitacaoResponse> save(@RequestBody @Valid SolicitacaoRequest request) {
        SolicitacaoResponse response = solicitacaoService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/solicitacoes/minhas")
    public ResponseEntity<List<SolicitacaoListagemResponse>> listarMinhas() {
        var list = solicitacaoService.listarMinhasSolicitacoes();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/solicitacoes")
    @PreAuthorize("hasAnyRole('SECRETARIA', 'PEDAGOGICO')")
    public ResponseEntity<Page<SolicitacaoListagemResponse>> findAll(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String cpf,
            @PageableDefault(size = 10, sort = {"dataLimiteEntrega"}) Pageable pageable
    ) {
        var list = solicitacaoService.findAll(nome, cpf, pageable);
        return ResponseEntity.ok(list);
    }

    @PatchMapping("/solicitacoes/{id}/status")
    @PreAuthorize("hasAnyRole('SECRETARIA', 'PEDAGOGICO')")
    public ResponseEntity<Void> atualizarStatus(
            @PathVariable Long id,
            @RequestBody @Valid AtualizarStatusRequest request
    ) {
        solicitacaoService.atualizarStatus(id, request);
        return ResponseEntity.noContent().build();
    }
}
