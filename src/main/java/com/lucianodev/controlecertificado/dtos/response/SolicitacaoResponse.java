package com.lucianodev.controlecertificado.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SolicitacaoResponse {

    private String nomeAluno;
    private LocalDate dataLimiteEntrega;
}
