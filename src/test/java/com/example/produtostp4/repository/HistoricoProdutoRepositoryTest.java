package com.example.produtostp4.repository;

import com.example.produtostp4.entity.HistoricoProduto;
import com.example.produtostp4.entity.Produto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class HistoricoProdutoRepositoryTest {
    @Autowired
    private ProdutoRepository produtoRepository;
    @Autowired
    private HistoricoProdutoRepository historicoProdutoRepository;

    private Produto criarESalvarProduto() {
        Produto produto = new Produto("Mouse", new BigDecimal("80.00"), LocalDateTime.now(), LocalDateTime.now(), null);
        return produtoRepository.save(produto);
    }

    @Test
    void deveSalvarHistoricoRelacionadoAoProduto() {
        Produto produto = criarESalvarProduto();
        HistoricoProduto historico = new HistoricoProduto(null, produto.getNome(), null, produto.getPreco(), LocalDateTime.now(), produto);

        HistoricoProduto historicoSalvo = historicoProdutoRepository.save(historico);

        assertNotNull(historicoSalvo.getId());
        assertNotNull(historicoSalvo.getProduto());
        assertEquals(produto.getId(), historicoSalvo.getProduto().getId());
    }

    @Test
    void deveBuscarHistoricosPeloIdDoProduto() {
        Produto produto = criarESalvarProduto();
        HistoricoProduto criacao = new HistoricoProduto(null, "Mouse", null, new BigDecimal("80.00"), LocalDateTime.now(), produto);
        HistoricoProduto atualizacao = new HistoricoProduto("Mouse", "Mouse Gamer", new BigDecimal("80.00"), new BigDecimal("120.00"), LocalDateTime.now(), produto);

        historicoProdutoRepository.save(criacao);
        historicoProdutoRepository.save(atualizacao);

        List<HistoricoProduto> resultado = historicoProdutoRepository.findHistoricoProdutoByProduto_Id(produto.getId());

        assertEquals(2, resultado.size());
        assertEquals("Mouse", resultado.get(0).getNomeAtualizado());
        assertEquals("Mouse Gamer", resultado.get(1).getNomeAtualizado());
    }
}