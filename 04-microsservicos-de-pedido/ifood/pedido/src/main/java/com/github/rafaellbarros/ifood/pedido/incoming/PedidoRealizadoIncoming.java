package com.github.rafaellbarros.ifood.pedido.incoming;

import com.github.rafaellbarros.ifood.pedido.model.dto.PedidoRealizadoDTO;
import org.eclipse.microprofile.reactive.messaging.Incoming;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PedidoRealizadoIncoming {


    @Incoming("pedidos")
    public void lerPedidos(PedidoRealizadoDTO dto) {
        System.out.println("-----------------");
        System.out.println(dto);
    }

}
