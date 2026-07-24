package com.visium.backend.mapper;

import com.visium.backend.dto.empresa.EmpresaRequest;
import com.visium.backend.dto.empresa.EmpresaResponse;
import com.visium.backend.entity.Empresa;
import org.springframework.stereotype.Component;

/**
 * Convierte entre Empresa (entidad) y sus DTOs.
 */
@Component
public class EmpresaMapper {

	public Empresa toEntity(EmpresaRequest request) {
		Empresa empresa = new Empresa();
		aplicar(empresa, request);
		return empresa;
	}

	public void aplicar(Empresa empresa, EmpresaRequest request) {
		empresa.setRut(request.getRut());
		empresa.setRazonSocial(request.getRazonSocial());
		empresa.setEmail(request.getEmail());
		if (request.getActivo() != null) {
			empresa.setActivo(request.getActivo());
		}
	}

	public EmpresaResponse toResponse(Empresa empresa) {
		return EmpresaResponse.builder()
				.id(empresa.getId())
				.rut(empresa.getRut())
				.razonSocial(empresa.getRazonSocial())
				.email(empresa.getEmail())
				.activo(empresa.getActivo())
				.createdAt(empresa.getCreatedAt())
				.updatedAt(empresa.getUpdatedAt())
				.build();
	}
}
