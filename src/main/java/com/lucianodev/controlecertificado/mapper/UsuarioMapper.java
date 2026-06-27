package com.lucianodev.controlecertificado.mapper;

import com.lucianodev.controlecertificado.dtos.request.UsuarioRequest;
import com.lucianodev.controlecertificado.dtos.response.UsuarioResponse;
import com.lucianodev.controlecertificado.entities.Usuario;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {

    public Usuario toEntity(UsuarioRequest request) {
        return Usuario.builder()
                .nome(request.getNome())
                .email(request.getEmail())
                .username(request.getUsername())
                .senha(request.getSenha())
                .build();
    }

    public UsuarioResponse toResponse(Usuario entity) {
        return UsuarioResponse.builder()
                .id(entity.getId())
                .nome(entity.getNome())
                .email(entity.getEmail())
                .username(entity.getUsername())
                .perfil(entity.getPerfil())
                .build();
    }
}
