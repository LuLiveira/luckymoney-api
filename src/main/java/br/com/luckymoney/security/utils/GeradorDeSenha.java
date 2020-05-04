package br.com.luckymoney.security.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class GeradorDeSenha {

    static {
        BCryptPasswordEncoder b = new BCryptPasswordEncoder();
        String admin = b.encode("admin");
        System.out.println("password admin: ".concat(admin));
    }
}
