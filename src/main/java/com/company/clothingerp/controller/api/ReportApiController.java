package com.company.clothingerp.controller.api;

import com.company.clothingerp.model.Report;
import com.company.clothingerp.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
@Tag(name = "Reports & Analytics (ERP/CRM)", description = "Endpoints for compiling and downloading financial, inventory, and sales reports")
public class ReportApiController {

    @Autowired
    private ReportService reportService;

    @GetMapping
    @Operation(summary = "Get list of all system reports")
    public List<Report> getAllReports() {
        return reportService.getAllReports();
    }

    @GetMapping("/export/pdf")
    @Operation(summary = "Download a PDF report document (Simulated PDF download)")
    public ResponseEntity<byte[]> downloadPdfReport(@RequestParam String type) {
        String mockPdfContent = "%PDF-1.4\n" +
                "1 0 obj < < /Type /Catalog /Pages 2 0 R > > endobj\n" +
                "2 0 obj < < /Type /Pages /Kids [ 3 0 R ] /Count 1 > > endobj\n" +
                "3 0 obj < < /Type /Page /Parent 2 0 R /MediaBox [ 0 0 595 842 ] /Contents 4 0 R > > endobj\n" +
                "4 0 obj < < /Length 100 > > stream\n" +
                "BT /F1 24 Tf 70 700 Td (Clothing ERP Corporate Report - " + type.toUpperCase() + ") Tj ET\n" +
                "BT /F1 12 Tf 70 650 Td (Generated on: 2026-06-04. Status: Confirmed.) Tj ET\n" +
                "endstream endobj\n" +
                "xref\n" +
                "0 5\n" +
                "0000000000 65535 f\n" +
                "0000000009 00000 n\n" +
                "0000000056 00000 n\n" +
                "0000000111 00000 n\n" +
                "0000000212 00000 n\n" +
                "trailer < < /Size 5 /Root 1 0 R > >\n" +
                "startxref\n" +
                "365\n" +
                "%%EOF";

        byte[] pdfBytes = mockPdfContent.getBytes(StandardCharsets.UTF_8);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"report_" + type + "_" + System.currentTimeMillis() + ".pdf\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }

    @GetMapping("/export/excel")
    @Operation(summary = "Download an Excel/CSV spreadsheet report (Fully formatted CSV content)")
    public ResponseEntity<byte[]> downloadExcelReport(@RequestParam String type) {
        StringBuilder csvContent = new StringBuilder();
        csvContent.append("Clothing ERP - Corporate ").append(type.toUpperCase()).append(" Report\n");
        csvContent.append("Generated Date,2026-06-04\n");
        csvContent.append("Status,Active\n\n");
        csvContent.append("ID,SKU/Code,Name,Category,Stock/Sales Count,Unit Price,Total Cost,Total Revenue\n");
        csvContent.append("1,OUT-DEN-01,Turkish Denim Jacket,Outerwear,570,$60.0,$14250.0,$34200.0\n");
        csvContent.append("2,MEN-JEAN-02,Slim Fit Black Jeans,Menswear,13,$35.0,$195.0,$455.0\n");
        csvContent.append("3,WOM-DRES-03,Floral Summer Dress,Womenswear,206,$45.0,$3708.0,$9270.0\n");
        csvContent.append("4,MEN-POLO-04,Classic Pique Polo,Menswear,800,$22.0,$6400.0,$17600.0\n");
        csvContent.append("5,ACC-BELT-05,Genuine Leather Belt,Accessories,3,$15.0,$15.0,$45.0\n");

        byte[] csvBytes = csvContent.toString().getBytes(StandardCharsets.UTF_8);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"report_" + type + "_" + System.currentTimeMillis() + ".csv\"")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(csvBytes);
    }
}
