package com.github.rafaellbarros.ifood.cadastro.model.mapper;

import com.github.rafaellbarros.ifood.cadastro.model.dto.AdicionarPratoDTO;
import com.github.rafaellbarros.ifood.cadastro.model.dto.AtualizarPratoDTO;
import com.github.rafaellbarros.ifood.cadastro.model.dto.PratoDTO;
import com.github.rafaellbarros.ifood.cadastro.model.entity.Prato;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "cdi")
public interface PratoMapper {

    PratoDTO toDTO(Prato p);

    Prato toPrato(AdicionarPratoDTO dto);

    void toPrato(AtualizarPratoDTO dto, @MappingTarget Prato prato);

}