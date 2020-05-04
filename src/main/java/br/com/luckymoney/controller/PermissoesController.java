package br.com.luckymoney.controller;

import br.com.luckymoney.business.PermissoesBusiness;
import br.com.luckymoney.model.Permissao;
import br.com.luckymoney.repository.PermissoesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/permissoes")
public class PermissoesController {

    @Autowired
    private PermissoesBusiness permissoesBusiness;

    @GetMapping
    public ResponseEntity<Map<Long, String>> buscar() {
        Map<Long, String> permissoes = permissoesBusiness.buscar();
        return ResponseEntity.ok(permissoes);
    }
}
