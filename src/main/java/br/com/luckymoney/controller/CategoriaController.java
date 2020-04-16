package br.com.luckymoney.controller;

import static org.springframework.http.HttpStatus.CREATED;

import java.net.URI;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

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
	private CategoriaRepository categoriaRepository;

	@Autowired
	private ApplicationEventPublisher publisher;

	@GetMapping
	public List<Categoria> listar() {
		return categoriaRepository.findAll();
	}

	@PostMapping
	public ResponseEntity<Categoria> criar(@Valid @RequestBody Categoria categoria, HttpServletResponse response) {
		categoria = categoriaRepository.save(categoria);

		/*Dispara evento para criar Location com ID do Objeto criado
		Esse padrão de projeto é conhecido como Observer*/
		publisher.publishEvent(new RecursoCriadoEvent(this, response, categoria.getCodigo()));

		return ResponseEntity.status(CREATED).body(categoria);
	}

	@GetMapping("/{codigo}")
	public ResponseEntity<Categoria> buscarPeloCodigo(@PathVariable Long codigo) {
		Categoria categoria = categoriaRepository.findOne(codigo);
		return !Utils.isNull(categoria) ? ResponseEntity.ok(categoria) : ResponseEntity.notFound().build();
	}
	
	@DeleteMapping("/{codigo}")
	public void remover (@PathVariable Long codigo) {
		categoriaRepository.delete(codigo);
	}
}
