
package com.CRUD.Empleado.controller;

import com.CRUD.Empleado.model.Empleado;
import com.CRUD.Empleado.service.EmpleadoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletResponse;

// Importaciones para PDF
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;

// Importaciones para Excel
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/empleados")
public class EmpleadoController {

    private final EmpleadoService empleadoService;

    public EmpleadoController(EmpleadoService empleadoService) {
        this.empleadoService = empleadoService;
    }

    // Listar empleados
    @GetMapping
    public String listarEmpleados(Model model) {
        model.addAttribute("empleados", empleadoService.listarTodos());
        return "list-empleados";
    }

    // Mostrar formulario para crear un nuevo empleado
    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("empleado", new Empleado());
        return "form-empleado";
    }

    // Guardar empleado
    @PostMapping("/guardar")
    public String guardarEmpleado(@ModelAttribute Empleado empleado) {
        empleadoService.guardar(empleado);
        return "redirect:/empleados";
    }

    // Mostrar formulario para editar empleado
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable int id, Model model) {
        Empleado empleado = empleadoService.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Empleado no encontrado con ID: " + id));
        model.addAttribute("empleado", empleado);
        return "form-empleado";
    }

    // Eliminar empleado
    @GetMapping("/eliminar/{id}")
    public String eliminarEmpleado(@PathVariable int id) {
        empleadoService.eliminar(id);
        return "redirect:/empleados";
    }

    // Generar reporte PDF
    @GetMapping("/reporte/pdf")
public void generarPdf(HttpServletResponse response) throws IOException {
    response.setContentType("application/pdf");
    response.setHeader("Content-Disposition", "inline; filename=empleados_reporte.pdf");

    PdfWriter writer = new PdfWriter(response.getOutputStream());
    Document document = new Document(new com.itextpdf.kernel.pdf.PdfDocument(writer));

    document.add(new Paragraph("Reporte de Empleados").setBold().setFontSize(18));

    Table table = new Table(4);
    table.addCell("ID");
    table.addCell("DNI");
    table.addCell("Nombre");
    table.addCell("Teléfono");

    List<Empleado> empleados = empleadoService.listarTodos();
    for (Empleado empleado : empleados) {
        table.addCell(String.valueOf(empleado.getId()));
        table.addCell(empleado.getDni());
        table.addCell(empleado.getNom());
        table.addCell(empleado.getTel());
    }

    document.add(table);
    document.close();
}

    // Generar reporte Excel
    @GetMapping("/reporte/excel")
public void generarExcel(HttpServletResponse response) throws IOException {
    response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    response.setHeader("Content-Disposition", "attachment; filename=empleados_reporte.xlsx");

    Workbook workbook = new XSSFWorkbook();
    Sheet sheet = workbook.createSheet("Empleados");

    Row headerRow = sheet.createRow(0);
    String[] columnHeaders = {"ID", "DNI", "Nombre", "Teléfono"};
    for (int i = 0; i < columnHeaders.length; i++) {
        Cell cell = headerRow.createCell(i);
        cell.setCellValue(columnHeaders[i]);
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        cell.setCellStyle(style);
    }

    List<Empleado> empleados = empleadoService.listarTodos();
    int rowIndex = 1;
    for (Empleado empleado : empleados) {
        Row row = sheet.createRow(rowIndex++);
        row.createCell(0).setCellValue(empleado.getId());
        row.createCell(1).setCellValue(empleado.getDni());
        row.createCell(2).setCellValue(empleado.getNom());
        row.createCell(3).setCellValue(empleado.getTel());
    }

    workbook.write(response.getOutputStream());
    workbook.close();
  }
}