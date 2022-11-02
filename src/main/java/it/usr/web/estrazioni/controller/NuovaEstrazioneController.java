/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.usr.web.estrazioni.controller;

import it.usr.web.estrazioni.domain.Dettaglio;
import it.usr.web.estrazioni.domain.Estrazione;
import it.usr.web.estrazioni.producer.EstrazioniLogger;
import it.usr.web.estrazioni.service.EstrazioniService;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
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
public class NuovaEstrazioneController extends BaseController {
    @Inject
    EstrazioniService es;
    @Inject
    @EstrazioniLogger
    Logger log;
    boolean permesso;
    String annoMese;
    Date dataEstrazione;
    int ord100APercentuale = 20;
    int ord100BPercentuale = 10;
    int ord100CPercentuale = 10;
    int ord59APercentuale = 5;
    int ord59BPercentuale = 5;
    int ord59CPercentuale = 5;    
    Random RAND = new Random();
    
    public void initialize() {        
        LocalDate _dataEstrazione = LocalDate.now();      
        LocalDate dataRiferimento = _dataEstrazione.minusMonths(1);        
        annoMese = dataRiferimento.format(DateTimeFormatter.ofPattern("YYYY"))+dataRiferimento.format(DateTimeFormatter.ofPattern("MM"));
        
        permesso = (es.getEstrazioneNonStoricizzata()==null && es.getEstrazioneAnnoMese(annoMese)==null);
        if(permesso) {                        
            dataEstrazione = java.sql.Date.valueOf(_dataEstrazione);            
        }
    }

    public boolean isPermesso() {
        return permesso;
    }

    public void setPermesso(boolean permesso) {
        this.permesso = permesso;
    }

    public String getAnnoMese() {
        return annoMese;
    }

    public void setAnnoMese(String annoMese) {
        this.annoMese = annoMese;
    }

    public Date getDataEstrazione() {
        return dataEstrazione;
    }

    public void setDataEstrazione(Date dataEstrazione) {
        this.dataEstrazione = dataEstrazione;
    }      

    public int getOrd100APercentuale() {
        return ord100APercentuale;
    }

    public void setOrd100APercentuale(int ord100APercentuale) {
        this.ord100APercentuale = ord100APercentuale;
    }

    public int getOrd100BPercentuale() {
        return ord100BPercentuale;
    }

    public void setOrd100BPercentuale(int ord100BPercentuale) {
        this.ord100BPercentuale = ord100BPercentuale;
    }

    public int getOrd100CPercentuale() {
        return ord100CPercentuale;
    }

    public void setOrd100CPercentuale(int ord100CPercentuale) {
        this.ord100CPercentuale = ord100CPercentuale;
    }

    public int getOrd59APercentuale() {
        return ord59APercentuale;
    }

    public void setOrd59APercentuale(int ord59APercentuale) {
        this.ord59APercentuale = ord59APercentuale;
    }

    public int getOrd59BPercentuale() {
        return ord59BPercentuale;
    }

    public void setOrd59BPercentuale(int ord59BPercentuale) {
        this.ord59BPercentuale = ord59BPercentuale;
    }

    public int getOrd59CPercentuale() {
        return ord59CPercentuale;
    }

    public void setOrd59CPercentuale(int ord59CPercentuale) {
        this.ord59CPercentuale = ord59CPercentuale;
    }
    
