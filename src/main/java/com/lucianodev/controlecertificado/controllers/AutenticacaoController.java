package com.lucianodev.controlecertificado.controllers;

import com.lucianodev.controlecertificado.dtos.auth.LoginRequestDto;
import com.lucianodev.controlecertificado.dtos.auth.TokenResponseDto;
import com.lucianodev.controlecertificado.services.AutenticacaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AutenticacaoController {


    private final AutenticacaoService service;

    @PostMapping("/auth/login")
    public ResponseEntity<TokenResponseDto> login(@RequestBody @Valid LoginRequestDto dto) {
        String token = service.autenticar(dto);
        return ResponseEntity.ok(new TokenResponseDto(token));
    }
}
