package br.com.luckymoney.controller;

import static br.com.luckymoney.util.Utils.isNull;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.luckymoney.business.PessoaBusiness;
import br.com.luckymoney.event.RecursoCriadoEvent;
import br.com.luckymoney.model.Pessoa;
import br.com.luckymoney.repository.PessoaRepository;

/***
 * 
 * @author Lucas Oliveira
 *
 */
@RestController
@RequestMapping("/pessoas")
public class PessoaController {

	@Autowired
	private PessoaBusiness pessoaBusiness;

	@Autowired
	private ApplicationEventPublisher publisher;

	@GetMapping
	public List<Pessoa> listar() {
		return pessoaBusiness.findAll();
	}

	@GetMapping("/{codigo}")
	public ResponseEntity<Pessoa> buscarPorCodigo(@PathVariable Long codigo) {
		Pessoa pessoa = pessoaBusiness.findOne(codigo);
		return !isNull(pessoa) ? ResponseEntity.ok(pessoa) : ResponseEntity.notFound().build();
	}

	@PostMapping
	public ResponseEntity<Pessoa> criar(@Valid @RequestBody Pessoa pessoa, HttpServletResponse response) {
		pessoa = pessoaBusiness.save(pessoa);

		publisher.publishEvent(new RecursoCriadoEvent(this, response, pessoa.getCodigo()));

		return ResponseEntity.status(CREATED).body(pessoa);
	}

	@DeleteMapping("/{codigo}")
	@ResponseStatus(NO_CONTENT)
	public void remover(@PathVariable Long codigo) {
		pessoaBusiness.delete(codigo);
	}

	@PutMapping("/{codigo}")
	public ResponseEntity<Pessoa> atualizar(@PathVariable Long codigo, @Valid @RequestBody Pessoa novaPessoa) {
		return ResponseEntity.ok(pessoaBusiness.atualizar(novaPessoa, codigo));
	}

	@PutMapping("/{codigo}/ativo")
	@ResponseStatus(NO_CONTENT)
	public void atualizarPropriedadeAtivo(@PathVariable Long codigo, @RequestBody Boolean ativo) {
		pessoaBusiness.atualizarPropriedadeAtivo(codigo, ativo);
	}

}
