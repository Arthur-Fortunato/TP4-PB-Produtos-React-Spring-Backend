package com.example.produtostp4.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class HistoricoProdutoResponseDTO {
    private Long id;
    private String nome;
    private String nomeAtualizado;
    private BigDecimal preco;
    private BigDecimal precoAtualizado;
    private LocalDateTime dataAtualizacao;
}
