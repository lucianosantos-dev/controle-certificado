package com.lucianodev.controlecertificado.dtos.request;

import com.lucianodev.controlecertificado.enums.Perfil;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UsuarioRequest {

    @NotNull(message = "Nome não pode ser nullo")
    @Size(max = 50)
    private String nome;

    @NotNull(message = "Email não pode ser nullo")
    @Size(max = 100)
    private String email;

    @NotNull(message = "Username não pode ser nullo")
    @Size(max = 50)
    private String username;

    @NotNull(message = "Senha não pode ser nullo")
    @Size(max = 150)
    private String senha;

    @NotNull(message = "Perfil não pode ser nullo")
    private Perfil perfil;

}
