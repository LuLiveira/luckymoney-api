package br.com.luckymoney.exceptionhandler;

import static br.com.luckymoney.util.Utils.isNull;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import br.com.luckymoney.util.Utils;

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

		String msgUsuario = messageSource.getMessage("mensagem.invalida", null, LocaleContextHolder.getLocale());
		String msgDev = isNull(ex.getCause()) ? ex.toString() : ex.getCause().getMessage();

		List<Erro> erros = Arrays.asList(new Erro(msgUsuario, msgDev));
		return handleExceptionInternal(ex, erros, headers, BAD_REQUEST, request);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		List<Erro> erros = criarListaDeErros(ex.getBindingResult());
		return handleExceptionInternal(ex, erros, headers, BAD_REQUEST, request);
	}

	@ExceptionHandler({ EmptyResultDataAccessException.class })
	@ResponseStatus(NOT_FOUND)
	public ResponseEntity<Object> handleEmptyResultDataAccessException(EmptyResultDataAccessException ex,
			WebRequest request) {
		String msgUsuario = messageSource.getMessage("recurso.nao-encontrado", null, LocaleContextHolder.getLocale());
		String msgDev = ex.toString();

		List<Erro> erros = Arrays.asList(new Erro(msgUsuario, msgDev));
		return handleExceptionInternal(ex, erros, new HttpHeaders(), NOT_FOUND, request);
	}

	private List<Erro> criarListaDeErros(BindingResult bindingResult) {
		List<Erro> erros = new ArrayList<Erro>();

		for (FieldError fieldError : bindingResult.getFieldErrors()) {
			String msgUsuario = messageSource.getMessage(fieldError, LocaleContextHolder.getLocale());
			String msgDev = fieldError.toString();
			erros.add(new Erro(msgUsuario, msgDev));
		}

		return erros;
	}

	public static class Erro {
		private final String msgUsuario;
		private final String msgDev;

		public Erro(String msgUsuario, String msgDev) {
			this.msgUsuario = msgUsuario;
			this.msgDev = msgDev;
		}

		public String getMsgUsuario() {
			return msgUsuario;
		}

		public String getMsgDev() {
			return msgDev;
		}
	}
}
