package com.github.rafaellbarros.ifood.cadastro.resource;

import com.github.rafaellbarros.ifood.cadastro.model.dto.*;
import com.github.rafaellbarros.ifood.cadastro.model.entity.Prato;
import com.github.rafaellbarros.ifood.cadastro.model.entity.Restaurante;
import com.github.rafaellbarros.ifood.cadastro.model.mapper.PratoMapper;
import com.github.rafaellbarros.ifood.cadastro.model.mapper.RestauranteMapper;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Path("/restaurantes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "restaurante")
public class RestauranteResource {


    @Inject
    RestauranteMapper restauranteMapper;

    @Inject
    PratoMapper pratoMapper;

    @GET
    public List<RestauranteDTO> listarTodos() {
        // TODO: Refactoring
        Stream<Restaurante> restaurantes = Restaurante.streamAll();
        return restaurantes.map(r -> restauranteMapper.toRestauranteDTO(r)).collect(Collectors.toList());
    }

    @POST
    @Transactional
    public void adicionar(AdicionarRestauranteDTO dto) {
        final Restaurante restaurante = restauranteMapper.toRestaurante(dto);
        restaurante.persist();
    }

    @PUT
    @Path("{id}")
    @Transactional
    public void atualizar(@PathParam("id") Long id, AtualizarRestauranteDTO dto) {
        Optional<Restaurante> restauranteOptional = Restaurante.findByIdOptional(id);
        if (restauranteOptional.isEmpty()) {
            throw new NotFoundException();
        }
        Restaurante restarante = restauranteOptional.get();
        restauranteMapper.toRestaurante(dto, restarante);
        restarante.persist();
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public void deletar(@PathParam("id") Long id) {
        Optional<Restaurante> produtoOptional = Restaurante.findByIdOptional(id);
        produtoOptional.ifPresentOrElse(Restaurante::delete, () -> {
            throw new NotFoundException();
        });
    }

    // Pratos
    @GET
    @Path("{idRestaurante}/pratos")
    @Tag(name = "prato")
    public List<PratoDTO> buscarPratos(@PathParam("idRestaurante") Long idRestaurante) {
        Optional<Restaurante> restauranteOptional = Restaurante.findByIdOptional(idRestaurante);
        if (restauranteOptional.isEmpty()) {
            throw new NotFoundException("Restaurante não existe");
        }
        Stream<Prato> pratos = Prato.stream("restaurante", restauranteOptional.get());
        return pratos.map(p -> pratoMapper.toDTO(p)).collect(Collectors.toList());
    }

    @POST
    @Tag(name = "prato")
    @Path("{idRestaurante}/pratos")
    @Transactional
    @Tag(name = "prato")
    public Response adicionarPrato(@PathParam("idRestaurante") Long idRestaurante, AdicionarPratoDTO dto) {
        Optional<Restaurante> restauranteOptional = Restaurante.findByIdOptional(idRestaurante);
        if (restauranteOptional.isEmpty()) {
            throw new NotFoundException("Restaurante não existe");
        }

        Prato prato = pratoMapper.toPrato(dto);
        prato.restaurante = restauranteOptional.get();
        prato.persist();
        return Response.status(Response.Status.CREATED).build();
    }

    @PUT
    @Path("{idRestaurante}/pratos/{id}")
    @Transactional
    @Tag(name = "prato")
    public void atualizarPrato(@PathParam("idRestaurante") Long idRestaurante, @PathParam("id") Long id, AtualizarPratoDTO dto) {
        Optional<Restaurante> restauranteOptional = Restaurante.findByIdOptional(idRestaurante);
        if (restauranteOptional.isEmpty()) {
            throw new NotFoundException("Restaurante não existe");
        }

        //No nosso caso, id do prato vai ser único, independente do restaurante...
        Optional<Prato> pratoOptional = Prato.findByIdOptional(id);
        if (pratoOptional.isEmpty()) {
            throw new NotFoundException("Prato não existe");
        }

        Prato prato = pratoOptional.get();
        pratoMapper.toPrato(dto, prato);
        prato.persist();
    }

    @DELETE
    @Path("{idRestaurante}/pratos/{id}")
    @Transactional
    @Tag(name = "prato")
    public void deletarPrato(@PathParam("idRestaurante") Long idRestaurante, @PathParam("id") Long id) {
        Optional<Restaurante> restauranteOptional = Restaurante.findByIdOptional(idRestaurante);
        if (restauranteOptional.isEmpty()) {
            throw new NotFoundException("Restaurante não existe");
        }

        Optional<Prato> pratoOptional = Prato.findByIdOptional(id);
        pratoOptional.ifPresentOrElse(Prato::delete, () -> {
            throw new NotFoundException();
        });
    }
}
