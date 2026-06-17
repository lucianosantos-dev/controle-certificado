package com.lucianodev.controlecertificado.controllers;

import com.lucianodev.controlecertificado.dtos.request.UsuarioRequest;
import com.lucianodev.controlecertificado.dtos.response.UsuarioResponse;
import com.lucianodev.controlecertificado.services.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService service;


    @PostMapping("/usuarios")
    public ResponseEntity<UsuarioResponse> save(@RequestBody @Valid UsuarioRequest request) {
        UsuarioResponse response = service.save(request);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(response.getId()).toUri();

        return ResponseEntity.created(uri).body(response);
    }


    @GetMapping("/usuarios/{username}")
    public ResponseEntity<UsuarioResponse> findByUserName(@PathVariable String username) {
        return ResponseEntity.ok(service.findByUsername(username));
    }
}
