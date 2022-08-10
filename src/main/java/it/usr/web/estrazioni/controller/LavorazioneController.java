/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.usr.web.estrazioni.controller;

import it.usr.web.estrazioni.domain.Dettaglio;
import it.usr.web.estrazioni.domain.Estrazione;
import it.usr.web.estrazioni.domain.LavCampione;
import it.usr.web.estrazioni.domain.LavPratica;
import it.usr.web.estrazioni.producer.EstrazioniLogger;
import it.usr.web.estrazioni.service.EstrazioniService;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.model.DualListModel;
import org.slf4j.Logger;

/**
 *
 * @author riccardo.iovenitti
 */
@Named
@ViewScoped
public class LavorazioneController extends BaseController {
    @Inject
    EstrazioniService es;
    @EstrazioniLogger
    @Inject
    Logger log;    
    DualListModel<Integer> ord100A;
    DualListModel<Integer> ord100B;
    DualListModel<Integer> ord100C;
    DualListModel<Integer> ord59A;
    DualListModel<Integer> ord59B;
    DualListModel<Integer> ord59C;
    boolean ord100ANote = false;
    boolean ord100BNote = false;
    boolean ord100CNote = false;
    boolean ord59ANote = false;
    boolean ord59BNote = false;
    boolean ord59CNote = false;
    int ord100ANum = 0;
    int ord100BNum = 0;
    int ord100CNum = 0;
    int ord59ANum = 0;
    int ord59BNum = 0;
    int ord59CNum = 0;
    boolean presente;
    private Date dataEstrazione;
    private String annoMese;
    
    public void initialize() {
        Estrazione e = es.getEstrazioneNonStoricizzata();
        if(e!=null) {
            annoMese = e.getAnnomese();
            dataEstrazione = e.getDataestrazione();
            
            ord100A = new DualListModel<>();
            ord100B = new DualListModel<>();
            ord100C = new DualListModel<>();
            ord59A = new DualListModel<>();
            ord59B = new DualListModel<>();
            ord59C = new DualListModel<>();

            Dettaglio d = es.getDettaglioEstrazione(e.getId(), 100, "A");
            List<LavCampione> llc = es.getLavorazioneCampione(100, "A");
            List<LavPratica> llp = es.getLavorazionePratica(100, "A");
            popolaList(ord100A, llc, llp);
            ord100ANum = percentuale(llc.size(), d.getPercentuale());
            ord100ANote = ord100ANum!=llp.size();

            d = es.getDettaglioEstrazione(e.getId(), 100, "B");
            llc = es.getLavorazioneCampione(100, "B");
            llp = es.getLavorazionePratica(100, "B");
            popolaList(ord100B, llc, llp);   
            ord100BNum = percentuale(llc.size(), d.getPercentuale());
            ord100BNote = ord100BNum!=llp.size();

            d = es.getDettaglioEstrazione(e.getId(), 100, "C");
            llc = es.getLavorazioneCampione(100, "C");
            llp = es.getLavorazionePratica(100, "C");
            popolaList(ord100C, llc, llp);
            ord100CNum = percentuale(llc.size(), d.getPercentuale());
            ord100CNote = ord100CNum!=llp.size();

            d = es.getDettaglioEstrazione(e.getId(), 59, "A");
            llc = es.getLavorazioneCampione(59, "A");
            llp = es.getLavorazionePratica(59, "A");
            popolaList(ord59A, llc, llp);
            ord59ANum = percentuale(llc.size(), d.getPercentuale());
            ord59ANote = ord59ANum!=llp.size();

            d = es.getDettaglioEstrazione(e.getId(), 59, "B");
            llc = es.getLavorazioneCampione(59, "B");
            llp = es.getLavorazionePratica(59, "B");
            popolaList(ord59B, llc, llp);
            ord59BNum = percentuale(llc.size(), d.getPercentuale());
            ord59BNote = ord59BNum!=llp.size();

            d = es.getDettaglioEstrazione(e.getId(), 59, "C");
            llc = es.getLavorazioneCampione(59, "C");
            llp = es.getLavorazionePratica(59, "C");
            popolaList(ord59C, llc, llp);
            ord59CNum = percentuale(llc.size(), d.getPercentuale());
            ord59CNote = ord59CNum!=llp.size();
        }
        
        presente = (e!=null);
    }

    private void popolaList(DualListModel dlm, List<LavCampione> llc, List<LavPratica> llp) {
        List<Integer> src = new ArrayList<>();
        llc.forEach(lc -> {
            src.add(lc.getIdpratica());
        });
        List<Integer> tgt = new ArrayList<>();
        llp.forEach(lp -> {
            tgt.add(lp.getIdpratica());
        });
        src.removeAll(tgt);
        dlm.setSource(src);
        dlm.setTarget(tgt);
    }
    
    public DualListModel<Integer> getOrd100A() {
        return ord100A;
    }

    public void setOrd100A(DualListModel<Integer> ord100A) {
        this.ord100A = ord100A;
    }

