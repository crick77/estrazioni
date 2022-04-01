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
import it.usr.web.estrazioni.model.Riepilogo;
import it.usr.web.estrazioni.producer.EstrazioniLogger;
import it.usr.web.estrazioni.service.EstrazioniService;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;
import org.slf4j.Logger;

/**
 *
 * @author riccardo.iovenitti
 */
@Named
@ViewScoped
public class RiepilogoController extends BaseController {
    @Inject
    EstrazioniService es;
    @Inject
    @EstrazioniLogger
    Logger log;
    List<Estrazione> estrazioni;
    Integer idEstrazione;
    List<Riepilogo> riepilogo;
    boolean estrazioniPresenti;
    
    public void initialize() {
        estrazioni = es.getEstrazioniStoricizzate();
        idEstrazione = null;
        riepilogo = null;
        estrazioniPresenti = !estrazioni.isEmpty();
    }

    public List<Estrazione> getEstrazioni() {
        return estrazioni;
    }

    public void setEstrazioni(List<Estrazione> estrazioni) {
        this.estrazioni = estrazioni;
    }

    public List<Riepilogo> getRiepilogo() {
        return riepilogo;
    }

    public void setRiepilogo(List<Riepilogo> riepilogo) {
        this.riepilogo = riepilogo;
    }

    public Integer getIdEstrazione() {
        return idEstrazione;
    }

    public void setIdEstrazione(Integer idEstrazione) {
        this.idEstrazione = idEstrazione;
    }    

    public boolean isEstrazioniPresenti() {
        return estrazioniPresenti;
    }

    public void setEstrazioniPresenti(boolean estrazioniPresenti) {
        this.estrazioniPresenti = estrazioniPresenti;
    }
        
    public void aggiornaRiepilogo() {
        riepilogo = es.getRiepilogo(idEstrazione);
    }
    
    public void rapporto() {
        Map<String, Object> options = new HashMap<>();
        options.put("modal", true);
        options.put("width", 640);
        options.put("height", 450);
        options.put("contentWidth", "100%");
        options.put("contentHeight", "100%");  
                
        Estrazione e = es.getEstrazioneById(idEstrazione); 
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("estrazione", e);
        PrimeFaces.current().dialog().openDynamic("riepilogodati", options, null);
    }
    
    public void onDialogClose(SelectEvent event) {
        String result = (String)event.getObject();
        if("OK".equalsIgnoreCase(result)) {
            FacesMessage msg = new FacesMessage();
            msg.setSeverity(FacesMessage.SEVERITY_INFO);
            msg.setSummary("Esito");
            msg.setDetail("Rapporto generato!");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
        else if ("KO".equalsIgnoreCase(result)) {
            FacesMessage msg = new FacesMessage();
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            msg.setSummary("Esito");
            msg.setDetail("Impossibile generare il reapporto!");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
        else {
            FacesMessage msg = new FacesMessage();
            msg.setSeverity(FacesMessage.SEVERITY_INFO);
            msg.setSummary("Esito");
            msg.setDetail("Operazione annullata!");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }        
    }
    
    public String formatDescrizione(Estrazione e) {
        if(e==null) return null;
        
        StringBuilder sb = new StringBuilder();
        sb = sb.append(e.getAnnomese().substring(4)).append("/").append(e.getAnnomese().substring(0, 4));
        sb = sb.append(" del ");
        sb = sb.append(dateFormat(e.getDataestrazione()));
        return sb.toString();        
    }
    
    public String rowStyle(Riepilogo r) {
        return r.getTipo().toLowerCase()+r.getOrdinanza();
    }
    
    public String nomeFile() {
        for(Estrazione e : estrazioni) {
            if(Objects.equals(e.getId(), idEstrazione)) {
                return "Estrazione_"+e.getAnnomese();
            }
        }
        return "estrazione_"+idEstrazione;
    }
}
