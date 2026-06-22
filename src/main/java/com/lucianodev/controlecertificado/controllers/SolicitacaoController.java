package com.lucianodev.controlecertificado.controllers;

import com.lucianodev.controlecertificado.dtos.request.AtualizarStatusRequest;
import com.lucianodev.controlecertificado.dtos.request.SolicitacaoRequest;
import com.lucianodev.controlecertificado.dtos.response.SolicitacaoListagemResponse;
import com.lucianodev.controlecertificado.dtos.response.SolicitacaoResponse;
import com.lucianodev.controlecertificado.services.SolicitacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@CrossOrigin("*")
@Tag(name = "Solicitações", description = "Endpoints para o fluxo de pedido e gestão de certificados")
public class SolicitacaoController {

    private final SolicitacaoService solicitacaoService;


    @PostMapping("/solicitacoes")
    @Operation(summary = "Cria uma nova solicitação", description = "Permite que um aluno logado solicite a emissão de um certificado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Solicitação criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de validação nos campos"),
            @ApiResponse(responseCode = "409", description = "O aluno já possui solicitação para este curso")
    })
    public ResponseEntity<SolicitacaoResponse> save(@RequestBody @Valid SolicitacaoRequest request) {
        SolicitacaoResponse response = solicitacaoService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/solicitacoes/minhas")
    @Operation(summary = "Lista as solicitações do aluno logado", description =
            "Retorna um array com os pedidos vinculados ao token do usuário atual.")
    public ResponseEntity<List<SolicitacaoListagemResponse>> listarMinhas() {
        var list = solicitacaoService.listarMinhasSolicitacoes();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/solicitacoes")
    @PreAuthorize("hasAnyAuthority('SECRETARIA', 'PEDAGOGICO')")
    @Operation(summary = "Lista todas as solicitações (Admin)", description =
            "Retorna de forma paginada todas as solicitações do sistema. Permite buscar por nome ou CPF do aluno.")
    public ResponseEntity<Page<SolicitacaoListagemResponse>> findAll(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String cpf,
            @PageableDefault(size = 10, sort = {"dataLimiteEntrega"}) Pageable pageable
    ) {
        var list = solicitacaoService.findAll(nome, cpf, pageable);
        return ResponseEntity.ok(list);
    }

    @PatchMapping("/solicitacoes/{id}/status")
    @PreAuthorize("hasAnyAuthority('SECRETARIA', 'PEDAGOGICO')")
    @Operation(summary = "Atualiza o status da solicitação (Admin)", description =
            "Evolui o status da solicitação seguindo o fluxo obrigatório: PENDENTE -> CONCLUIDO -> ENTREGUE.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Status atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Solicitação não encontrada no banco de dados"),
            @ApiResponse(responseCode = "409", description = "Regra da máquina de estados violada (ex: tentar alterar um status já ENTREGUE ou pular etapas)")
    })
    public ResponseEntity<Void> atualizarStatus(
            @PathVariable Long id,
            @RequestBody @Valid AtualizarStatusRequest request
    ) {
        solicitacaoService.atualizarStatus(id, request);
        return ResponseEntity.noContent().build();
    }
}
