package com.lucianodev.controlecertificado.mapper;

import com.lucianodev.controlecertificado.dtos.request.SolicitacaoRequest;
import com.lucianodev.controlecertificado.dtos.response.SolicitacaoListagemResponse;
import com.lucianodev.controlecertificado.dtos.response.SolicitacaoResponse;
import com.lucianodev.controlecertificado.entities.Solicitacao;
import org.springframework.stereotype.Component;

@Component
public class SolicitacaoMapper {

    public Solicitacao toEntity(SolicitacaoRequest request) {
        return Solicitacao.builder()
                .nomeAluno(request.getNomeAluno())
                .curso(request.getCurso())
                .dataConclusao(request.getDataConclusao())
                .cpf(request.getCpf())
                .telefone(request.getTelefone())
                .tipoCertificado(request.getTipoCertificado())
                .build();
    }

    public SolicitacaoResponse toResponse(Solicitacao entity) {
        return SolicitacaoResponse.builder()
                .nomeAluno(entity.getNomeAluno())
                .dataLimiteEntrega(entity.getDataLimiteEntrega())
                .build();
    }

    public SolicitacaoListagemResponse toListResponse(Solicitacao entity) {
        return new SolicitacaoListagemResponse(
                entity.getId(),
                entity.getNomeAluno(),
                entity.getCurso(),
                entity.getCpf(),
                entity.getDataSolicitacao(),
                entity.getDataLimiteEntrega(),
                entity.getStatusSolicitacao()
        );
    }
}
