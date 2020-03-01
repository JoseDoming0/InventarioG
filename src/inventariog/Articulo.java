/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inventariog;

/**
 *
 * @author José Domingo
 */
public class Articulo {
	
	private String codigo;
	private String descripcion;
	private int cantidad;
	private String lote;
	private String ean;
	
	public Articulo() {
		super();
		}

	public Articulo(String codigo, String descripcion, int cantidad,
			String lote, String ean) {
		super();
		this.codigo = codigo;
		this.descripcion = descripcion;
		this.cantidad = cantidad;
		this.lote = lote;
		this.ean = ean;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public int getCantidad() {
		return cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	public String getLote() {
		return lote;
	}

	public void setLote(String lote) {
		this.lote = lote;
	}

	public String getEan() {
		return ean;
	}

	public void setEan(String ean) {
		this.ean = ean;
	}		
}
