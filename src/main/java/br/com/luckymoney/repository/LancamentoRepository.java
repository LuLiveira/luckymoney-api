package br.com.luckymoney.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.luckymoney.model.Lancamento;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long> {

}
