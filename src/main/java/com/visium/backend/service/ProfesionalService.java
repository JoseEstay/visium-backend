package com.visium.backend.service;

import com.visium.backend.dto.profesional.ProfesionalRequest;
import com.visium.backend.dto.profesional.ProfesionalResponse;
import com.visium.backend.entity.Empresa;
import com.visium.backend.entity.Profesional;
import com.visium.backend.entity.Rol;
import com.visium.backend.entity.Sucursal;
import com.visium.backend.entity.Usuario;
import com.visium.backend.entity.UsuarioEmpresa;
import com.visium.backend.entity.UsuarioEmpresaRol;
import com.visium.backend.entity.UsuarioSucursal;
import com.visium.backend.exception.BadRequestException;
import com.visium.backend.exception.ResourceNotFoundException;
import com.visium.backend.repository.EmpresaRepository;
import com.visium.backend.repository.ProfesionalRepository;
import com.visium.backend.repository.RolRepository;
import com.visium.backend.repository.SucursalRepository;
import com.visium.backend.repository.UsuarioEmpresaRepository;
import com.visium.backend.repository.UsuarioEmpresaRolRepository;
import com.visium.backend.repository.UsuarioRepository;
import com.visium.backend.repository.UsuarioSucursalRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Registro de profesionales segun reglas de negocio: 1) usuario 2) usuarios_empresas 3) rol
 * PROFESIONAL 4) profesionales 5) usuarios_sucursales Todo en una sola transaccion.
 */
@Service
@RequiredArgsConstructor
public class ProfesionalService {

  private final UsuarioRepository usuarioRepository;
  private final EmpresaRepository empresaRepository;
  private final UsuarioEmpresaRepository usuarioEmpresaRepository;
  private final RolRepository rolRepository;
  private final UsuarioEmpresaRolRepository usuarioEmpresaRolRepository;
  private final ProfesionalRepository profesionalRepository;
  private final SucursalRepository sucursalRepository;
  private final UsuarioSucursalRepository usuarioSucursalRepository;
  private final PasswordEncoder passwordEncoder;

  @Transactional(readOnly = true)
  public List<ProfesionalResponse> listar() {
    return profesionalRepository.findAll().stream().map(this::toResponse).toList();
  }

  @Transactional(readOnly = true)
  public ProfesionalResponse obtenerPorId(UUID id) {
    Profesional profesional =
        profesionalRepository
            .findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Profesional no encontrado: " + id));
    return toResponse(profesional);
  }

  @Transactional
  public ProfesionalResponse registrar(ProfesionalRequest request) {
    Empresa empresa =
        empresaRepository
            .findById(request.getEmpresaId())
            .orElseThrow(
                () ->
                    new ResourceNotFoundException(
                        "Empresa no encontrada: " + request.getEmpresaId()));

    usuarioRepository
        .findByEmailIgnoreCase(request.getEmail())
        .ifPresent(
            u -> {
              throw new BadRequestException(
                  "Ya existe un usuario con el email " + request.getEmail());
            });

    profesionalRepository
        .findByNumeroRegistro(request.getNumeroRegistro())
        .ifPresent(
            p -> {
              throw new BadRequestException(
                  "Ya existe un profesional con el registro " + request.getNumeroRegistro());
            });

    // 1) Usuario
    Usuario usuario = new Usuario();
    usuario.setNombre(request.getNombre());
    usuario.setApellido(request.getApellido());
    usuario.setEmail(request.getEmail());
    usuario.setRun(request.getRun());
    usuario.setTelefono(request.getTelefono());
    usuario.setPasswordHash(passwordEncoder.encode(request.getPassword()));
    usuario.setActivo(true);
    usuario = usuarioRepository.save(usuario);

    // 2) Pertenencia a la empresa
    UsuarioEmpresa pertenencia = new UsuarioEmpresa();
    pertenencia.setUsuario(usuario);
    pertenencia.setEmpresa(empresa);
    pertenencia.setActivo(true);
    pertenencia = usuarioEmpresaRepository.save(pertenencia);

    // 3) Rol PROFESIONAL
    Rol rolProfesional =
        rolRepository
            .findByCodigo("PROFESIONAL")
            .orElseThrow(
                () ->
                    new ResourceNotFoundException("Rol PROFESIONAL no existe en la base de datos"));
    UsuarioEmpresaRol asignacionRol = new UsuarioEmpresaRol();
    asignacionRol.setUsuarioEmpresa(pertenencia);
    asignacionRol.setRol(rolProfesional);
    usuarioEmpresaRolRepository.save(asignacionRol);

    // 4) Perfil profesional
    Profesional profesional = new Profesional();
    profesional.setUsuario(usuario);
    profesional.setNumeroRegistro(request.getNumeroRegistro());
    profesional.setEspecialidad(request.getEspecialidad());
    profesional.setActivo(true);
    profesional = profesionalRepository.save(profesional);

    // 5) Sucursales donde trabaja
    List<UUID> sucursalIds = new ArrayList<>();
    for (UUID sucursalId : request.getSucursalIds()) {
      Sucursal sucursal =
          sucursalRepository
              .findById(sucursalId)
              .orElseThrow(
                  () -> new ResourceNotFoundException("Sucursal no encontrada: " + sucursalId));

      if (!sucursal.getEmpresa().getId().equals(empresa.getId())) {
        throw new BadRequestException("La sucursal " + sucursalId + " no pertenece a la empresa");
      }

      UsuarioSucursal usuarioSucursal = new UsuarioSucursal();
      usuarioSucursal.setUsuarioEmpresaId(pertenencia.getId());
      usuarioSucursal.setSucursalId(sucursal.getId());
      usuarioSucursal.setEmpresaId(empresa.getId());
      usuarioSucursalRepository.save(usuarioSucursal);
      sucursalIds.add(sucursal.getId());
    }

    return ProfesionalResponse.builder()
        .id(profesional.getId())
        .usuarioId(usuario.getId())
        .empresaId(empresa.getId())
        .nombre(usuario.getNombre())
        .apellido(usuario.getApellido())
        .email(usuario.getEmail())
        .numeroRegistro(profesional.getNumeroRegistro())
        .especialidad(profesional.getEspecialidad())
        .activo(profesional.getActivo())
        .sucursalIds(sucursalIds)
        .build();
  }

  private ProfesionalResponse toResponse(Profesional profesional) {
    Usuario usuario = profesional.getUsuario();
    List<UsuarioEmpresa> pertenencias = usuarioEmpresaRepository.findByUsuarioId(usuario.getId());
    UUID empresaId = pertenencias.isEmpty() ? null : pertenencias.getFirst().getEmpresa().getId();

    List<UUID> sucursalIds = new ArrayList<>();
    if (!pertenencias.isEmpty()) {
      sucursalIds =
          usuarioSucursalRepository.findByUsuarioEmpresaId(pertenencias.getFirst().getId()).stream()
              .map(UsuarioSucursal::getSucursalId)
              .toList();
    }

    return ProfesionalResponse.builder()
        .id(profesional.getId())
        .usuarioId(usuario.getId())
        .empresaId(empresaId)
        .nombre(usuario.getNombre())
        .apellido(usuario.getApellido())
        .email(usuario.getEmail())
        .numeroRegistro(profesional.getNumeroRegistro())
        .especialidad(profesional.getEspecialidad())
        .activo(profesional.getActivo())
        .sucursalIds(sucursalIds)
        .build();
  }
}
