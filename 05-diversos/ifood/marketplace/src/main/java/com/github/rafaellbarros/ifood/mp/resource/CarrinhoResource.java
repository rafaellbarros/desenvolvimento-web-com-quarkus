package com.github.rafaellbarros.ifood.mp.resource;

import com.github.rafaellbarros.ifood.mp.model.dto.PedidoRealizadoDTO;
import com.github.rafaellbarros.ifood.mp.model.dto.PratoDTO;
import com.github.rafaellbarros.ifood.mp.model.dto.PratoPedidoDTO;
import com.github.rafaellbarros.ifood.mp.model.dto.RestauranteDTO;
import com.github.rafaellbarros.ifood.mp.model.entity.Prato;
import com.github.rafaellbarros.ifood.mp.model.entity.PratoCarrinho;
import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.stream.Collectors;

@Path("carrinho")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CarrinhoResource {

    private static final String CLIENTE = "xpto";

    @Inject
    io.vertx.mutiny.pgclient.PgPool client;

    @Inject
    @Channel("pedidos")
    Emitter<PedidoRealizadoDTO> emitterPedido;

    @GET
    public Uni<List<PratoCarrinho>> buscarcarrinho() {
        return PratoCarrinho.findCarrinho(client, CLIENTE);
    }

    @POST
    @Path("/prato/{idPrato}")
    public Uni<Long> adicionarPrato(@PathParam("idPrato") Long idPrato) {
        //poderia retornar o pedido atual
        PratoCarrinho pc = new PratoCarrinho();
        pc.cliente = CLIENTE;
        pc.prato = idPrato;
        return PratoCarrinho.save(client, CLIENTE, idPrato);

    }

    @POST
    @Path("/realizar-pedido")
    public Uni<Boolean> finalizar() {
        PedidoRealizadoDTO pedido = new PedidoRealizadoDTO();
        String cliente = CLIENTE;
        pedido.cliente = cliente;
        List<PratoCarrinho> pratoCarrinho = PratoCarrinho.findCarrinho(client, cliente).await().indefinitely();
        //Utilizar mapstruts
        List<PratoPedidoDTO> pratos = pratoCarrinho.stream().map(pc -> from(pc)).collect(Collectors.toList());
        pedido.pratos = pratos;

        RestauranteDTO restaurante = new RestauranteDTO();
        restaurante.nome = "nome restaurante";
        pedido.restaurante = restaurante;
        emitterPedido.send(pedido);
        return PratoCarrinho.delete(client, cliente);
    }

    private PratoPedidoDTO from(PratoCarrinho pratoCarrinho) {
        PratoDTO dto = Prato.findById(client, pratoCarrinho.prato).await().indefinitely();
        return new PratoPedidoDTO(dto.nome, dto.descricao, dto.preco);
    }

}
