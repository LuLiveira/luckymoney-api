package br.com.luckymoney.controller;

import static org.springframework.http.HttpStatus.CREATED;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import br.com.luckymoney.business.CategoriaBusiness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.luckymoney.event.RecursoCriadoEvent;
import br.com.luckymoney.model.Categoria;
import br.com.luckymoney.repository.CategoriaRepository;
import br.com.luckymoney.util.Utils;

/***
 * 
 * @author Lucas Oliveira
 *
 */
@RestController
@RequestMapping("/categorias")
public class CategoriaController {

	@Autowired
	private CategoriaBusiness categoriaBusiness;

	@Autowired
	private ApplicationEventPublisher publisher;

	@GetMapping
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_CATEGORIA') and #oauth2.hasScope('read')")
	public List<Categoria> listar() {
		return categoriaBusiness.findAll();
	}

	@PostMapping
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_CATEGORIA') and #oauth2.hasScope('write')")
	public ResponseEntity<Categoria> criar(@Valid @RequestBody Categoria categoria, HttpServletResponse response) {
		categoria = categoriaBusiness.save(categoria);

		/*
		 * Dispara evento para criar Location com ID do Objeto criado Esse padrão de
		 * projeto é conhecido como Observer
		 */
		publisher.publishEvent(new RecursoCriadoEvent(this, response, categoria.getCodigo()));

		return ResponseEntity.status(CREATED).body(categoria);
	}

	@GetMapping("/{codigo}")
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_CATEGORIA') and #oauth2.hasScope('read')")
	public ResponseEntity<Categoria> buscarPeloCodigo(@PathVariable Long codigo) {
		Categoria categoria = categoriaBusiness.findOne(codigo);
		return !Utils.isNull(categoria) ? ResponseEntity.ok(categoria) : ResponseEntity.notFound().build();
	}

	@DeleteMapping("/{codigo}")
	@PreAuthorize("hasAuthority('ROLE_REMOVER_CATEGORIA') and #oauth2.hasScope('delete')")
	public void remover(@PathVariable Long codigo) {
		categoriaBusiness.delete(codigo);
	}
}
