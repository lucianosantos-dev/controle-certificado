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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class SolicitacaoServiceTest {

    @Mock
    SolicitacaoRepository repository;
    @InjectMocks
    SolicitacaoService service;

    @Mock
    SolicitacaoMapper mapper;

    private Long id = 1L;
    private Long idInexistente = 100L;
    private Solicitacao solicitacao;

    @BeforeEach
    public void setUp() {
        solicitacao = new Solicitacao();
        solicitacao.setStatusSolicitacao(StatusSolicitacao.PENDENTE);
        solicitacao.setId(id);
    }

    private Usuario simularUsuarioLogado() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("Aluno test");

        org.springframework.security.core.context.SecurityContext securityContext = Mockito.mock(org.springframework.security.core.context.SecurityContext.class);
        org.springframework.security.core.Authentication authentication = Mockito.mock(org.springframework.security.core.Authentication.class);

        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        Mockito.when(authentication.getPrincipal()).thenReturn(usuario);

        org.springframework.security.core.context.SecurityContextHolder.setContext(securityContext);

        return usuario;
    }

    @Test
    public void deveAtualizarStatusComSucesso() {

        //ARRANGE
        Mockito.when(repository.findById(id)).thenReturn(Optional.of(solicitacao));
        //ACT
        service.atualizarStatus(id, new AtualizarStatusRequest(StatusSolicitacao.CONCLUIDO));
        //ASSERT
        assertEquals(StatusSolicitacao.CONCLUIDO, solicitacao.getStatusSolicitacao());
        Mockito.verify(repository).save(solicitacao);
    }

    @Test
    public void deveLancarExcecaoQuandoIdNaoEncontrado() {

        //ARRANGE
        Mockito.when(repository.findById(idInexistente)).thenReturn(Optional.empty());

        //ACT / ASSERT
        assertThrows(ResourceNotFoundException.class,
                () -> service.atualizarStatus(idInexistente, new AtualizarStatusRequest(StatusSolicitacao.CONCLUIDO))
        );

    }

    @Test
    public void deveLancarConflictExceptionQuandoTentarMudarStatusPendenteParaEntregue() {

        //ARRANGE
        Mockito.when(repository.findById(id)).thenReturn(Optional.of(solicitacao));

        //ACT / ASSERT
        assertThrows(ConflictException.class,
                () -> service.atualizarStatus(id, new AtualizarStatusRequest(StatusSolicitacao.ENTREGUE)));
    }

    @Test
    public void deveLancarConflictExceptionQuandoStatusSolicitacaoJaEstiverEntregue() {

        //ARRANGE
        solicitacao.setStatusSolicitacao(StatusSolicitacao.ENTREGUE);
        Mockito.when(repository.findById(id)).thenReturn(Optional.of(solicitacao));

        //ACT / ASSERT
        assertThrows(ConflictException.class,
                () -> service.atualizarStatus(id, new AtualizarStatusRequest(StatusSolicitacao.PENDENTE)));
    }

    @Test
    public void deveLancarConflictExceptionQuandoTentarRetrocederStatusConcluido() {

        //ARRANGE
        solicitacao.setStatusSolicitacao(StatusSolicitacao.CONCLUIDO);
        Mockito.when(repository.findById(id)).thenReturn(Optional.of(solicitacao));

        //ACT / ASSERT
        assertThrows(ConflictException.class,
                () -> service.atualizarStatus(id, new AtualizarStatusRequest(StatusSolicitacao.PENDENTE)));
    }

    @Test
    public void deveSalvarSolicitacaoComSucesso() {
        Usuario usuarioLogado = simularUsuarioLogado();

        SolicitacaoRequest solicitacaoRequest = new SolicitacaoRequest();
        solicitacaoRequest.setCurso("Java 10.0");

        //ARRANGE
        Mockito.when(repository.existsByUsuarioIdAndCursoIgnoreCase(usuarioLogado.getId(), solicitacaoRequest.getCurso()))
                .thenReturn(false);

        Mockito.when(mapper.toEntity(solicitacaoRequest)).thenReturn(solicitacao);

        SolicitacaoResponse solicitacaoResponse = new SolicitacaoResponse();
        Mockito.when(mapper.toResponse(solicitacao)).thenReturn(solicitacaoResponse);

        //ACT
        SolicitacaoResponse responseSalvo = service.save(solicitacaoRequest);

        //ASSERT
        assertNotNull(responseSalvo);
        Mockito.verify(repository).save(solicitacao);
    }

    @Test
    public void deveLancarConflictExceptionQuandoUsuarioJaPossuiSolicitacaoParaOCurso() {

        //ARRANGE
        Usuario usuarioLogado = simularUsuarioLogado();

        SolicitacaoRequest solicitacaoRequest = new SolicitacaoRequest();
        solicitacaoRequest.setCurso("Java 10.0");

        Mockito.when(repository.existsByUsuarioIdAndCursoIgnoreCase(usuarioLogado.getId(), solicitacaoRequest.getCurso()))
                .thenReturn(true);

        //ACT / ASSERT
        assertThrows(ConflictException.class,
                () -> service.save(solicitacaoRequest));

        Mockito.verify(repository, Mockito.never()).save(Mockito.any(Solicitacao.class));
    }

    @Test
    public void deveMostrarSolicitacoesComSucesso() {

        //ARRANGE
        Usuario usuarioLogado = simularUsuarioLogado();

        Mockito.when(repository.findByUsuarioId(usuarioLogado.getId()))
                .thenReturn(java.util.List.of(solicitacao));

        SolicitacaoListagemResponse listagemResponse = new SolicitacaoListagemResponse(
                1L,
                "Aluno Teste",
                "Java",
                "12345678900",
                java.time.LocalDate.now(),
                java.time.LocalDate.now().plusDays(7),
                StatusSolicitacao.PENDENTE
        );
        Mockito.when(mapper.toListResponse(solicitacao)).thenReturn(listagemResponse);

        //ACT
        java.util.List<SolicitacaoListagemResponse> result = service.listarMinhasSolicitacoes();

        //ASSERT
        assertNotNull(result);
        assertEquals(1, result.size());
        Mockito.verify(repository).findByUsuarioId(usuarioLogado.getId());

    }

    @Test
    public void deveBuscarTodasSolicitacoesComFiltrosPaginadas() {

        //ARRANGE
        String nomeBusca = "Joao";
        String cpfBusca = "12345678900";
        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(0, 10);

        org.springframework.data.domain.Page<Solicitacao> paginaFalsa =
                new org.springframework.data.domain.PageImpl<>(java.util.List.of(solicitacao));

        Mockito.when(repository.buscarComFiltros(nomeBusca, cpfBusca, pageable))
                .thenReturn(paginaFalsa);

        SolicitacaoListagemResponse solicitacaoListagemResponse = new SolicitacaoListagemResponse(
                1L,
                "Aluno Teste",
                "Java",
                "12345678900",
                java.time.LocalDate.now(),
                java.time.LocalDate.now().plusDays(7),
                StatusSolicitacao.PENDENTE
        );
        Mockito.when(mapper.toListResponse(solicitacao)).thenReturn(solicitacaoListagemResponse);

        // ACT
        org.springframework.data.domain.Page<SolicitacaoListagemResponse> resultado =
                service.findAll(nomeBusca, cpfBusca, pageable);

        //ASSERT
        assertNotNull(resultado);
        assertEquals(1, resultado.getTotalElements());
        Mockito.verify(repository).buscarComFiltros(nomeBusca, cpfBusca, pageable);
    }
}