    public DualListModel<Integer> getOrd100B() {
        return ord100B;
    }

    public void setOrd100B(DualListModel<Integer> ord100B) {
        this.ord100B = ord100B;
    }

    public DualListModel<Integer> getOrd100C() {
        return ord100C;
    }

    public void setOrd100C(DualListModel<Integer> ord100C) {
        this.ord100C = ord100C;
    }

    public DualListModel<Integer> getOrd59A() {
        return ord59A;
    }

    public void setOrd59A(DualListModel<Integer> ord59A) {
        this.ord59A = ord59A;
    }

    public DualListModel<Integer> getOrd59B() {
        return ord59B;
    }

    public void setOrd59B(DualListModel<Integer> ord59B) {
        this.ord59B = ord59B;
    }

    public DualListModel<Integer> getOrd59C() {
        return ord59C;
    }

    public void setOrd59C(DualListModel<Integer> ord59C) {
        this.ord59C = ord59C;
    }       

    public boolean isPresente() {
        return presente;
    }

    public void setPresente(boolean presente) {
        this.presente = presente;
    }        

    public Date getDataEstrazione() {
        return dataEstrazione;
    }

    public void setDataEstrazione(Date dataEstrazione) {
        this.dataEstrazione = dataEstrazione;
    }

    public String getAnnoMese() {
        return annoMese;
    }

    public void setAnnoMese(String annoMese) {
        this.annoMese = annoMese;
    }        

    public boolean isOrd100ANote() {
        return ord100ANote;
    }

    public void setOrd100ANote(boolean ord100ANote) {
        this.ord100ANote = ord100ANote;
    }

    public boolean isOrd100BNote() {
        return ord100BNote;
    }

    public void setOrd100BNote(boolean ord100BNote) {
        this.ord100BNote = ord100BNote;
    }

    public boolean isOrd100CNote() {
        return ord100CNote;
    }

    public void setOrd100CNote(boolean ord100CNote) {
        this.ord100CNote = ord100CNote;
    }

    public boolean isOrd59ANote() {
        return ord59ANote;
    }

    public void setOrd59ANote(boolean ord59ANote) {
        this.ord59ANote = ord59ANote;
    }

    public boolean isOrd59BNote() {
        return ord59BNote;
    }

    public void setOrd59BNote(boolean ord59BNote) {
        this.ord59BNote = ord59BNote;
    }

    public boolean isOrd59CNote() {
        return ord59CNote;
    }

    public void setOrd59CNote(boolean ord59CNote) {
        this.ord59CNote = ord59CNote;
    }

    public int getOrd100ANum() {
        return ord100ANum;
    }

    public void setOrd100ANum(int ord100ANum) {
        this.ord100ANum = ord100ANum;
    }

    public int getOrd100BNum() {
        return ord100BNum;
    }

    public void setOrd100BNum(int ord100BNum) {
        this.ord100BNum = ord100BNum;
    }

    public int getOrd100CNum() {
        return ord100CNum;
    }

    public void setOrd100CNum(int ord100CNum) {
        this.ord100CNum = ord100CNum;
    }

    public int getOrd59ANum() {
        return ord59ANum;
    }

    public void setOrd59ANum(int ord59ANum) {
        this.ord59ANum = ord59ANum;
    }

    public int getOrd59BNum() {
        return ord59BNum;
    }

    public void setOrd59BNum(int ord59BNum) {
        this.ord59BNum = ord59BNum;
    }

    public int getOrd59CNum() {
        return ord59CNum;
    }

    public void setOrd59CNum(int ord59CNum) {
        this.ord59CNum = ord59CNum;
    }
            
    public void salva() {
        dbSave();
        
        FacesMessage msg = new FacesMessage();
        msg.setSeverity(FacesMessage.SEVERITY_INFO);
        msg.setSummary("Esito");
        msg.setDetail("Salvataggio completato!");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
    
    public String concludi() {
        dbSave();
        es.storicizza();
        
        return redirect("estrazioni");
    }  
    
    private void dbSave() {
        //java.sql.Date _de = new java.sql.Date(dataEstrazione.getTime());
        
        es.ripulisciEstrazione();
        es.salvaLavoro(ord100A.getSource(), ord100A.getTarget(), annoMese, 100, "A");
        es.salvaLavoro(ord100B.getSource(), ord100B.getTarget(), annoMese, 100, "B");
        es.salvaLavoro(ord100C.getSource(), ord100C.getTarget(), annoMese, 100, "C");
        es.salvaLavoro(ord59A.getSource(), ord59A.getTarget(), annoMese, 59, "A");
        es.salvaLavoro(ord59B.getSource(), ord59B.getTarget(), annoMese, 59, "B");
        es.salvaLavoro(ord59C.getSource(), ord59C.getTarget(), annoMese, 59, "C"); 
    }
    
    public String elimina() {
        es.elimina();
        
        return redirect("estrazioni");
    }
}
