package com.lucianodev.controlecertificado.services;

import com.lucianodev.controlecertificado.dtos.request.UsuarioRequest;
import com.lucianodev.controlecertificado.dtos.response.UsuarioResponse;
import com.lucianodev.controlecertificado.entities.Usuario;
import com.lucianodev.controlecertificado.exceptions.ConflictException;
import com.lucianodev.controlecertificado.factories.UsuarioFactory;
import com.lucianodev.controlecertificado.mapper.UsuarioMapper;
import com.lucianodev.controlecertificado.repositories.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;


@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {

    @InjectMocks
    UsuarioService service;
    @Mock
    UsuarioRepository repository;
    @Mock
    PasswordEncoder passwordEncoder;
    @Mock
    UsuarioMapper mapper;

    @Test
    public void deveLancarConflictExceptionQuandoEmailOuUsernameJaExistir() {
        UsuarioRequest usuarioRequest = UsuarioFactory.usuarioRequest();

        Mockito.when(repository.existsByUsernameOrEmail(usuarioRequest.getUsername(), usuarioRequest.getEmail())).thenReturn(true);

        assertThrows(ConflictException.class,
                () -> service.save(usuarioRequest));
    }

    @Test
    public void deveSalvarUsuarioComSucesso() {
        UsuarioRequest usuarioRequest = UsuarioFactory.usuarioRequest();
        Usuario entity = UsuarioFactory.usuarioEntity();

        Mockito.when(repository.existsByUsernameOrEmail(usuarioRequest.getUsername(), usuarioRequest.getEmail())).thenReturn(false);

        Mockito.when(mapper.toEntity(usuarioRequest)).thenReturn(entity);
        Mockito.when(passwordEncoder.encode(usuarioRequest.getSenha())).thenReturn("senha_criptografada");
        Mockito.when(repository.save(entity)).thenReturn(entity);
        Mockito.when(mapper.toResponse(entity)).thenReturn(new UsuarioResponse());

        UsuarioResponse usuarioResponse = service.save(usuarioRequest);

        assertNotNull(usuarioResponse);
        Mockito.verify(repository).save(entity);
    }

    @Test
    public void deveLancarUsernameNotFoundExceptionQuandoNaoExistirUsuario() {
        String usuarioFalso = "usuarioFalso";

        Mockito.when(repository.findByUsername(usuarioFalso)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class,
                () -> service.findByUsername(usuarioFalso));
    }

    @Test
    public void deveBuscarUsuarioPorUsernameComSucesso() {
        Usuario usuario = UsuarioFactory.usuarioEntity();
        String username = usuario.getUsername();

        Mockito.when(repository.findByUsername(username)).thenReturn(Optional.of(usuario));
        Mockito.when(mapper.toResponse(usuario)).thenReturn(new UsuarioResponse());

        UsuarioResponse usuarioResponse = service.findByUsername(username);

        assertNotNull(usuarioResponse);
        Mockito.verify(repository).findByUsername(username);
    }
}
