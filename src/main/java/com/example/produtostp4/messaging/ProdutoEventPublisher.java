package com.example.produtostp4.messaging;

import com.example.produtostp4.config.RabbitConfig;
import com.example.produtostp4.event.ProdutoCriadoEvent;
import com.example.produtostp4.event.ProdutoExcluidoEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class ProdutoEventPublisher {
    private final RabbitTemplate rabbitTemplate;

    public ProdutoEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publicarProdutoCriado(Long produtoId) {
        ProdutoCriadoEvent evento = new ProdutoCriadoEvent(produtoId);
        rabbitTemplate.convertAndSend(RabbitConfig.PRODUTO_EXCHANGE, RabbitConfig.PRODUTO_CRIADO_ROUTING_KEY, evento);
    }

    public void publicarProdutoExcluido(Long produtoId) {
        ProdutoExcluidoEvent evento = new ProdutoExcluidoEvent(produtoId);
        rabbitTemplate.convertAndSend(RabbitConfig.PRODUTO_EXCHANGE, RabbitConfig.PRODUTO_EXCLUIDO_ROUTING_KEY, evento);
    }
}