package br.com.luckymoney.exceptionhandler;

import static br.com.luckymoney.util.Utils.isNull;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import br.com.luckymoney.business.exception.PessoaInexistenteOuInativaException;
import br.com.luckymoney.exceptionhandler.utils.Erro;

/***
 * 
 * @author Lucas Oliveira
 *
 */
@ControllerAdvice
public class LuckymoneyExceptionHandler extends ResponseEntityExceptionHandler {

	@Autowired
	private MessageSource messageSource;

	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		return handleExceptionInternal(ex, errosBuilder(ex), headers, BAD_REQUEST, request);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		return handleExceptionInternal(ex, errosBuilder(ex), headers, BAD_REQUEST, request);
	}

	@ExceptionHandler({ EmptyResultDataAccessException.class })
	@ResponseStatus(NOT_FOUND)
	public ResponseEntity<Object> handleEmptyResultDataAccessException(EmptyResultDataAccessException ex,
			WebRequest request) {
		return handleExceptionInternal(ex, errosBuilder(ex), new HttpHeaders(), NOT_FOUND, request);
	}

	@ExceptionHandler({ DataIntegrityViolationException.class })
	public ResponseEntity<Object> handleDataIntegrityViolationException(DataIntegrityViolationException ex,
			WebRequest request) {
		return handleExceptionInternal(ex, errosBuilder(ex), new HttpHeaders(), BAD_REQUEST, request);
	}

	@ExceptionHandler({ PessoaInexistenteOuInativaException.class })
	public ResponseEntity<Object> handlePessoaInexistenteOuInativaException(PessoaInexistenteOuInativaException ex,
			WebRequest request) {
		return handleExceptionInternal(ex, errosBuilder(ex), new HttpHeaders(), BAD_REQUEST, request);
	}

	private List<Erro> errosBuilder(Exception ex) {
		List<Erro> erros = new ArrayList<Erro>();

		if (ex instanceof MethodArgumentNotValidException) {
			ex = (MethodArgumentNotValidException) ex;
			for (FieldError fieldError : ((MethodArgumentNotValidException) ex).getBindingResult().getFieldErrors()) {
				erros.add(new Erro().mensagemUsuario(messageBuilderParaUsuario(fieldError))
						.mensagemDesenvolvedor(fieldError.toString()));
			}
		} else if (ex instanceof EmptyResultDataAccessException) {
			erros.add(new Erro().mensagemDesenvolvedor(validaMensagemParaDesenvolvedor(ex))
					.mensagemUsuario(messageBuilderParaUsuario("recurso.nao-encontrado")));
		} else if (ex instanceof HttpMessageNotReadableException) {
			erros.add(new Erro().mensagemUsuario(messageBuilderParaUsuario("mensagem.invalida"))
					.mensagemDesenvolvedor(validaMensagemParaDesenvolvedor(ex)));
		} else if (ex instanceof DataIntegrityViolationException) {
			erros.add(new Erro().mensagemUsuario(messageBuilderParaUsuario("recurso.operacao-nao-permitida"))
					.mensagemDesenvolvedor(ExceptionUtils.getRootCauseMessage(ex)));
		} else if (ex instanceof PessoaInexistenteOuInativaException) {
			erros.add(new Erro().mensagemUsuario(messageBuilderParaUsuario("pessoa.inexistente-ou-inativa"))
					.mensagemDesenvolvedor(validaMensagemParaDesenvolvedor(ex)));
		}

		return erros;
	}

	private String messageBuilderParaUsuario(String messageProperties) {
		return messageSource.getMessage(messageProperties, null, LocaleContextHolder.getLocale());
	}

	private String messageBuilderParaUsuario(FieldError messageProperties) {
		return messageSource.getMessage(messageProperties, LocaleContextHolder.getLocale());
	}

	private String validaMensagemParaDesenvolvedor(Exception ex) {
		return isNull(ex.getCause()) ? ex.toString() : ex.getCause().getMessage();
	}

}
