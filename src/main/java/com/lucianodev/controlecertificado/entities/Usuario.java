package com.lucianodev.controlecertificado.entities;

import com.lucianodev.controlecertificado.enums.Perfil;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "tb_usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, nullable = false)
    private String nome;

    @Column(length = 100, unique = true, nullable = false)
    private String email;

    @Column(length = 50, unique = true, nullable = false)
    private String username;

    @Column(length = 150, nullable = false)
    private String senha;

    @Enumerated(EnumType.STRING)
    @Column(length = 15, nullable = false)
    private Perfil perfil;
}
