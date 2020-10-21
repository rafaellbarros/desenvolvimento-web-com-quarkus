package com.github.rafaellbarros.ifood.pedido.incoming;

import com.github.rafaellbarros.ifood.pedido.model.dto.PedidoRealizadoDTO;
import com.github.rafaellbarros.ifood.pedido.model.dto.PratoPedidoDTO;
import com.github.rafaellbarros.ifood.pedido.model.entity.Pedido;
import com.github.rafaellbarros.ifood.pedido.model.entity.Prato;
import com.github.rafaellbarros.ifood.pedido.model.entity.Restaurante;
import org.bson.types.Decimal128;
import org.eclipse.microprofile.reactive.messaging.Incoming;

import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;

@ApplicationScoped
public class PedidoRealizadoIncoming {

    @Incoming("pedidos")
    public void lerPedidos(PedidoRealizadoDTO dto) {
        System.out.println("-----------------");
        System.out.println(dto);

        Pedido p = new Pedido();
        p.cliente = dto.cliente;
        p.pratos = new ArrayList<>();
        dto.pratos.forEach(prato -> p.pratos.add(from(prato)));
        Restaurante restaurante = new Restaurante();
        restaurante.nome = dto.restaurante.nome;
        p.restaurante = restaurante;
        p.persist();

    }

    private Prato from(PratoPedidoDTO prato) {
        Prato p = new Prato();
        p.descricao = prato.descricao;
        p.nome = prato.nome;
        p.preco = new Decimal128(prato.preco);
        return p;
    }
}