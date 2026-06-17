package com.lucianodev.controlecertificado.dtos.request;

import com.lucianodev.controlecertificado.enums.TipoCertificado;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SolicitacaoRequest {

    @NotBlank(message = "O nome do aluno é obrigatório")
    private String nomeAluno;

    @NotBlank(message = "O curso é obrigatório")
    private String curso;

    @NotNull(message = "A data de conclusão é obrigatória")
    private LocalDate dataConclusao;

    @NotBlank(message = "O CPF é obrigatório")
    private String cpf;

    @NotBlank(message = "O telefone é obrigatório")
    private String telefone;

    @NotNull(message = "O formato de entrega é obrigatório")
    private TipoCertificado tipoCertificado;

}
