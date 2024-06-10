package br.com.eicon.desafio.exception;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
		String errorMessage = ex.getBindingResult().getFieldErrors().stream()
				.map(fieldError -> fieldError.getDefaultMessage()).findFirst().orElse(ex.getMessage());

		Map<String, Object> body = new LinkedHashMap<>();
		body.put("status", HttpStatus.BAD_REQUEST);
		body.put("message", errorMessage);

		return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(CustomException.class)
	@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
	@ResponseBody
	public ResponseEntity<Object> handleCustomException(CustomException ex) {
		Map<String, Object> body = new LinkedHashMap<>();
		body.put("status", ex.getHttpStatus());
		body.put("message", ex.getMessage());

		return new ResponseEntity<>(body, ex.getHttpStatus());
	}
	
	@ExceptionHandler(PermissionDeniedException.class)
	@ResponseStatus(HttpStatus.FORBIDDEN)
	@ResponseBody
	public ResponseEntity<Object> handlePermissionDeniedException(PermissionDeniedException ex) {
		Map<String, Object> body = new LinkedHashMap<>();
		body.put("status", ex.getHttpStatus());
		body.put("message", ex.getMessage());

		return new ResponseEntity<>(body, ex.getHttpStatus());
	}
}