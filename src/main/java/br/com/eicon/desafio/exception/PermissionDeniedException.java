package br.com.eicon.desafio.exception;

import org.springframework.http.HttpStatus;

public class PermissionDeniedException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private final String message;
	private final HttpStatus httpStatus;
	
	public PermissionDeniedException() {
		this.message = "Permissão negada";
		this.httpStatus = HttpStatus.FORBIDDEN;
	}
	
	@Override
	public String getMessage() {
		return message;
	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}
}