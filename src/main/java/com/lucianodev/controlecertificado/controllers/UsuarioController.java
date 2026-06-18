package com.lucianodev.controlecertificado.controllers;

import com.lucianodev.controlecertificado.dtos.request.UsuarioRequest;
import com.lucianodev.controlecertificado.dtos.response.UsuarioResponse;
import com.lucianodev.controlecertificado.services.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Tag(name = "Usuários e Autenticação", description
        = "Endpoints para gestão de contas e geração do token JWT")
@RestController
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService service;


    @PostMapping("/usuarios")
    @Operation(summary = "Cadastra um novo usuário", description =
            "Cria uma conta no sistema. O e-mail e o username devem ser únicos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de validação nos dados enviados"),
            @ApiResponse(responseCode = "409", description = "E-mail ou Username já cadastrados no banco de dados")
    })
    public ResponseEntity<UsuarioResponse> save(@RequestBody @Valid UsuarioRequest request) {
        UsuarioResponse response = service.save(request);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(response.getId()).toUri();

        return ResponseEntity.created(uri).body(response);
    }


    @GetMapping("/usuarios/{username}")
    @Operation(summary = "Busca um usuário pelo username", description =
            "Retorna os detalhes de um usuário específico pesquisando pelo seu nome de usuário.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário encontrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado no sistema")
    })
    public ResponseEntity<UsuarioResponse> findByUserName(@PathVariable String username) {
        return ResponseEntity.ok(service.findByUsername(username));
    }
}
