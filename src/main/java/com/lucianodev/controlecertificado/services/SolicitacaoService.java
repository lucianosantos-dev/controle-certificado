package com.lucianodev.controlecertificado.services;

import com.lucianodev.controlecertificado.dtos.request.AtualizarStatusRequest;
import com.lucianodev.controlecertificado.dtos.request.SolicitacaoRequest;
import com.lucianodev.controlecertificado.dtos.response.SolicitacaoListagemResponse;
import com.lucianodev.controlecertificado.dtos.response.SolicitacaoResponse;
import com.lucianodev.controlecertificado.entities.Curso;
import com.lucianodev.controlecertificado.entities.Solicitacao;
import com.lucianodev.controlecertificado.entities.Usuario;
import com.lucianodev.controlecertificado.enums.StatusSolicitacao;
import com.lucianodev.controlecertificado.exceptions.ConflictException;
import com.lucianodev.controlecertificado.exceptions.ForbiddenException;
import com.lucianodev.controlecertificado.exceptions.ResourceNotFoundException;
import com.lucianodev.controlecertificado.mapper.SolicitacaoMapper;
import com.lucianodev.controlecertificado.repositories.CursoRepository;
import com.lucianodev.controlecertificado.repositories.SolicitacaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SolicitacaoService {

    private final SolicitacaoRepository repository;
    private final CursoRepository cursoRepository;
    private final SolicitacaoMapper mapper;


    @Transactional
    public SolicitacaoResponse save(SolicitacaoRequest request) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var usuarioLogado = (Usuario) authentication.getPrincipal();

        String nomePerfil = usuarioLogado.getPerfil().name();

        boolean isAdmin = nomePerfil.equalsIgnoreCase("PEDAGOGICO") ||
                nomePerfil.equalsIgnoreCase("SECRETARIA");

        Curso curso = cursoRepository.findById(request.getCursoId())
                .orElseThrow(() -> new ResourceNotFoundException("Curso não encontrado com id informado"));

        if (!isAdmin) {
            boolean possuiSolicitacao = repository.
                    existsByUsuarioIdAndNomeCursoId(usuarioLogado.getId(), request.getCursoId());

            if (possuiSolicitacao) {
                throw new ConflictException("Você já possui uma solicitação para o curso selecionado.");
            }
        }


        Solicitacao solicitacao = mapper.toEntity(request);
        solicitacao.setUsuario(usuarioLogado);
        solicitacao.setNomeCurso(curso);
        repository.save(solicitacao);

        return mapper.toResponse(solicitacao);
    }

    @Transactional(readOnly = true)
    public List<SolicitacaoListagemResponse> listarMinhasSolicitacoes() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var usuarioLogado = (Usuario) authentication.getPrincipal();

        return repository.findByUsuarioId(usuarioLogado.getId())
                .stream()
                .map(mapper::toListResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public Page<SolicitacaoListagemResponse> findAll(String nome, String cpf, String status, Long cursoId, Pageable pageable) {

        StatusSolicitacao statusEnum = null;

        if (status != null && !status.isBlank() && !status.equals("TODOS")) {
            try {
                statusEnum = StatusSolicitacao.valueOf(status.toUpperCase());
            } catch (IllegalArgumentException e) {
                statusEnum = null;
            }
        }

        return repository.buscarComFiltros(nome, cpf, cursoId, statusEnum, pageable)
                .map(mapper::toListResponse);
    }

    @Transactional(readOnly = true)
    public SolicitacaoListagemResponse findById(Long id) {
        Solicitacao solicitacao = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Solicitação não encontrada"));

        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var usuarioLogado = (Usuario) authentication.getPrincipal();

        String nomePerfil = usuarioLogado.getPerfil().name();
        boolean isAdmin = nomePerfil.equalsIgnoreCase("PEDAGOGICO") ||
                nomePerfil.equalsIgnoreCase("SECRETARIA");

        if (!isAdmin && !solicitacao.getUsuario().getId().equals(usuarioLogado.getId())) {
            throw new ForbiddenException("Você não tem permissão para acessar esta solicitação.");
        }
        return mapper.toListResponse(solicitacao);
    }

    @Transactional
    public void atualizarStatus(Long id, AtualizarStatusRequest request) {
        Solicitacao solicitacao = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Solicitaçao nao encontrada: " + id));


        StatusSolicitacao statusAtual = solicitacao.getStatusSolicitacao();
        StatusSolicitacao novoStatus = request.status();

        if (statusAtual == StatusSolicitacao.ENTREGUE) {
            throw new ConflictException("Esta solicitação já foi entregue e finalizada. O status não pode ser alterado.");
        }

        if (statusAtual == StatusSolicitacao.PENDENTE &&
                novoStatus != StatusSolicitacao.CONCLUIDO &&
                novoStatus != StatusSolicitacao.AGUARDANDO_RETIRADA) {
            throw new ConflictException("Uma solicitação PENDENTE só pode ser alterada para CONCLUIDO ou AGUARDANDO_RETIRADA.");
        }

        if (statusAtual == StatusSolicitacao.CONCLUIDO && novoStatus != StatusSolicitacao.ENTREGUE && novoStatus != StatusSolicitacao.AGUARDANDO_RETIRADA) {
            throw new ConflictException("Uma solicitação CONCLUIDA só pode ser alterada para ENTREGUE ou AGURDANDO_RETIRADA.");
        }

        if (statusAtual == StatusSolicitacao.AGUARDANDO_RETIRADA && novoStatus != StatusSolicitacao.ENTREGUE) {
            throw new ConflictException("Uma solicitação AGUARDANDO_RETIRADA só pode ser alterada para ENTREGUE.");
        }

        solicitacao.setStatusSolicitacao(novoStatus);
        repository.save(solicitacao);
    }

    @Transactional
    public void marcarFinanceiroComoVerificado(Long idSolicitacao) {
        Solicitacao solicitacao = repository.findById(idSolicitacao)
                .orElseThrow(() -> new ResourceNotFoundException("Solicitação não encontrada"));

        solicitacao.marcarFinanceiroComoOk();

        repository.save(solicitacao);
    }
}
