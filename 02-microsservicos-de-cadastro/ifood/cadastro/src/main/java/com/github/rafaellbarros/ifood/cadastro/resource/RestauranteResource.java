package com.github.rafaellbarros.ifood.cadastro.resource;

import com.github.rafaellbarros.ifood.cadastro.model.entity.Prato;
import com.github.rafaellbarros.ifood.cadastro.model.entity.Restaurante;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.eclipse.microprofile.openapi.annotations.tags.Tags;

import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;

@Path("/restaurantes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "restaurante")
public class RestauranteResource {

    @GET
    public List<Restaurante> listarTodos() {
        return Restaurante.listAll();
    }

    @POST
    @Transactional
    public void adicionar(Restaurante restaurante) {
        restaurante.persist();
    }

    @PUT
    @Path("{id}")
    @Transactional
    public void atualizar(@PathParam("id") Long id, Restaurante restaurante) {
        Optional<Restaurante> restauranteOptional = Restaurante.findByIdOptional(id);
        if (restauranteOptional.isEmpty()) {
            throw new NotFoundException();
        }
        Restaurante restaranteAtualizar = restauranteOptional.get();
        restaranteAtualizar.nome = restaurante.nome;
        restaranteAtualizar.persist();
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
    public List<Restaurante> buscarPratos(@PathParam("idRestaurante") Long idRestaurante) {
        Optional<Restaurante> restauranteOptional = Restaurante.findByIdOptional(idRestaurante);
        if (restauranteOptional.isEmpty()) {
            throw new NotFoundException("Restaurante não existe");
        }
        return Prato.list("restaurante", restauranteOptional.get());
    }

    @POST
    @Tag(name = "prato")
    @Path("{idRestaurante}/pratos")
    @Transactional
    @Tag(name = "prato")
    public Response adicionarPrato(@PathParam("idRestaurante") Long idRestaurante, Prato prato) {
        Optional<Restaurante> restauranteOptional = Restaurante.findByIdOptional(idRestaurante);
        if (restauranteOptional.isEmpty()) {
            throw new NotFoundException("Restaurante não existe");
        }
        // Utilizando prato, recebi detached entity passed to persist:
        Prato novoPrato = new Prato();
        novoPrato.nome = prato.nome;
        novoPrato.descricao = prato.descricao;
        novoPrato.preco = prato.preco;
        novoPrato.restaurante = restauranteOptional.get();
        novoPrato.persist();

        return Response.status(Response.Status.CREATED).build();
    }

    @PUT
    @Path("{idRestaurante}/pratos/{id}")
    @Transactional
    @Tag(name = "prato")
    public void atualizarPrato(@PathParam("idRestaurante") Long idRestaurante, @PathParam("id") Long id, Prato prato) {
        Optional<Restaurante> restauranteOptional = Restaurante.findByIdOptional(idRestaurante);
        if (restauranteOptional.isEmpty()) {
            throw new NotFoundException("Restaurante não existe");
        }

        //No nosso caso, id do prato vai ser único, independente do restaurante...
        Optional<Prato> pratoOptional = Prato.findByIdOptional(id);
        if (pratoOptional.isEmpty()) {
            throw new NotFoundException("Prato não existe");
        }

        // Utilizando prato, recebi detached entity passed to persist:
        Prato atualizarPrato = pratoOptional.get();
        atualizarPrato.preco = prato.preco;
        atualizarPrato.persist();
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
