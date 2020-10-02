package com.github.rafaellbarros.ifood.cadastro.model.mapper;

import com.github.rafaellbarros.ifood.cadastro.model.dto.AdicionarRestauranteDTO;
import com.github.rafaellbarros.ifood.cadastro.model.entity.Restaurante;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "cdi")
public interface RestauranteMapper {

    @Mapping(target = "nome", source = "nomeFantasia")
    public Restaurante toRestaurante(AdicionarRestauranteDTO dto);
}
