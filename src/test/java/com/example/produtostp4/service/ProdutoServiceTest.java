package com.example.produtostp4.service;

import com.example.produtostp4.dto.ProdutoRequestDTO;
import com.example.produtostp4.dto.ProdutoResponseDTO;
import com.example.produtostp4.entity.HistoricoProduto;
import com.example.produtostp4.entity.Produto;
import com.example.produtostp4.messaging.ProdutoEventPublisher;
import com.example.produtostp4.repository.ProdutoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProdutoServiceTest {
    @Mock
    private ProdutoRepository produtoRepository;
    @Mock
    private HistoricoProdutoService historicoProdutoService;
    @Mock
    private ProdutoEventPublisher produtoEventPublisher;

    private ProdutoService produtoService;

    @BeforeEach
    void setUp() {
        produtoService = new ProdutoService(produtoRepository, historicoProdutoService, produtoEventPublisher);
    }

    private Produto criarProduto(Long id, String nome, BigDecimal preco) {
        Produto produto = new Produto(nome, preco, LocalDateTime.now(), LocalDateTime.now(), null);
        produto.setId(id);
        return produto;
    }

    @Test
    void deveCriarProdutoESalvarHistorico() {
        ProdutoRequestDTO dto = new ProdutoRequestDTO("Teclado", new BigDecimal("150.00"));
        when(produtoRepository.save(any(Produto.class))).thenAnswer(inv -> {
                    Produto produto = inv.getArgument(0);
                    produto.setId(1L);
                    return produto;
                });

        ProdutoResponseDTO resultado = produtoService.criarProduto(dto);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Teclado", resultado.getNome());
        assertEquals(new BigDecimal("150.00"), resultado.getPreco());
    }

    @Test
    void deveCriarProdutoEAdicionarNoEstoque() {
        ProdutoRequestDTO dto = new ProdutoRequestDTO("Teclado", new BigDecimal("150.00"));
        when(produtoRepository.save(any(Produto.class))).thenAnswer(inv -> {
                    Produto produto = inv.getArgument(0);
                    produto.setId(1L);
                    return produto;
                });

        produtoService.criarProduto(dto);
        verify(produtoEventPublisher).publicarProdutoCriado(1L);
    }

    @Test
    void deveRegistrarValoresDaCriacaoNoHistorico() {
        ProdutoRequestDTO dto = new ProdutoRequestDTO("Mouse", new BigDecimal("80.00"));

        when(produtoRepository.save(any(Produto.class))).thenAnswer(inv -> {
                    Produto produto = inv.getArgument(0);
                    produto.setId(1L);
                    return produto;
                });

        produtoService.criarProduto(dto);

        ArgumentCaptor<HistoricoProduto> captor = ArgumentCaptor.forClass(HistoricoProduto.class);

        verify(historicoProdutoService).salvar(captor.capture());

        HistoricoProduto historico = captor.getValue();

        assertNull(historico.getNome());
        assertEquals("Mouse", historico.getNomeAtualizado());
        assertNull(historico.getPreco());
        assertEquals(new BigDecimal("80.00"), historico.getPrecoAtualizado());
        assertEquals(1L, historico.getProduto().getId());
    }

    @Test
    void deveBuscarProdutosNaoExcluidos() {
        Produto produto1 = criarProduto(1L, "Mouse", new BigDecimal("80.00"));
        Produto produto2 = criarProduto(2L, "Teclado", new BigDecimal("150.00"));

        when(produtoRepository.findProdutoByDataExclusaoNull()).thenReturn(List.of(produto1, produto2));

        List<ProdutoResponseDTO> resultado = produtoService.buscarProdutos();

        assertEquals(2, resultado.size());
        assertEquals("Mouse", resultado.get(0).getNome());
        assertEquals("Teclado", resultado.get(1).getNome());
    }

    @Test
    void deveLancarExcecaoQuandoNaoExistiremProdutos() {
        when(produtoRepository.findProdutoByDataExclusaoNull()).thenReturn(List.of());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> produtoService.buscarProdutos());

        assertEquals("Nenhum produto cadastrado", exception.getMessage());
    }

    @Test
    void deveBuscarProdutoAtivoPorId() {
        Produto produto = criarProduto(1L, "Monitor", new BigDecimal("1000.00"));

        when(produtoRepository.findProdutoByIdAndDataExclusaoNull(1L)).thenReturn(Optional.of(produto));

        ProdutoResponseDTO resultado = produtoService.buscarProdutoPorId(1L);

        assertEquals(1L, resultado.getId());
        assertEquals("Monitor", resultado.getNome());
        assertEquals(new BigDecimal("1000.00"), resultado.getPreco());
    }

    @Test
    void deveLancarExcecaoQuandoProdutoNaoForEncontrado() {
        when(produtoRepository.findProdutoByIdAndDataExclusaoNull(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> produtoService.buscarProdutoPorId(1L));

        assertEquals("Produto não encontrado", exception.getMessage());
    }

    @Test
    void deveAtualizarProdutoESalvarHistorico() {
        Produto produto = criarProduto(1L, "Mouse", new BigDecimal("80.00"));
        ProdutoRequestDTO dto = new ProdutoRequestDTO("Mouse Gamer", new BigDecimal("120.00"));

        when(produtoRepository.findProdutoByIdAndDataExclusaoNull(1L)).thenReturn(Optional.of(produto));

        when(produtoRepository.save(any(Produto.class))).thenAnswer(inv -> inv.getArgument(0));

        ProdutoResponseDTO resultado = produtoService.atualizarProduto(1L, dto);

        assertEquals("Mouse Gamer", resultado.getNome());
        assertEquals(new BigDecimal("120.00"), resultado.getPreco());
        assertNotNull(resultado.getDataAtualizada());
    }

    @Test
    void deveMaterValoresAntigosNoHistoricoDaAtualizacao() {
        Produto produto = criarProduto(1L, "Mouse", new BigDecimal("80.00"));
        ProdutoRequestDTO dto = new ProdutoRequestDTO("Mouse Gamer", new BigDecimal("120.00"));

        when(produtoRepository.findProdutoByIdAndDataExclusaoNull(1L)).thenReturn(Optional.of(produto));
        when(produtoRepository.save(any(Produto.class))).thenAnswer(inv -> inv.getArgument(0));

        produtoService.atualizarProduto(1L, dto);

        ArgumentCaptor<HistoricoProduto> captor = ArgumentCaptor.forClass(HistoricoProduto.class);

        verify(historicoProdutoService).salvar(captor.capture());

        HistoricoProduto historico = captor.getValue();

        assertEquals("Mouse", historico.getNome());
        assertEquals("Mouse Gamer", historico.getNomeAtualizado());
        assertEquals(new BigDecimal("80.00"), historico.getPreco());
        assertEquals(new BigDecimal("120.00"), historico.getPrecoAtualizado());
        assertEquals(1L, historico.getProduto().getId());
    }

    @Test
    void deveRealizarExclusaoLogicaDoProduto() {
        Produto produto = criarProduto(1L, "Mouse", new BigDecimal("80.00"));

        when(produtoRepository.findProdutoByIdAndDataExclusaoNull(1L)).thenReturn(Optional.of(produto));
        when(produtoRepository.save(any(Produto.class))).thenAnswer(inv -> inv.getArgument(0));

        produtoService.deletarProduto(1L);

        assertNotNull(produto.getDataExclusao());
    }
}