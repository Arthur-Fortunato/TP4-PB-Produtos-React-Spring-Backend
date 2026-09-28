package com.example.produtostp4.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@AllArgsConstructor
@Data
public class ProdutoRequestDTO {
    @NotBlank
    private String nome;
    @NotNull
    @Positive
    private BigDecimal preco;
}
