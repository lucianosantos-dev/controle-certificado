package com.lucianodev.controlecertificado.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CursoRequest {

    @Size(min = 5, message = "O nome do Curso deve conter no mínimo 5 caracteres")
    @NotBlank(message = "O nome do curso é obrigatório")
    String nome;
}
