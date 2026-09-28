package com.example.produtostp4.service;

import com.example.produtostp4.dto.HistoricoProdutoResponseDTO;
import com.example.produtostp4.entity.HistoricoProduto;
import com.example.produtostp4.repository.HistoricoProdutoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HistoricoProdutoService {
    private final HistoricoProdutoRepository repository;

    public HistoricoProdutoService(HistoricoProdutoRepository repository) {
        this.repository = repository;
    }

    public HistoricoProdutoResponseDTO salvar(HistoricoProduto historicoProduto) {
        HistoricoProduto historicoSalvo = this.repository.save(historicoProduto);
        return transformarDTO(historicoSalvo);
    }

    public List<HistoricoProdutoResponseDTO> buscarHistoricoProdutos(){
        return transformarDTO(this.repository.findAll());
    }

    public List<HistoricoProdutoResponseDTO> buscarHistoricoProdutoPorId(Long id){
        List<HistoricoProduto> historicoProdutoList = this.repository.findHistoricoProdutoByProduto_Id(id);
        if (historicoProdutoList.isEmpty()){
            throw new RuntimeException("Nenhum historico do produto foi encontrado");
        }
        return transformarDTO(historicoProdutoList);
    }

    private List<HistoricoProdutoResponseDTO> transformarDTO(List<HistoricoProduto> historicoProdutosList) {
        return historicoProdutosList.stream().map(this::transformarDTO).toList();
    }

    private HistoricoProdutoResponseDTO transformarDTO(HistoricoProduto historico) {
        return new HistoricoProdutoResponseDTO(
                historico.getId(),
                historico.getNome(),
                historico.getNomeAtualizado(),
                historico.getPreco(),
                historico.getPrecoAtualizado(),
                historico.getDataAtualizacao()
        );
    }
}
