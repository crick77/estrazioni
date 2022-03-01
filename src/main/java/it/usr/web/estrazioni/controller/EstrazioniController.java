/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.usr.web.estrazioni.controller;

import it.usr.web.estrazioni.domain.Campione;
import it.usr.web.estrazioni.domain.Dettaglio;
import it.usr.web.estrazioni.domain.Estrazione;
import it.usr.web.estrazioni.domain.Pratica;
import it.usr.web.estrazioni.producer.EstrazioniLogger;
import it.usr.web.estrazioni.service.EstrazioniService;
import java.util.List;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.slf4j.Logger;

/**
 *
 * @author riccardo.iovenitti
 */
@Named
@ViewScoped
public class EstrazioniController extends BaseController {
    @Inject
    EstrazioniService es;
    @Inject
    @EstrazioniLogger
    Logger logger;
    List<Estrazione> estrazioni;
    Integer idEstrazione;
    List<Dettaglio> dettaglio;
    Integer dettaglioSelezionato;
    List<Campione> campione;
    List<Pratica> pratiche;
    boolean estrazioniPresenti;
    
    public void initialize() {
        estrazioni = es.getEstrazioniStoricizzate();
        idEstrazione = null;     
        dettaglio = null;
        dettaglioSelezionato = null;
        campione = null;
        pratiche = null;
        estrazioniPresenti = !estrazioni.isEmpty();
    }

    public List<Estrazione> getEstrazioni() {
        return estrazioni;
    }        

    public Integer getIdEstrazione() {
        return idEstrazione;
    }

    public void setIdEstrazione(Integer idEstrazione) {
        this.idEstrazione = idEstrazione;
    }       

    public List<Dettaglio> getDettaglio() {
        return dettaglio;
    }

    public void setDettaglio(List<Dettaglio> dettaglio) {
        this.dettaglio = dettaglio;
    }
    
    public void aggiornaDettaglio() {
        dettaglio = es.getDettaglioEstrazione(idEstrazione);
        dettaglioSelezionato = null;
        campione = null;
        pratiche = null;
    }

    public Integer getDettaglioSelezionato() {
        return dettaglioSelezionato;
    }

    public void setDettaglioSelezionato(Integer dettaglioSelezionato) {
        this.dettaglioSelezionato = dettaglioSelezionato;
    }

    public List<Campione> getCampione() {
        return campione;
    }

    public void setCampione(List<Campione> campione) {
        this.campione = campione;
    }

    public List<Pratica> getPratiche() {
        return pratiche;
    }

    public void setPratiche(List<Pratica> pratiche) {
        this.pratiche = pratiche;
    }                

    public boolean isEstrazioniPresenti() {
        return estrazioniPresenti;
    }

    public void setEstrazioniPresenti(boolean estrazioniPresenti) {
        this.estrazioniPresenti = estrazioniPresenti;
    }
            
    public void aggiornaCampione() {
        campione = es.getCampione(dettaglioSelezionato);
        pratiche = es.getPratiche(dettaglioSelezionato);
    }
    
    public String formatDescrizione(Estrazione e) {
        if(e==null) return null;
        
        StringBuilder sb = new StringBuilder();
        sb = sb.append(e.getAnnomese().substring(4)).append("/").append(e.getAnnomese().substring(0, 4));
        sb = sb.append(" del ");
        sb = sb.append(dateFormat(e.getDataestrazione()));
        return sb.toString();        
    }
    
    public String formatDettaglio(Dettaglio d) {
        if(d==null) return null;
        
        StringBuilder sb = new StringBuilder();        
        sb = sb.append(d.getPercentuale()).append("% dell' Ord. ").append(d.getOrdinanza()).append(" tipo '").append(d.getTipo()).append("'");
        return sb.toString();        
    }
}
