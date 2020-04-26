package br.com.luckymoney.business;

import static br.com.luckymoney.util.Utils.isNull;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.luckymoney.business.exception.PessoaInexistenteOuInativaException;
import br.com.luckymoney.model.Lancamento;
import br.com.luckymoney.model.Pessoa;
import br.com.luckymoney.repository.LancamentoRepository;
import br.com.luckymoney.repository.PessoaRepository;
import br.com.luckymoney.repository.filter.LancamentoFilter;

/***
 * 
 * @author Lucas Oliveira
 *
 */
@Service
public class LancamentoBusiness {

	@Autowired
	private LancamentoRepository lancamentoRepository;

	@Autowired
	private PessoaRepository pessoaRepository;

	public Page<Lancamento> filtrar(LancamentoFilter lancamentoFilter, Pageable pageable) {
		return lancamentoRepository.filtrar(lancamentoFilter, pageable);
	}

	public Lancamento buscarPorCodigo(Long codigo) {
		Lancamento lancamento = lancamentoRepository.findOne(codigo);
		return lancamento;
	}

	public Lancamento criar(Lancamento lancamento) {

		Pessoa pessoa = pessoaRepository.findOne(lancamento.getPessoa().getCodigo());
		validaPessoa(pessoa);

		return lancamentoRepository.save(lancamento);
	}

	private void validaPessoa(Pessoa pessoa) {
		if (isNull(pessoa) || !pessoa.isAtivo()) {
			throw new PessoaInexistenteOuInativaException();
		}
	}

	public void remover(Long codigo) {
		lancamentoRepository.delete(codigo);
	}
}
