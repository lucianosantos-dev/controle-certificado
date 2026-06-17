package com.lucianodev.controlecertificado.dtos.request;

import com.lucianodev.controlecertificado.enums.StatusSolicitacao;
import jakarta.validation.constraints.NotNull;

public record AtualizarStatusRequest(
        @NotNull(message = "O novo status é obrigatório")
        StatusSolicitacao status
) {
}