package com.lucianodev.controlecertificado.mapper;

import com.lucianodev.controlecertificado.dtos.request.CursoRequest;
import com.lucianodev.controlecertificado.dtos.response.CursoResponse;
import com.lucianodev.controlecertificado.entities.Curso;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CursoMapper {

    @Mapping(target = "id", ignore = true)
    Curso toEntity(CursoRequest request);
    CursoResponse toResponse(Curso entity);
}
