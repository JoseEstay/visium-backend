package com.visium.backend.mapper;

import com.visium.backend.dto.sucursal.SucursalRequest;
import com.visium.backend.dto.sucursal.SucursalResponse;
import com.visium.backend.entity.Empresa;
import com.visium.backend.entity.Sucursal;
import org.springframework.stereotype.Component;

@Component
public class SucursalMapper {

	public Sucursal toEntity(SucursalRequest request, Empresa empresa) {
		Sucursal sucursal = new Sucursal();
		sucursal.setEmpresa(empresa);
		aplicar(sucursal, request);
		return sucursal;
	}

	public void aplicar(Sucursal sucursal, SucursalRequest request) {
		sucursal.setNombre(request.getNombre());
		sucursal.setDireccion(request.getDireccion());
		sucursal.setComuna(request.getComuna());
		sucursal.setCiudad(request.getCiudad());
		sucursal.setRegion(request.getRegion());
		sucursal.setTelefono(request.getTelefono());
		if (request.getActivo() != null) {
			sucursal.setActivo(request.getActivo());
		}
	}

	public SucursalResponse toResponse(Sucursal sucursal) {
		return SucursalResponse.builder()
				.id(sucursal.getId())
				.empresaId(sucursal.getEmpresa().getId())
				.nombre(sucursal.getNombre())
				.direccion(sucursal.getDireccion())
				.comuna(sucursal.getComuna())
				.ciudad(sucursal.getCiudad())
				.region(sucursal.getRegion())
				.telefono(sucursal.getTelefono())
				.activo(sucursal.getActivo())
				.createdAt(sucursal.getCreatedAt())
				.updatedAt(sucursal.getUpdatedAt())
				.build();
	}
}
