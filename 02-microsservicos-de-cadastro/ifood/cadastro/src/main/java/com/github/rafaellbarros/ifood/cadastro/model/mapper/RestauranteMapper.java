package com.github.rafaellbarros.ifood.cadastro.model.mapper;

import com.github.rafaellbarros.ifood.cadastro.model.dto.AdicionarRestauranteDTO;
import com.github.rafaellbarros.ifood.cadastro.model.dto.AtualizarRestauranteDTO;
import com.github.rafaellbarros.ifood.cadastro.model.dto.RestauranteDTO;
import com.github.rafaellbarros.ifood.cadastro.model.entity.Restaurante;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "cdi")
public interface RestauranteMapper {

    @Mapping(target = "nome", source = "nomeFantasia")
    public Restaurante toRestaurante(AdicionarRestauranteDTO dto);

    @Mapping(target = "nomeFantasia", source = "nome")
    @Mapping(target = "dataCriacao", dateFormat = "dd/MM/yyyy HH:mm:ss")
    public RestauranteDTO toRestauranteDTO(Restaurante r);

    @Mapping(target = "nome", source = "nomeFantasia")
    public void toRestaurante(AtualizarRestauranteDTO dto, @MappingTarget Restaurante restaurante);
}
