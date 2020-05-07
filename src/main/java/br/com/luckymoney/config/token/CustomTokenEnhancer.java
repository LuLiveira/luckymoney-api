package br.com.luckymoney.config.token;

import br.com.luckymoney.model.Usuario;
import br.com.luckymoney.security.UsuarioSistema;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import java.util.HashMap;
import java.util.Map;

public class CustomTokenEnhancer implements TokenEnhancer {
    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        UsuarioSistema usuario = (UsuarioSistema) authentication.getPrincipal();

        Map<String, Object> addInfo = new HashMap<String, Object>();
        addInfo.put("nome", usuario.getUsuario().getNome());

        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(addInfo);
        return accessToken;
    }
}
