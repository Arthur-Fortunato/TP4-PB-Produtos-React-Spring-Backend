package com.example.produtostp4.repository;

import com.example.produtostp4.entity.Produto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ProdutoRepositoryTest {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Test
    void deveSalvarProduto() {
        Produto produto = new Produto("Mouse", new BigDecimal("80.00"), LocalDateTime.now(), LocalDateTime.now(), null);
        Produto produtoSalvo = produtoRepository.save(produto);

        assertNotNull(produtoSalvo.getId());
        assertEquals("Mouse", produtoSalvo.getNome());
        assertEquals(new BigDecimal("80.00"), produtoSalvo.getPreco());
    }

    @Test
    void deveBuscarProdutosNaoExcluidos() {
        Produto produtoAtivo = new Produto("Mouse", new BigDecimal("80.00"), LocalDateTime.now(), LocalDateTime.now(), null);
        Produto produtoExcluido = new Produto("Teclado", new BigDecimal("150.00"), LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now());

        produtoRepository.save(produtoAtivo);
        produtoRepository.save(produtoExcluido);

        List<Produto> resultado = produtoRepository.findProdutoByDataExclusaoNull();

        assertEquals(1, resultado.size());
        assertEquals("Mouse", resultado.get(0).getNome());
        assertNull(resultado.get(0).getDataExclusao());
    }

    @Test
    void deveBuscarProdutoAtivoPorId() {
        Produto produto = new Produto("Monitor", new BigDecimal("1000.00"), LocalDateTime.now(), LocalDateTime.now(), null);
        Produto produtoSalvo = produtoRepository.save(produto);

        Optional<Produto> resultado = produtoRepository.findProdutoByIdAndDataExclusaoNull(produtoSalvo.getId());

        assertTrue(resultado.isPresent());
        assertEquals("Monitor", resultado.get().getNome());
    }

    @Test
    void naoDeveBuscarProdutoExcluidoPorId() {
        Produto produto = new Produto("Monitor", new BigDecimal("1000.00"), LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now());
        Produto produtoSalvo = produtoRepository.save(produto);

        Optional<Produto> resultado = produtoRepository.findProdutoByIdAndDataExclusaoNull(produtoSalvo.getId());

        assertTrue(resultado.isEmpty());
    }
}