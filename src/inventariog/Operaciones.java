/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inventariog;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;

/**
 *
 * @author José Domingo
 */
public class Operaciones {

    private ArrayList<Articulo> listaBase = new ArrayList<>();
    private ArrayList<Articulo> listaContados = new ArrayList<>();
    private ArrayList<String> agregaContado = new ArrayList<>();
    private int totalPorArticulo = 0;
    private int contador = 0;
    private String ean, codigo, desripcion, zona;
    private ArrayList<ArticuloContado> nuevaLista = new ArrayList<>();
    private int contadorZona = 0;
    
    private ArrayList<Integer> indicesRevision = new ArrayList<>();

    public void establecerZona(String zona) {
        contadorZona = 0;
        if (zona != null) {
            this.zona = zona;
        } else {
            this.zona = "";
        }
    }

    public ArrayList<Articulo> leerArchivoBase(String ruta) {
        FileInputStream excelStream = null;
        try {
            excelStream = new FileInputStream(ruta);

            HSSFWorkbook libro = new HSSFWorkbook(excelStream);
            HSSFSheet hoja = libro.getSheetAt(0);
            HSSFRow fila;
            int filas = hoja.getLastRowNum() + 1;
            int columnas = 0;
            String codigo = "";
            String desc = "";
            int cantidad = 0;
            String lote = "";
            String ean = "";

            for (int r = 0 + 1; r < filas; r++) {
                fila = hoja.getRow(r);
                if (fila == null) {
                    break;
                } else {
                    for (int c = 0; c < (columnas = fila.getLastCellNum()); c++) {
                        codigo = fila.getCell(columnas - 5).getStringCellValue();
                        desc = fila.getCell(columnas - 4).getStringCellValue();
                        cantidad = (int) fila.getCell(columnas - 3).getNumericCellValue();
                        lote = fila.getCell(columnas - 2).getStringCellValue();
                        ean = fila.getCell(columnas - 1).getStringCellValue();
                    }
                    listaBase.add(new Articulo(codigo, desc, cantidad, lote, ean));
                    listaContados.add(new Articulo(codigo, desc, 0, lote, ean));
                }
            }
            System.out.println("***********************BASE CARGADA*******************************************");
        } catch (FileNotFoundException fnfe) {
            System.out.println("archivo no encontrado");
        } catch (IOException e) {
            System.out.println("Error al procesar");
            JOptionPane.showMessageDialog(null, "Archivo incorrecto", "Error en el archivo", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                excelStream.close();
            } catch (IOException e) {
                System.out.println("Error al procesar");
            }
        }
        return listaBase;
    }