    public String effettuaEstrazione() {
        log.debug("Estrazione del "+dataEstrazione+" riferita a "+annoMese);
        int[] _am = annoMeseSplit(annoMese);
        
        List<Integer> lOrd100A = es.getProgettiPresentatiOrd100(_am[0], _am[1]);
        int perc100A = percentuale(lOrd100A.size(), ord100APercentuale);
        List<Integer> lExOrd100A = extract(lOrd100A, perc100A);
        
        List<Integer> lOrd100B = es.getProgettiDecretatiOrd100(_am[0], _am[1]);
        int perc100B = percentuale(lOrd100B.size(), ord100BPercentuale);
        List<Integer> lExOrd100B = extract(lOrd100B, perc100B);
        
        List<Integer> lOrd100C = es.getProgettiChiusiOrd100(_am[0], _am[1]);
        int perc100C = percentuale(lOrd100C.size(), ord100CPercentuale);
        List<Integer> lExOrd100C = extract(lOrd100C, perc100C);
        
        List<Integer> lOrd59A = es.getProgettiDecretatiOrd59(_am[0], _am[1]);
        int perc59A = percentuale(lOrd59A.size(), ord59APercentuale);
        List<Integer> lExOrd59A = extract(lOrd59A, perc59A);
        
        List<Integer> lOrd59B = es.getProgettiOltre50Ord59(_am[0], _am[1]);
        int perc59B = percentuale(lOrd59B.size(), ord59BPercentuale);
        List<Integer> lExOrd59B = extract(lOrd59B, perc59B);
        
        List<Integer> lOrd59C = es.getProgettiChiusiOrd59(_am[0], _am[1]);
        int perc59C = percentuale(lOrd59C.size(), ord59CPercentuale);
        List<Integer> lExOrd59C = extract(lOrd59C, perc59C);
        
        Estrazione e = new Estrazione();
        e.setAnnomese(annoMese);
        e.setDataestrazione(dataEstrazione);
        e.setUtente(getUtente().getUsername());
        e.setDettaglioList(new ArrayList<>());
        
        Dettaglio d = new Dettaglio();
        d.setOrdinanza(100);
        d.setTipo("A");
        d.setPercentuale(ord100APercentuale);
        d.setEstrazione(e);
        e.getDettaglioList().add(d);
        
        d = new Dettaglio();
        d.setOrdinanza(100);
        d.setTipo("B");
        d.setPercentuale(ord100BPercentuale);
        d.setEstrazione(e);
        e.getDettaglioList().add(d);
        
        d = new Dettaglio();
        d.setOrdinanza(100);
        d.setTipo("C");
        d.setPercentuale(ord100CPercentuale);
        d.setEstrazione(e);
        e.getDettaglioList().add(d);
        
        d = new Dettaglio();
        d.setOrdinanza(59);
        d.setTipo("A");
        d.setPercentuale(ord59APercentuale);
        d.setEstrazione(e);
        e.getDettaglioList().add(d);
        
        d = new Dettaglio();
        d.setOrdinanza(59);
        d.setTipo("B");
        d.setPercentuale(ord59BPercentuale);
        d.setEstrazione(e);
        e.getDettaglioList().add(d);
        
        d = new Dettaglio();
        d.setOrdinanza(59);
        d.setTipo("C");
        d.setPercentuale(ord59CPercentuale);
        d.setEstrazione(e);
        e.getDettaglioList().add(d);
        
        es.salvaEstrazione(e);
        
        es.salvaLavoro(lOrd100A, lExOrd100A, annoMese, 100, "A");
        es.salvaLavoro(lOrd100B, lExOrd100B, annoMese, 100, "B");
        es.salvaLavoro(lOrd100C, lExOrd100C, annoMese, 100, "C");
        es.salvaLavoro(lOrd59A, lExOrd59A, annoMese, 59, "A");
        es.salvaLavoro(lOrd59B, lExOrd59B, annoMese, 59, "B");
        es.salvaLavoro(lOrd59C, lExOrd59C, annoMese, 59, "C");        
        
        log.debug("Elenco Ord100-A: {}", lOrd100A);
        log.debug("Numero minimo Ord100-A: {}", perc100A);
        log.debug("Estratti Ord100-A: {}", lExOrd100A);
        
        log.debug("Elenco Ord100-B: {}", lOrd100B);
        log.debug("Numero minimo Ord100-B: {}", perc100B);
        log.debug("Estratti Ord100-B: {}", lExOrd100B);
        
        log.debug("Elenco Ord100-C: {}", lOrd100C);
        log.debug("Numero minimo Ord100-C: {}", perc100C);
        log.debug("Estratti Ord100-C: {}", lExOrd100C);
        
        log.debug("Elenco Ord59-A: {}", lOrd59A);
        log.debug("Numero minimo Ord59-A: {}", perc59A);
        log.debug("Estratti Ord59-A: {}", lExOrd59A);
        
        log.debug("Elenco Ord59-B: {}", lOrd59B);
        log.debug("Numero minimo Ord59-B: {}", perc59B);
        log.debug("Estratti Ord59-B: {}", lExOrd59B);
        
        log.debug("Elenco Ord59-C: {}", lOrd59C);
        log.debug("Numero minimo Ord59-C: {}", perc59C);
        log.debug("Estratti Ord59-C: {}", lExOrd59C);
        
        return redirect("lavorazione");
    }
                    
    private List<Integer> extract(List<Integer> pratiche, int toExtract) {
        HashSet<Integer> ex = new HashSet<>();
        List<Integer> considered = new ArrayList<>();
                
        int total = pratiche.size();
        if(toExtract>=total) return pratiche;
        
        // Esegue fino a quando non vengono estratte il numero di pratiche indicate
        // oppure fino a che non ho considerato tutte le pratiche
        while(ex.size()<toExtract && considered.size()<pratiche.size()) {
            Integer extracted = pratiche.get(RAND.nextInt(total));
            if(!es.estrattaMesePrecedente(extracted, annoMese)) {
                ex.add(extracted);
            }
            considered.add(extracted);
        }
        
        return new ArrayList<>(ex);
    }        
}
