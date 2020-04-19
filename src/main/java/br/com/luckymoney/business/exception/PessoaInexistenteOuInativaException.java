package br.com.luckymoney.business.exception;

public class PessoaInexistenteOuInativaException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public PessoaInexistenteOuInativaException(String s) {
		super(s);
	}
	public PessoaInexistenteOuInativaException() {}
}
