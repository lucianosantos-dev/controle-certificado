package com.lucianodev.controlecertificado.services;

import com.lucianodev.controlecertificado.config.TokenService;
import com.lucianodev.controlecertificado.dtos.auth.LoginRequestDto;
import com.lucianodev.controlecertificado.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AutenticacaoService {

    private final UsuarioRepository repository;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;

    public String autenticar(LoginRequestDto loginRequestDto){
        var usuario = repository.findByUsername(loginRequestDto.login())
                .orElseThrow(
                        ()-> new BadCredentialsException("Usuário ou senha inválidos")
                );

        if (!passwordEncoder.matches(loginRequestDto.senha(), usuario.getSenha())){
            throw new BadCredentialsException("Usuário ou senha inválidos");
        }
        return tokenService.generateToken(usuario);
    }
}
