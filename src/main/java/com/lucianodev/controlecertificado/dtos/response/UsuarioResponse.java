package com.lucianodev.controlecertificado.dtos.response;

import com.lucianodev.controlecertificado.enums.Perfil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UsuarioResponse {

    private Long id;
    private String nome;
    private String email;
    private String username;
    private Perfil perfil;
}
