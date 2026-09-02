package com.lucianodev.controlecertificado.dtos.request;

import com.lucianodev.controlecertificado.enums.TipoCertificado;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SolicitacaoRequest {

    @NotBlank(message = "O nome do aluno é obrigatório.")
    private String nomeAluno;

    @NotNull(message = "A data de conclusão é obrigatória")
    @PastOrPresent(message = "A data de conclusão não pode ser uma data futura.")
    private LocalDate dataConclusao;

    @NotBlank(message = "O CPF é obrigatório.")
    @CPF(message = "CPF inválido")
    private String cpf;

    @NotBlank(message = "O telefone é obrigatório.")
    @Pattern(regexp = "\\(\\d{2}\\)\\s\\d{4,5}-\\d{4}", message = "O telefone deve estar no formato (99) 99999-9999")
    private String telefone;

    @NotNull(message = "O formato de entrega é obrigatório.")
    private TipoCertificado tipoCertificado;

    @NotNull(message = "Selecione um curso válido")
    private Long cursoId;

}
