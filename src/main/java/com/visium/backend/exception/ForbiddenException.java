package com.visium.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Se lanza cuando el usuario autenticado no tiene permiso sobre un recurso
 * (empresa o sucursal ajena).
 */
@ResponseStatus(HttpStatus.FORBIDDEN)
public class ForbiddenException extends RuntimeException {

	public ForbiddenException(String mensaje) {
		super(mensaje);
	}
}
