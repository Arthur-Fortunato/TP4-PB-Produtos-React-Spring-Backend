package com.example.produtostp4.controller;

import com.example.produtostp4.dto.ProdutoRequestDTO;
import com.example.produtostp4.dto.ProdutoResponseDTO;
import com.example.produtostp4.service.ProdutoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/produtos")
public class ProdutoController {
    @Autowired
    private ProdutoService produtoService;

    @GetMapping
    public ResponseEntity<List<ProdutoResponseDTO>> listarProdutos(){
        return ResponseEntity.ok().body(this.produtoService.buscarProdutos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProdutoResponseDTO> buscarProdutoPorId(@PathVariable Long id){
        return ResponseEntity.ok().body(this.produtoService.buscarProdutoPorId(id));
    }

    @PostMapping
    public ResponseEntity<ProdutoResponseDTO> adicionarProduto(@Valid @RequestBody ProdutoRequestDTO dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(this.produtoService.criarProduto(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProdutoResponseDTO> atualizarProduto(@PathVariable Long id,@Valid @RequestBody ProdutoRequestDTO dto){
        return ResponseEntity.ok().body(this.produtoService.atualizarProduto(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ProdutoResponseDTO> removerProduto(@PathVariable Long id){
        this.produtoService.deletarProduto(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
