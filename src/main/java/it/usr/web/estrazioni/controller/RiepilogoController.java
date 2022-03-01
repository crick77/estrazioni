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
import java.util.List;
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
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-YYYY");
        Estrazione e = es.getEstrazioneById(idEstrazione);        
        try (PDDocument pdfDocument = PDDocument.load(ec.getResourceAsStream("/WEB-INF/Estrazioni.pdf"))) {
            PDDocumentCatalog docCatalog = pdfDocument.getDocumentCatalog();
            PDAcroForm acroForm = docCatalog.getAcroForm();
            if (acroForm != null) {
                PDField field = (PDField)acroForm.getField("DATA_ESTRAZIONE");
                field.setValue(sdf.format(e.getDataestrazione()));

                field = (PDField)acroForm.getField("PERIODO");
                int[] am = annoMeseSplit(e.getAnnomese());
                String periodo = ((am[0]<10) ? "0"+am[0] : am[0])+"/"+am[1];
                field.setValue(periodo);
                
                for(Dettaglio d : e.getDettaglioList()) {
                    String fieldName = "ORD"+d.getOrdinanza()+d.getTipo();
                    
                    field = (PDField)acroForm.getField(fieldName+"C");
                    StringJoiner joiner = new StringJoiner(", ");
                    for(Campione c : d.getCampioneList()) {
                        joiner.add(String.valueOf(c.getIdpratica()));
                    }
                    field.setValue(joiner.toString());
                    
                    field = (PDField)acroForm.getField(fieldName+"S");
                    joiner = new StringJoiner(", ");
                    for(Pratica p : d.getPraticaList()) {
                        joiner.add(String.valueOf(p.getIdpratica()));
                    }
                    field.setValue(joiner.toString());
                }
                                
                acroForm.flatten();
            }            

            ec.responseReset();
            ec.setResponseContentType("application/pdf"); 
            ec.setResponseHeader("Content-Disposition", "attachment; filename=\"Estrazioni_" + e.getAnnomese() + ".pdf\""); 

            OutputStream output = ec.getResponseOutputStream();
            pdfDocument.save(output);
            output.flush();        
            fc.responseComplete(); 
            log.debug("File Estrazioni_" + e.getAnnomese()+".pdf generato.");
            
            FacesMessage msg = new FacesMessage();
            msg.setSeverity(FacesMessage.SEVERITY_INFO);
            msg.setSummary("Esito");
            msg.setDetail("Rapporto generato!");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
        catch(Exception ioe) {            
            FacesMessage msg = new FacesMessage();
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            msg.setSummary("Esito");
            msg.setDetail("Impossibile generare il reapporto!");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            log.debug("Impossibile generare il rapporto: {}", ioe);
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
