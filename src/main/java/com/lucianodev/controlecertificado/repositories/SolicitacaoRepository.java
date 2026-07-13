package com.lucianodev.controlecertificado.repositories;

import com.lucianodev.controlecertificado.entities.Solicitacao;
import com.lucianodev.controlecertificado.enums.StatusSolicitacao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SolicitacaoRepository extends JpaRepository<Solicitacao, Long> {

    boolean existsByUsuarioIdAndCursoIgnoreCase(Long usuarioId, String curso);

    List<Solicitacao> findByUsuarioId(Long usuarioId);

    @Query("SELECT s FROM Solicitacao s WHERE " +
            "(:nome IS NULL OR LOWER(s.nomeAluno) LIKE LOWER(CONCAT('%', :nome, '%'))) AND " +
            "(:cpf IS NULL OR s.cpf = :cpf) AND " +
            "(:status IS NULL OR s.statusSolicitacao = :status)")
    Page<Solicitacao> buscarComFiltros(String nome, String cpf, StatusSolicitacao status, Pageable pageable);
}
