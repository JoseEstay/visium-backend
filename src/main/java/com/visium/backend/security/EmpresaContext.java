package com.visium.backend.security;

import java.util.UUID;

/**
 * Guarda la empresa activa del request actual (ThreadLocal).
 * Se llena desde el header X-Empresa-Id o, si el usuario solo tiene una, desde esa.
 */
public final class EmpresaContext {

	private static final ThreadLocal<UUID> EMPRESA_ACTIVA = new ThreadLocal<>();

	private EmpresaContext() {
	}

	public static void setEmpresaId(UUID empresaId) {
		EMPRESA_ACTIVA.set(empresaId);
	}

	public static UUID getEmpresaId() {
		return EMPRESA_ACTIVA.get();
	}

	public static void clear() {
		EMPRESA_ACTIVA.remove();
	}
}
