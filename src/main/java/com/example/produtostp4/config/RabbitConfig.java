package com.example.produtostp4.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;

@Configuration
public class RabbitConfig {

    public static final String PRODUTO_EXCHANGE = "produto.events";
    public static final String PRODUTO_CRIADO_QUEUE = "estoque.produto-criado";
    public static final String PRODUTO_CRIADO_ROUTING_KEY = "produto.criado";
    public static final String PRODUTO_EXCLUIDO_QUEUE = "estoque.produto-excluido";
    public static final String PRODUTO_EXCLUIDO_ROUTING_KEY = "produto.excluido";

    @Bean
    public TopicExchange produtoExchange() {
        return new TopicExchange(PRODUTO_EXCHANGE);
    }

    @Bean
    public Queue produtoCriadoQueue() {
        return new Queue(PRODUTO_CRIADO_QUEUE, true);
    }

    @Bean
    public Binding produtoCriadoBinding(Queue produtoCriadoQueue, TopicExchange produtoExchange) {
        return BindingBuilder.bind(produtoCriadoQueue).to(produtoExchange).with(PRODUTO_CRIADO_ROUTING_KEY);
    }

    @Bean
    public MessageConverter messageConverter() {
        return new JacksonJsonMessageConverter();
    }

    @Bean
    public Queue produtoExcluidoQueue() {
        return new Queue(PRODUTO_EXCLUIDO_QUEUE, true);
    }

    @Bean
    public Binding produtoExcluidoBinding(Queue produtoExcluidoQueue, TopicExchange produtoExchange) {
        return BindingBuilder.bind(produtoExcluidoQueue).to(produtoExchange).with(PRODUTO_EXCLUIDO_ROUTING_KEY);
    }
}