package br.com.luckymoney.controller;

import br.com.luckymoney.business.UsuarioBusiness;
import br.com.luckymoney.event.RecursoCriadoEvent;
import br.com.luckymoney.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioBusiness usuarioBusiness;

    @Autowired
    private ApplicationEventPublisher publisher;

    @GetMapping
    public ResponseEntity<List<Usuario>> buscar() {
        List<Usuario> usuarios = usuarioBusiness.buscar();
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<Usuario> buscarPorCodigo(@PathVariable Long codigo) {
        Usuario usuario = usuarioBusiness.buscarPorCodigo(codigo);
        return ResponseEntity.ok(usuario);
    }

    @PostMapping
    public ResponseEntity<Usuario> criar(@RequestBody Usuario usuario, HttpServletResponse response) {
        usuario = usuarioBusiness.criar(usuario);

        publisher.publishEvent(new RecursoCriadoEvent(this, response, usuario.getCodigo()));

        return ResponseEntity.status(CREATED).body(usuario);
    }
}
