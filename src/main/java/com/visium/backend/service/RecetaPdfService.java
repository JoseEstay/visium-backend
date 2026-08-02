package com.visium.backend.service;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.visium.backend.entity.RecetaOptica;
import com.visium.backend.entity.RecetaOpticaDetalle;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;

@Service
public class RecetaPdfService {

    public byte[] generarPdf(RecetaOptica receta) {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            // ==========================================
            // 1. DATOS DINÁMICOS (EMPRESA Y SUCURSAL)
            // ==========================================
            String nombreEmpresa = receta.getConsulta().getCita().getSucursal().getEmpresa().getRazonSocial();
            String nombreSucursal = receta.getConsulta().getCita().getSucursal().getNombre();

            // Título Principal (La Empresa)
            Font fontTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Paragraph titulo = new Paragraph("Receta Óptica - " + nombreEmpresa, fontTitulo);
            titulo.setAlignment(Element.ALIGN_CENTER);
            document.add(titulo);

            // Subtítulo (La Sucursal)
            Font fontSubtitulo = FontFactory.getFont(FontFactory.HELVETICA, 12);
            Paragraph subtitulo = new Paragraph("Emitida en: " + nombreSucursal, fontSubtitulo);
            subtitulo.setAlignment(Element.ALIGN_CENTER);
            subtitulo.setSpacingAfter(20); // Espacio antes de los datos del paciente
            document.add(subtitulo);
            // ==========================================

            // Datos del Paciente (obtenidos a través de la Cita)
            String nombrePaciente = receta.getConsulta().getCita().getPaciente().getNombre() + " " +
                    receta.getConsulta().getCita().getPaciente().getApellido();
            document.add(new Paragraph("Paciente: " + nombrePaciente));
            document.add(new Paragraph("Fecha de Emisión: " + receta.getFechaEmision().format(DateTimeFormatter.ISO_LOCAL_DATE)));
            if (receta.getVigenciaHasta() != null) {
                document.add(new Paragraph("Vigencia Hasta: " + receta.getVigenciaHasta().format(DateTimeFormatter.ISO_LOCAL_DATE)));
            }
            document.add(new Paragraph("\n"));

            // Tabla de Detalles (OD / OI)
            PdfPTable table = new PdfPTable(7); // 7 columnas
            table.setWidthPercentage(100);

            // Encabezados
            String[] headers = {"Ojo", "Esfera", "Cilindro", "Eje", "Prisma", "Base", "Agudeza V."};
            for (String header : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(header, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10)));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
            }

            // Filas
            for (RecetaOpticaDetalle detalle : receta.getDetalles()) {
                table.addCell(detalle.getOjo().name());
                table.addCell(detalle.getEsfera() != null ? detalle.getEsfera().toString() : "");
                table.addCell(detalle.getCilindro() != null ? detalle.getCilindro().toString() : "");
                table.addCell(detalle.getEje() != null ? detalle.getEje().toString() : "");
                table.addCell(detalle.getPrisma() != null ? detalle.getPrisma().toString() : "");
                table.addCell(detalle.getBasePrisma() != null ? detalle.getBasePrisma() : "");
                table.addCell(detalle.getAgudezaVisual() != null ? detalle.getAgudezaVisual() : "");
            }
            document.add(table);
            document.add(new Paragraph("\n"));

            // Medidas adicionales
            if (receta.getAdicion() != null) {
                document.add(new Paragraph("Adición: " + receta.getAdicion()));
            }
            if (receta.getDistanciaPupilar() != null) {
                document.add(new Paragraph("Distancia Pupilar: " + receta.getDistanciaPupilar() + " mm"));
            }

            // Indicaciones y Observaciones
            if (receta.getIndicaciones() != null && !receta.getIndicaciones().isBlank()) {
                document.add(new Paragraph("\nIndicaciones:\n" + receta.getIndicaciones()));
            }
            if (receta.getObservaciones() != null && !receta.getObservaciones().isBlank()) {
                document.add(new Paragraph("\nObservaciones:\n" + receta.getObservaciones()));
            }

            document.close();
        } catch (Exception e) {
            throw new RuntimeException("Error al generar el PDF de la receta", e);
        }

        return out.toByteArray();
    }
}
