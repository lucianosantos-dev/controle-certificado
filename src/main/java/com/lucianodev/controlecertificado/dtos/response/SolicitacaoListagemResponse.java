package com.lucianodev.controlecertificado.dtos.response;

import com.lucianodev.controlecertificado.enums.StatusSolicitacao;

import java.time.LocalDate;

public record SolicitacaoListagemResponse(
        Long id,
        String nomeAluno,
        String curso,
        LocalDate dataSolicitacao,
        LocalDate dataLimiteEntrega,
        StatusSolicitacao statusSolicitacao
) {
}
