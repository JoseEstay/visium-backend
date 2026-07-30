package com.visium.backend.controller;

import com.visium.backend.entity.RecetaOptica;
import com.visium.backend.exception.ResourceNotFoundException;
import com.visium.backend.repository.RecetaOpticaRepository;
import com.visium.backend.service.RecetaOpticaService;
import com.visium.backend.service.RecetaPdfService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/recetas")
@RequiredArgsConstructor
public class RecetaOpticaController {

    private final RecetaOpticaRepository recetaOpticaRepository;
    private final RecetaPdfService pdfService;
    private final RecetaOpticaService recetaOpticaService;

    @GetMapping("/paciente/{pacienteId}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'JEFE', 'PROFESIONAL')")
    public ResponseEntity<List<RecetaOptica>> historialPorPaciente(@PathVariable UUID pacienteId) {
        List<RecetaOptica> historial = recetaOpticaRepository.findHistorialByPacienteId(pacienteId);
        return ResponseEntity.ok(historial);
    }
    @GetMapping("/{id}/pdf")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'JEFE', 'RECEPCIONISTA', 'PROFESIONAL')")
    public ResponseEntity<byte[]> descargarPdf(@PathVariable UUID id) {
        RecetaOptica receta = recetaOpticaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Receta no encontrada: " + id));

        byte[] pdfBytes = pdfService.generarPdf(receta);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "Receta_" + id + ".pdf");

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'JEFE', 'PROFESIONAL')")
    public ResponseEntity<RecetaOptica> crearReceta(@RequestBody RecetaOptica receta) {
        RecetaOptica nuevaReceta = recetaOpticaService.guardarReceta(receta);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaReceta);

}
}

