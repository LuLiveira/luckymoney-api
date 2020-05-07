package br.com.luckymoney.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.luckymoney.model.Pessoa;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PessoaRepository extends  JpaRepository<Pessoa, Long> {

    @Query("SELECT p FROM Pessoa p WHERE p.nome LIKE ?1 ")
    public List<Pessoa> buscaPorNome(String nome);
}
