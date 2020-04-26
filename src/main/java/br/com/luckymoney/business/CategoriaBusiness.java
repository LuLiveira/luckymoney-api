package br.com.luckymoney.business;

import br.com.luckymoney.model.Categoria;
import br.com.luckymoney.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/***
 *
 * @author Lucas Oliveira
 *
 */

@Service
public class CategoriaBusiness {

    @Autowired
    private CategoriaRepository categoriaRepository;


    public List<Categoria> findAll() {
        return categoriaRepository.findAll();
    }

    public Categoria save(Categoria categoria) {
        return categoriaRepository.save(categoria);
    }

    public Categoria findOne(Long codigo) {
        return categoriaRepository.findOne(codigo);
    }

    public void delete(Long codigo) {
        categoriaRepository.delete(codigo);
    }
}
