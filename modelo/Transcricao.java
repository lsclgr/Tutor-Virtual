/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

/**
 *
 * @author IFMG
 */
public class Transcricao {
    //Atributos.
    private String textoTranscricao;
    private int indiceAlternativaReferente;
    private double porcentagemAcertos;
    //Construtores.
    public Transcricao(String textoTranscricao, int indiceAlternativaReferente) {
        this.textoTranscricao=textoTranscricao;
        this.indiceAlternativaReferente=indiceAlternativaReferente;
    }
    //Encapsulamento.
    public String getTextoTranscricao() {
        return textoTranscricao;
    }

    public void setTextoTranscricao(String transcricao) {
        this.textoTranscricao = transcricao;
    }

    public int getIndiceAlternativaReferente() {
        return indiceAlternativaReferente;
    }

    public void setIndiceAlternativaReferente(int indiceAlternativaReferente) {
        this.indiceAlternativaReferente = indiceAlternativaReferente;
    }

    public double getPorcentagemAcertos() {
        return porcentagemAcertos;
    }

    public void setPorcentagemAcertos(double porcentagemAcertos) {
        this.porcentagemAcertos = porcentagemAcertos;
    }
    

    @Override
    public String toString() {
        return this.textoTranscricao;
    }
    
}
