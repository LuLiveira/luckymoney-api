package br.com.luckymoney.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.luckymoney.model.Lancamento;
import br.com.luckymoney.repository.lancamento.LancamentoRepositoryQuery;

/***
 * 
 * @author Lucas Oliveira
 *
 */
public interface LancamentoRepository extends JpaRepository<Lancamento, Long>, LancamentoRepositoryQuery {

}
