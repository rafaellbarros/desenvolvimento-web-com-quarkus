package com.github.rafaellbarros.ifood.cadastro.resource;

import com.github.rafaellbarros.ifood.cadastro.model.entity.Restaurante;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import javax.print.attribute.standard.Media;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Optional;

@Path("/restaurantes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
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
    public void excluir(@PathParam("id") Long id) {
        Optional<Restaurante> produtoOptional = Restaurante.findByIdOptional(id);
        produtoOptional.ifPresentOrElse(Restaurante::delete, () -> {
            throw new NotFoundException();
        });
    }
}
