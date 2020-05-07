package br.com.luckymoney.controller;

import static org.springframework.http.HttpStatus.CREATED;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import br.com.luckymoney.model.Pessoa;
import br.com.luckymoney.repository.projection.ResumoLancamento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO') and #oauth2.hasScope('read')")
	public ResponseEntity<Page<Lancamento>> pesquisar(LancamentoFilter lancamentoFilter, Pageable pageable) {
		Page<Lancamento> listaDeLancamentos = lancamentoBusiness.filtrar(lancamentoFilter, pageable);
		return ResponseEntity.ok(listaDeLancamentos);
	}

	@GetMapping(params = "resumo")
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO') and #oauth2.hasScope('read')")
	public ResponseEntity<Page<ResumoLancamento>> resumir(LancamentoFilter lancamentoFilter, Pageable pageable) {
		Page<ResumoLancamento> listaDeLancamentos = lancamentoBusiness.resumir(lancamentoFilter, pageable);
		return ResponseEntity.ok(listaDeLancamentos);
	}
	
	@GetMapping(value = "/{codigo}")
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO') and #oauth2.hasScope('read')")
	public ResponseEntity<Lancamento> buscarPorCodigo(@PathVariable Long codigo) {
		Lancamento lancamento = lancamentoBusiness.buscarPorCodigo(codigo);
		return ResponseEntity.ok(lancamento);
	}
	
	@PostMapping
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_LANCAMENTO') and #oauth2.hasScope('write')")
	public ResponseEntity<Lancamento> criar(@Valid @RequestBody Lancamento lancamento, HttpServletResponse response) {
		lancamento = lancamentoBusiness.criar(lancamento);
		publisher.publishEvent(new RecursoCriadoEvent(this, response, lancamento.getCodigo()));
		return ResponseEntity.status(CREATED).body(lancamento);
	}

	@DeleteMapping(value = "/{codigo}")
	@PreAuthorize("hasAuthority('ROLE_REMOVER_CATEGORIA') and #oauth2.hasScope('delete')")
	public void remover(@PathVariable Long codigo) {
		lancamentoBusiness.remover(codigo);
	}

	@PutMapping("/{codigo}")
	@PreAuthorize("hasAuthority('ROLE_ATUALIZAR_LANCAMENTO') and #oauth2.hasScope('write')")
	public ResponseEntity<Lancamento> atualizar(@PathVariable Long codigo, @Valid @RequestBody Lancamento novoLancamento) {
		return ResponseEntity.ok(lancamentoBusiness.atualizar(novoLancamento, codigo));
	}
}
