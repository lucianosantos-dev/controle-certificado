package com.lucianodev.controlecertificado.controllers;

import com.lucianodev.controlecertificado.dtos.auth.LoginRequestDto;
import com.lucianodev.controlecertificado.dtos.auth.TokenResponseDto;
import com.lucianodev.controlecertificado.services.AutenticacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@CrossOrigin("*")
@Tag(name = "Autenticação", description = "Endpoints para login e segurança do sistema")
public class AutenticacaoController {


    private final AutenticacaoService service;

    @PostMapping("/auth/login")
    @Operation(summary = "Realiza a autenticação", description =
            "Recebe as credenciais do usuário, valida e retorna o token JWT necessário para acessar as rotas protegidas.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login realizado com sucesso. Token gerado."),
            @ApiResponse(responseCode = "400", description = "Erro de validação (campos vazios ou nulos)"),
            @ApiResponse(responseCode = "401", description = "Credenciais inválidas ou usuário não autorizado")
    })
    public ResponseEntity<TokenResponseDto> login(@RequestBody @Valid LoginRequestDto dto) {
        String token = service.autenticar(dto);
        return ResponseEntity.ok(new TokenResponseDto(token));
    }

}
