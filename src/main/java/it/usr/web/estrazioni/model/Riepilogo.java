/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.usr.web.estrazioni.model;

import it.usr.web.estrazioni.domain.Mude;

/**
 *
 * @author riccardo.iovenitti
 */
public class Riepilogo extends Mude {
    private int ordinanza;
    private String tipo;

    public Riepilogo(Integer idPratica, String intestatario, String comune, String indirizzo, String tecnico, String pecTecnico, Integer ordinanza, String tipo) {
        super(idPratica, intestatario, comune, indirizzo, tecnico, pecTecnico);
        this.ordinanza = ordinanza;
        this.tipo = tipo;
    }
    
    public int getOrdinanza() {
        return ordinanza;
    }

    public void setOrdinanza(int ordinanza) {
        this.ordinanza = ordinanza;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }        
}
