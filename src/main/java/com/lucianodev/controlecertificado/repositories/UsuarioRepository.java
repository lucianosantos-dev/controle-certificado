package com.lucianodev.controlecertificado.repositories;

import com.lucianodev.controlecertificado.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByUsername(String username);
    boolean existsByUsernameOrEmail(String username, String email);
}
