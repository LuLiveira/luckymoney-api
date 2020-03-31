package br.com.luckymoney.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.luckymoney.model.Pessoa;

public interface PessoaRepository extends JpaRepository<Pessoa, Long>{

}
