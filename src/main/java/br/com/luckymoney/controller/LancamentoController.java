package br.com.luckymoney.controller;

import static org.springframework.http.HttpStatus.CREATED;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.luckymoney.business.LancamentoBusiness;
import br.com.luckymoney.event.RecursoCriadoEvent;
import br.com.luckymoney.model.Lancamento;
import br.com.luckymoney.repository.filter.LancamentoFilter;
/***
 * 
 * @author Lucas Oliveira
 *
 */
@RestController
@RequestMapping("/lancamentos")
public class LancamentoController {

	@Autowired
	private LancamentoBusiness lancamentoBusiness;
	
	@Autowired
	private ApplicationEventPublisher publisher;
	
	@GetMapping
	public ResponseEntity<Page<Lancamento>> pesquisar(LancamentoFilter lancamentoFilter, Pageable pageable) {
		Page<Lancamento> listaDeLancamentos = lancamentoBusiness.filtrar(lancamentoFilter, pageable);
		return ResponseEntity.ok(listaDeLancamentos);
	}
	
	@GetMapping(value = "/{codigo}")
	public ResponseEntity<Lancamento> buscarPorCodigo(@PathVariable Long codigo) {
		Lancamento lancamento = lancamentoBusiness.buscarPorCodigo(codigo);
		return ResponseEntity.ok(lancamento);
	}
	
	@PostMapping
	public ResponseEntity<Lancamento> criar(@Valid @RequestBody Lancamento lancamento, HttpServletResponse response) {
		lancamento = lancamentoBusiness.criar(lancamento);
		
		publisher.publishEvent(new RecursoCriadoEvent(this, response, lancamento.getCodigo()));

		return ResponseEntity.status(CREATED).body(lancamento);
	}

	@DeleteMapping(value = "/{codigo}")
	public void remover(@PathVariable Long codigo) {
		lancamentoBusiness.remover(codigo);
	}
}
