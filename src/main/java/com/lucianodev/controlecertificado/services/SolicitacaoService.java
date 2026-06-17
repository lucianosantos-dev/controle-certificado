package com.lucianodev.controlecertificado.services;

import com.lucianodev.controlecertificado.dtos.request.AtualizarStatusRequest;
import com.lucianodev.controlecertificado.dtos.request.SolicitacaoRequest;
import com.lucianodev.controlecertificado.dtos.response.SolicitacaoListagemResponse;
import com.lucianodev.controlecertificado.dtos.response.SolicitacaoResponse;
import com.lucianodev.controlecertificado.entities.Solicitacao;
import com.lucianodev.controlecertificado.entities.Usuario;
import com.lucianodev.controlecertificado.enums.StatusSolicitacao;
import com.lucianodev.controlecertificado.exceptions.ConflictException;
import com.lucianodev.controlecertificado.exceptions.ResourceNotFoundException;
import com.lucianodev.controlecertificado.mapper.SolicitacaoMapper;
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
    private final SolicitacaoMapper mapper;


    @Transactional
    public SolicitacaoResponse save(SolicitacaoRequest request) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var usuarioLogado = (Usuario) authentication.getPrincipal();

        boolean possuiSolicitacao = repository.
                existsByUsuarioIdAndCursoIgnoreCase(usuarioLogado.getId(), request.getCurso());

        if (possuiSolicitacao) {
            throw new ConflictException("Voce ja possui uma solicitaçao para o curso: " + request.getCurso());
        }

        Solicitacao solicitacao = mapper.toEntity(request);
        solicitacao.setUsuario(usuarioLogado);
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
    public Page<SolicitacaoListagemResponse> findAll(String nome, String cpf, Pageable pageable) {
        return repository.buscarComFiltros(nome, cpf, pageable)
                .map(mapper::toListResponse);
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
        if (statusAtual == StatusSolicitacao.PENDENTE && novoStatus != StatusSolicitacao.CONCLUIDO) {
            throw new ConflictException("Uma solicitação PENDENTE só pode ser alterada para CONCLUIDO.");
        }
        if (statusAtual == StatusSolicitacao.CONCLUIDO && novoStatus != StatusSolicitacao.ENTREGUE) {
            throw new ConflictException("Uma solicitação CONCLUIDA só pode ser alterada para ENTREGUE.");
        }

        solicitacao.setStatusSolicitacao(novoStatus);
        repository.save(solicitacao);
    }
}
