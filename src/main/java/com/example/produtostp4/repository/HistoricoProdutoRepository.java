package com.example.produtostp4.repository;

import com.example.produtostp4.entity.HistoricoProduto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoricoProdutoRepository extends JpaRepository<HistoricoProduto,Long> {
    List<HistoricoProduto> findHistoricoProdutoByProduto_Id(Long id);
}
