package br.com.luckymoney;

import br.com.luckymoney.config.property.Luckymoneyroperty;
import br.com.luckymoney.security.utils.GeradorDeSenha;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(Luckymoneyroperty.class)
public class LuckymoneyApiApplication {

	public static void main(String[] args) {

		SpringApplication.run(LuckymoneyApiApplication.class, args);

		new GeradorDeSenha();
	}
}
