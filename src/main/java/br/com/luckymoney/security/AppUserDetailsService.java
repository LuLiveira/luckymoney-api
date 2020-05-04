package br.com.luckymoney.security;

import br.com.luckymoney.model.Usuario;
import br.com.luckymoney.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class AppUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Usuario> user = usuarioRepository.findByEmail(email);
        Usuario usuario = user.orElseThrow(() -> new UsernameNotFoundException("Usuario e/ou senha incorretos"));
        return new User(email, usuario.getSenha(), getPermissoes(usuario));
    }

    private Collection<? extends GrantedAuthority> getPermissoes(Usuario usuario) {
        Set<SimpleGrantedAuthority> authorities = new HashSet<SimpleGrantedAuthority>();

        usuario
            .getPermissoes()
            .forEach(permissao -> authorities.add(new SimpleGrantedAuthority(permissao.getDescricao().toUpperCase())));

        return authorities;
    }
}
