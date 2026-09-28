package com.example.produtostp4.controller;

import com.example.produtostp4.dto.HistoricoProdutoResponseDTO;
import com.example.produtostp4.service.HistoricoProdutoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/historico")
public class HistoricoProdutoController {
    private final HistoricoProdutoService service;

    public HistoricoProdutoController(HistoricoProdutoService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<HistoricoProdutoResponseDTO>> listar() {
        List<HistoricoProdutoResponseDTO> historicoProdutos = this.service.buscarHistoricoProdutos();
        return ResponseEntity.ok().body(historicoProdutos);
    }

    @GetMapping("/{idproduto}")
    public ResponseEntity<List<HistoricoProdutoResponseDTO>> buscarPorProdutoId(@PathVariable(name = "idproduto") Long id) {
        List<HistoricoProdutoResponseDTO> historicoProdutoList = this.service.buscarHistoricoProdutoPorId(id);
        return ResponseEntity.ok().body(historicoProdutoList);
    }
}
