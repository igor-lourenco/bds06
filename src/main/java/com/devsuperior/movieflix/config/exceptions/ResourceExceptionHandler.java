package com.devsuperior.movieflix.config.exceptions;

import java.time.Instant;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.devsuperior.movieflix.services.exceptions.DatabaseException;
import com.devsuperior.movieflix.services.exceptions.ForbiddenException;
import com.devsuperior.movieflix.services.exceptions.ResourceNotFoundException;
import com.devsuperior.movieflix.services.exceptions.UnauthorizedException;

@ControllerAdvice
public class ResourceExceptionHandler {
	
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<StandartError> entityNotFound(ResourceNotFoundException e, HttpServletRequest request){
		HttpStatus status = HttpStatus.NOT_FOUND; // 404
		StandartError err = new StandartError();
		err.setTimestamp(Instant.now());
		err.setStatus(status.value());
		err.setError("Recurso não encontrado");
		err.setMessage(e.getMessage());
		err.setPath(request.getRequestURI());
		return ResponseEntity.status(status).body(err);
	}

	@ExceptionHandler(DatabaseException.class)
	public ResponseEntity<StandartError> database(DatabaseException e, HttpServletRequest request){
		HttpStatus status = HttpStatus.BAD_REQUEST; //400
		StandartError err = new StandartError();
		err.setTimestamp(Instant.now());
		err.setStatus(status.value());
		err.setError("Exceção no banco");
		err.setMessage(e.getMessage());
		err.setPath(request.getRequestURI());
		return ResponseEntity.status(status).body(err);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ValidationError> validation(MethodArgumentNotValidException e, HttpServletRequest request){
		HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY; // 422
		ValidationError err = new ValidationError();
		err.setTimestamp(Instant.now());
		err.setStatus(status.value());
		err.setError("Exceção na validação");
		err.setMessage(e.getMessage());
		err.setPath(request.getRequestURI());
		
		for(FieldError f : e.getBindingResult().getFieldErrors()) {
			err.addError(f.getField(), f.getDefaultMessage());
		}
		
		return ResponseEntity.status(status).body(err);
	}
	
	@ExceptionHandler(ForbiddenException.class)
	public ResponseEntity<OAuthCustomError> forbidden(ForbiddenException e, HttpServletRequest request){
		OAuthCustomError err = new OAuthCustomError("Forbidden", e.getMessage());
		
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(err); //403
	}
	
	@ExceptionHandler(UnauthorizedException.class)
	public ResponseEntity<OAuthCustomError> unauthorized(UnauthorizedException e, HttpServletRequest request){
		OAuthCustomError err = new OAuthCustomError("unauthorized", e.getMessage());
		
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(err); // 401
	}
}
