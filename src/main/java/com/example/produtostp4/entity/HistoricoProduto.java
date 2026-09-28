package com.example.produtostp4.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class HistoricoProduto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String nomeAtualizado;
    private BigDecimal preco;
    private BigDecimal precoAtualizado;
    private LocalDateTime dataAtualizacao;

    @ManyToOne
    @JoinColumn(name = "produto_id")
    private Produto produto;

    public HistoricoProduto(String nome, String nomeAtualizado, BigDecimal preco, BigDecimal precoAtualizado, LocalDateTime dataAtualizacao, Produto produto) {
        this.nome = nome;
        this.nomeAtualizado = nomeAtualizado;
        this.preco = preco;
        this.precoAtualizado = precoAtualizado;
        this.dataAtualizacao = dataAtualizacao;
        this.produto = produto;
    }
}
