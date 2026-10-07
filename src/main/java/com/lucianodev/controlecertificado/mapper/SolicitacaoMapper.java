package com.lucianodev.controlecertificado.mapper;

import com.lucianodev.controlecertificado.dtos.request.SolicitacaoRequest;
import com.lucianodev.controlecertificado.dtos.response.SolicitacaoListagemResponse;
import com.lucianodev.controlecertificado.dtos.response.SolicitacaoResponse;
import com.lucianodev.controlecertificado.entities.Solicitacao;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SolicitacaoMapper {

    @Mapping(target = "dataLimiteEntrega", ignore = true)
    @Mapping(target = "dataSolicitacao", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "financeiroOk", ignore = true)
    @Mapping(target = "usuario", ignore = true)
    @Mapping(target = "statusSolicitacao", ignore = true)
    @Mapping(target = "curso", ignore = true)
    @Mapping(target = "nomeCurso.id", source = "cursoId")
    Solicitacao toEntity(SolicitacaoRequest request);

    @Mapping(target = "curso", source = "nomeCurso.nome")
    SolicitacaoResponse toResponse(Solicitacao entity);

    @Mapping(target = "curso", source = "nomeCurso.nome")
    SolicitacaoListagemResponse toListResponse(Solicitacao entity);
}
