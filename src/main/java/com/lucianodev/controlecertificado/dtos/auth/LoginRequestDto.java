package com.lucianodev.controlecertificado.dtos.auth;

import jakarta.validation.constraints.NotBlank;

public record LoginRequestDto(

        @NotBlank(message = "O campo login não pode estar vazio.")
        String login,

        @NotBlank(message = "O campo senha não pode estar vazio.")
        String senha
) {}
