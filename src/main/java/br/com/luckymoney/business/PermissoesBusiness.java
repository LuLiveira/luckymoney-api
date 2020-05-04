package br.com.luckymoney.business;

import br.com.luckymoney.model.Permissao;
import br.com.luckymoney.repository.PermissoesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PermissoesBusiness {

    @Autowired
    private PermissoesRepository permissoesRepository;

    public Map<Long, String> buscar(){
        List<Permissao> permissaos = permissoesRepository.findAll();
        Map<Long, String> permissoes = new HashMap<Long, String>();

        for(Permissao p : permissaos) {
            permissoes.put(p.getCodigo(), p.getDescricao());
        }

        return permissoes;
    }
}