    public void conteoDeArticulos(String contado) {
        boolean encontrado = false;
        int i = 0;
        for (i = 0; i < listaContados.size(); i++) {
            if (listaContados.get(i).getCodigo().equals(contado) || listaContados.get(i).getEan().equals(contado)) {
                encontrado = true;
                System.out.println("es igual" + encontrado);
                break;
            }
        }
        if (encontrado) {
            contador += 1;
            listaContados.get(i).setCantidad(listaContados.get(i).getCantidad() + 1);
            totalPorArticulo = listaContados.get(i).getCantidad();
            ean = listaContados.get(i).getEan();
            codigo = listaContados.get(i).getCodigo();
            desripcion = listaContados.get(i).getDescripcion();
            nuevaLista.add(new ArticuloContado(contador, ean, codigo, desripcion, zona));
            System.out.println("tamano de nueva lista" + nuevaLista.size());
            contadorZona += 1;
            agregaContado.add(contado);
        } else {
            JOptionPane.showMessageDialog(null, "Codigo de barras o artículo incorrecto", "Verificar conteo", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void generaReporteConteo() throws FileNotFoundException, IOException {

        Workbook wb = new HSSFWorkbook();
        CreationHelper createHelper = wb.getCreationHelper();
        org.apache.poi.ss.usermodel.Sheet hoja1 = wb.createSheet("Reporte Conteo");

        Row fila1 = hoja1.createRow(0);
        fila1.createCell(0).setCellValue("Número");
        fila1.createCell(1).setCellValue("Codigo de barras");
        fila1.createCell(2).setCellValue("Codigo");
        fila1.createCell(3).setCellValue("Descripcion");
        fila1.createCell(4).setCellValue("Zona");
        int rowNum = 1;
        int numero = 1;
        for (int i = 0; i < nuevaLista.size(); i++) {

            Row row = hoja1.createRow(rowNum++);
            row.createCell(0).setCellValue(numero++);
            row.createCell(1).setCellValue(nuevaLista.get(i).getEan());
            row.createCell(2).setCellValue(nuevaLista.get(i).getCodigo());
            row.createCell(3).setCellValue(nuevaLista.get(i).getDescripcion());
            row.createCell(4).setCellValue(nuevaLista.get(i).getZona());
        }
        String archivo = "Reporte de Conteo " + JOptionPane.showInputDialog("Marca: ");
        FileOutputStream fileOut = new FileOutputStream(archivo + ".xls");
        wb.write(fileOut);
        fileOut.close();
        JOptionPane.showMessageDialog(null, "Reporte creado");
    }

    public void gererarReporteDiferencias() throws IOException {

        String marca = "";
        int diferencia = 0;
        Workbook wb = new HSSFWorkbook();
        CreationHelper createHelper = wb.getCreationHelper();
        org.apache.poi.ss.usermodel.Sheet hoja1 = wb.createSheet("Reporte");

        Row fila1 = hoja1.createRow(0);
        fila1.createCell(0).setCellValue("Número");
        fila1.createCell(1).setCellValue("Clave");
        fila1.createCell(2).setCellValue("Descripción");
        fila1.createCell(3).setCellValue("Físico");
        fila1.createCell(4).setCellValue("Sistema");
        fila1.createCell(5).setCellValue("Diferencia");
        fila1.createCell(6).setCellValue("Observación");

        int rowNum = 1;
        int numero = 1;
        int n1 = 0, n2 = 0;
        for (int i = 0; i < listaContados.size(); i++) {

            Row row = hoja1.createRow(rowNum++);
            row.createCell(0).setCellValue(numero++);
            row.createCell(1).setCellValue(listaContados.get(i).getCodigo());
            row.createCell(2).setCellValue(listaContados.get(i).getDescripcion());
            n1 = listaContados.get(i).getCantidad();
            row.createCell(3).setCellValue(n1);
            n2 = listaBase.get(i).getCantidad();
            row.createCell(4).setCellValue(n2);
            diferencia = listaContados.get(i).getCantidad() - listaBase.get(i).getCantidad();
            row.createCell(5).setCellValue(diferencia);
            if (diferencia < 0) {
                row.createCell(6).setCellValue(" Faltante");
                indicesRevision.add(i);
            } else {
                if (diferencia > 0) {
                    row.createCell(6).setCellValue(" Sobrante");
                    indicesRevision.add(i);
                }
            }
        }
        marca = JOptionPane.showInputDialog("Marca:");
        String archivo = "Reporte de Inventario " + marca;
        System.out.println("Reporte creado");
        FileOutputStream fileOut = new FileOutputStream(archivo + ".xls");
        wb.write(fileOut);
        fileOut.close();
    }

    public void generarReportePdf() throws FileNotFoundException, DocumentException {
        Document documento = new Document();
        FileOutputStream pdf = new FileOutputStream("Reporte.pdf");
        PdfWriter.getInstance(documento, pdf).setInitialLeading(20);
        documento.open();
        try {
            Image foto = Image.getInstance("logo.png");
            foto.scaleToFit(150, 150);
            foto.setAlignment(Chunk.ALIGN_RIGHT);
            documento.add(foto);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Paragraph titulo = new Paragraph();
        titulo.setAlignment(Element.ALIGN_CENTER);
        titulo.setFont(FontFactory.getFont("Times New Roman", 18, Font.BOLD, BaseColor.RED));
        titulo.add("Resultado final de Inventario");
        documento.add(titulo);

        Paragraph saltolinea1 = new Paragraph();
        saltolinea1.add("\n\n");
        documento.add(saltolinea1);

        Paragraph totalArts = new Paragraph();
        totalArts.setAlignment(Chunk.ALIGN_CENTER);
        totalArts.setFont(FontFactory.getFont("Times New Roman", 16, Font.BOLD, BaseColor.CYAN));
        totalArts.add(String.valueOf(nuevaLista.size()));
        documento.add(totalArts);

        PdfPTable tabla = new PdfPTable(3);
        tabla.setWidthPercentage(100);
        tabla.setWidths(new float[]{20, 40, 40});

        Paragraph columna1 = new Paragraph("Numero");
        columna1.getFont().setStyle(Font.NORMAL);
        columna1.getFont().setSize(10);
        tabla.addCell(columna1);

        Paragraph columna2 = new Paragraph("Artículo");
        columna1.getFont().setStyle(Font.NORMAL);
        columna1.getFont().setSize(10);
        tabla.addCell(columna2);

        Paragraph columna3 = new Paragraph("Cantidad");
        columna1.getFont().setStyle(Font.NORMAL);
        columna1.getFont().setSize(10);
        tabla.addCell(columna3);

        for (int i = 0; i < nuevaLista.size(); i++) {
            tabla.addCell(String.valueOf(i + 1));
            tabla.addCell(nuevaLista.get(i).getCodigo());
            tabla.addCell(String.valueOf(nuevaLista.get(i).getZona()));
        }
        documento.add(tabla);
        documento.close();
        JOptionPane.showMessageDialog(null, "Se ha creado el reporte", "Reporte Final", JOptionPane.INFORMATION_MESSAGE);
    }

    public ArrayList<String> getAgregaContado() {
        return agregaContado;
    }

    public void setAgregaContado(ArrayList<String> agregaContado) {
        this.agregaContado = agregaContado;
    }

    public int getTotalPorArticulo() {
        return totalPorArticulo;
    }

    public void setTotalPorArticulo(int totalPorArticulo) {
        this.totalPorArticulo = totalPorArticulo;
    }

    public ArrayList<ArticuloContado> getNuevaLista() {
        return nuevaLista;
    }

    public void setNuevaLista(ArrayList<ArticuloContado> nuevaLista) {
        this.nuevaLista = nuevaLista;
    }

    public int getContadorZona() {
        return contadorZona;
    }

    public void setContadorZona(int contadorZona) {
        this.contadorZona = contadorZona;
    }

    public ArrayList<Articulo> getListaContados() {
        return listaContados;
    }

    public void setListaContados(ArrayList<Articulo> listaContados) {
        this.listaContados = listaContados;
    }

    public ArrayList<Integer> getIndicesRevision() {
        return indicesRevision;
    }

    public void setIndicesRevision(ArrayList<Integer> indicesRevision) {
        this.indicesRevision = indicesRevision;
    }

    public ArrayList<Articulo> getListaBase() {
        return listaBase;
    }

    public void setListaBase(ArrayList<Articulo> listaBase) {
        this.listaBase = listaBase;
    }

}
