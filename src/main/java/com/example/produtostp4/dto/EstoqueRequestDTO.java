package com.example.produtostp4.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class EstoqueRequestDTO {
    private Long produtoId;
    private Integer quantidade;
}
