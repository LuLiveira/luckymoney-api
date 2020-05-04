package br.com.luckymoney.repository;

import br.com.luckymoney.model.Permissao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissoesRepository extends JpaRepository<Permissao, Long> {
}
