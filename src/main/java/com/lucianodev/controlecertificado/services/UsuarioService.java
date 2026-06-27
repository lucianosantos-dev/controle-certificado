package com.lucianodev.controlecertificado.services;

import com.lucianodev.controlecertificado.dtos.request.UsuarioRequest;
import com.lucianodev.controlecertificado.dtos.response.UsuarioResponse;
import com.lucianodev.controlecertificado.entities.Usuario;
import com.lucianodev.controlecertificado.enums.Perfil;
import com.lucianodev.controlecertificado.exceptions.ConflictException;
import com.lucianodev.controlecertificado.mapper.UsuarioMapper;
import com.lucianodev.controlecertificado.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository repository;
    private final PasswordEncoder encoder;
    private final UsuarioMapper mapper;


    @Transactional
    public UsuarioResponse save(UsuarioRequest request) {
        validaDados(request.getUsername(), request.getEmail());

        Usuario user = mapper.toEntity(request);

        user.setSenha(encoder.encode(user.getSenha()));
        user.setPerfil(Perfil.ALUNO);

        Usuario usuarioSalvo = repository.save(user);
        return mapper.toResponse(usuarioSalvo);
    }

    @Transactional(readOnly = true)
    public UsuarioResponse findByUsername(String username) {
        Usuario user = repository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("Usuário não encontrado")
        );
        return mapper.toResponse(user);
    }


    private void validaDados(String username, String email) {
        if (repository.existsByUsernameOrEmail(username, email)) {
            throw new ConflictException("Username ou Email já existe.");
        }
    }
}
