package br.com.luckymoney.business;

import br.com.luckymoney.model.Usuario;
import br.com.luckymoney.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioBusiness {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Usuario> buscar() {
        return usuarioRepository.findAll();
    }

    public Usuario buscarPorCodigo(Long codigo) {
        return usuarioRepository.findOne(codigo);
    }

    public Usuario criar(Usuario usuario) {
        return usuarioRepository.save(encodeAndSave(usuario));
    }

    private Usuario encodeAndSave(Usuario usuario) {
        String senha = usuario.getSenha();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String passwordHash = encoder.encode(senha);
        usuario.setSenha(passwordHash);
        return usuario;
    }
}
