package br.com.luckymoney.business;

import static br.com.luckymoney.util.Utils.isNull;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import br.com.luckymoney.model.Pessoa;
import br.com.luckymoney.repository.PessoaRepository;

/***
 * 
 * @author Lucas Oliveira
 *
 */
@Service
public class PessoaBusiness {
	
	@Autowired
	private PessoaRepository pessoaRepository;

	public Pessoa atualizar(Pessoa novaPessoa, Long codigo) {

		Pessoa pessoa = pessoaRepository.findOne(codigo);
		if (isNull(pessoa))
			throw new EmptyResultDataAccessException(1);
		BeanUtils.copyProperties(novaPessoa, pessoa, "codigo"); // importante saber (copyProperties) -> copia as
		pessoa = pessoaRepository.save(pessoa); // propriedades de um objeto para outro, o terceiro
												// parametro é o atributo que deve ser ignorado (se
												// existir)
		return pessoa;
	}

}
