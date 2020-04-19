package br.com.luckymoney.exceptionhandler.utils;

public class Erro {
	private String mensagemUsuario;
	private String mensagemDesenvolvedor;

	public Erro() {
	}

	public Erro(String mensagemUsuario, String mensagemDesenvolvedor) {
		this.mensagemUsuario = mensagemUsuario;
		this.mensagemDesenvolvedor = mensagemDesenvolvedor;
	}

	public Erro mensagemDesenvolvedor(final String s) {
		this.mensagemDesenvolvedor = s;
		return this;
	}

	public Erro mensagemUsuario(final String s) {
		this.mensagemUsuario = s;
		return this;
	}

	public String getMensagemUsuario() {
		return mensagemUsuario;
	}

	public String getMensagemDesenvolvedor() {
		return mensagemDesenvolvedor;
	}
}
