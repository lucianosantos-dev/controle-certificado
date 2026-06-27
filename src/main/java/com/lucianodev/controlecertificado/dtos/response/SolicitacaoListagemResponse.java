package com.lucianodev.controlecertificado.dtos.response;

import com.lucianodev.controlecertificado.enums.StatusSolicitacao;
import com.lucianodev.controlecertificado.enums.TipoCertificado;

import java.time.LocalDate;

public record SolicitacaoListagemResponse(
        Long id,
        String nomeAluno,
        String curso,
        String cpf,
        LocalDate dataSolicitacao,
        LocalDate dataLimiteEntrega,
        StatusSolicitacao statusSolicitacao,
        TipoCertificado tipoCertificado
) {
}
