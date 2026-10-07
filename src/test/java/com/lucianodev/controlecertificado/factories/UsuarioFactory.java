package com.lucianodev.controlecertificado.factories;

import com.lucianodev.controlecertificado.dtos.request.UsuarioRequest;
import com.lucianodev.controlecertificado.entities.Usuario;

public class UsuarioFactory {

    public static UsuarioRequest usuarioRequest() {
        UsuarioRequest usuarioRequest = new UsuarioRequest();
        usuarioRequest.setNome("Luciano Santos");
        usuarioRequest.setEmail("luciano@email.com");
        usuarioRequest.setSenha("068732@");
        usuarioRequest.setUsername("lusantos");
        return usuarioRequest;
    }

    public static Usuario usuarioEntity() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome(usuarioRequest().getNome());
        usuario.setEmail(usuarioRequest().getEmail());
        usuario.setSenha(usuarioRequest().getSenha());
        usuario.setUsername(usuarioRequest().getUsername());
        return usuario;
    }
}
