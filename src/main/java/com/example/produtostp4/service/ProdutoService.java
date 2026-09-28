package com.example.produtostp4.service;

import com.example.produtostp4.dto.ProdutoRequestDTO;
import com.example.produtostp4.dto.ProdutoResponseDTO;
import com.example.produtostp4.entity.HistoricoProduto;
import com.example.produtostp4.entity.Produto;
import com.example.produtostp4.messaging.ProdutoEventPublisher;
import com.example.produtostp4.repository.ProdutoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProdutoService {
    private final ProdutoRepository produtoRepository;
    private final HistoricoProdutoService historicoProdutoService;
    private final ProdutoEventPublisher produtoEventPublisher;

    public ProdutoService(ProdutoRepository produtoRepository, HistoricoProdutoService historicoProdutoService, ProdutoEventPublisher produtoEventPublisher) {
        this.produtoRepository = produtoRepository;
        this.historicoProdutoService = historicoProdutoService;
        this.produtoEventPublisher = produtoEventPublisher;
    }

    @Transactional(rollbackFor = Exception.class)
    public ProdutoResponseDTO criarProduto(ProdutoRequestDTO dto){
        Produto produto = new Produto(dto.getNome(), dto.getPreco(), LocalDateTime.now(), LocalDateTime.now(), null);
        Produto produtoSalvo = this.produtoRepository.save(produto);
        this.historicoProdutoService.salvar(new HistoricoProduto(null, produtoSalvo.getNome(), null,  produtoSalvo.getPreco(), produtoSalvo.getDataCriacao(), produtoSalvo));
        produtoEventPublisher.publicarProdutoCriado(produtoSalvo.getId());
        return new ProdutoResponseDTO(produtoSalvo.getId(),  produtoSalvo.getNome(), produtoSalvo.getPreco(), produtoSalvo.getDataCriacao());
    }

    public List<ProdutoResponseDTO> buscarProdutos(){
        List<Produto> produtos = this.produtoRepository.findProdutoByDataExclusaoNull();
        if (produtos.isEmpty()){
            throw new RuntimeException("Nenhum produto cadastrado");
        }
        return produtos.stream()
                .map(produto -> new ProdutoResponseDTO(produto.getId(), produto.getNome(), produto.getPreco(), produto.getDataAtualizacao()))
                .collect(Collectors.toList());
    }

    public ProdutoResponseDTO buscarProdutoPorId(Long id){
        Produto produto = this.produtoRepository.findProdutoByIdAndDataExclusaoNull(id).orElseThrow(() -> new RuntimeException("Produto não encontrado"));
        return new ProdutoResponseDTO(produto.getId(), produto.getNome(), produto.getPreco(), produto.getDataAtualizacao());
    }

    @Transactional(rollbackFor = Exception.class)
    public ProdutoResponseDTO atualizarProduto(Long id, ProdutoRequestDTO dto){
        Produto produto = this.produtoRepository.findProdutoByIdAndDataExclusaoNull(id).orElseThrow(() -> new RuntimeException("Produto não encontrado"));
        String nomeAntigo = produto.getNome();
        BigDecimal precoAntigo = produto.getPreco();
        produto.setNome(dto.getNome());
        produto.setPreco(dto.getPreco());
        produto.setDataAtualizacao(LocalDateTime.now());
        Produto produtoAtualizado = this.produtoRepository.save(produto);
        this.historicoProdutoService.salvar(new HistoricoProduto(
                nomeAntigo,
                produtoAtualizado.getNome(),
                precoAntigo,
                produtoAtualizado.getPreco(),
                produtoAtualizado.getDataAtualizacao(),
                produtoAtualizado));

        return new ProdutoResponseDTO(produtoAtualizado.getId(), produtoAtualizado.getNome(), produtoAtualizado.getPreco(), produtoAtualizado.getDataAtualizacao());
    }

    public void deletarProduto(Long id){
        Produto produto = this.produtoRepository.findProdutoByIdAndDataExclusaoNull(id).orElseThrow(() -> new RuntimeException("Produto não encontrado"));
        produto.setDataExclusao(LocalDateTime.now());
        this.produtoRepository.save(produto);
        produtoEventPublisher.publicarProdutoExcluido(produto.getId());
    }
}
