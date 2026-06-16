package com.lucianodev.controlecertificado.entities;

import com.lucianodev.controlecertificado.enums.StatusSolicitacao;
import com.lucianodev.controlecertificado.enums.TipoCertificado;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "tb_solicitacao")
public class Solicitacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, nullable = false)
    private String nomeAluno;

    @Column(length = 50, nullable = false)
    private String curso;

    @Column(nullable = false)
    private LocalDate dataSolicitacao;

    @Column(nullable = false)
    private LocalDate dataConclusao;

    @Column(nullable = false)
    private LocalDate dataLimiteEntrega;

    @Column(length = 20, nullable = false)
    private String telefone;

    @Column(length = 20, nullable = false)
    private String cpf;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private TipoCertificado tipoCertificado;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private StatusSolicitacao statusSolicitacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @PrePersist
    public void onCreate() {
        this.dataLimiteEntrega = LocalDate.now().plusDays(20);
        this.dataSolicitacao = LocalDate.now();
        this.statusSolicitacao = StatusSolicitacao.PENDENTE;
    }
}
