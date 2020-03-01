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
public class ArticuloContado {
    
    private int numero;
    private String ean;
    private String codigo;
    private String descripcion;
    private String zona;
    
    public ArticuloContado(){
        
    }

    public ArticuloContado(int numero, String ean, String codigo, String descripcion, String zona) {
        this.numero = numero;
        this.ean = ean;
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.zona = zona;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getEan() {
        return ean;
    }

    public void setEan(String ean) {
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

    public String getZona() {
        return zona;
    }

    public void setZona(String zona) {
        this.zona = zona;
    }
    
}
