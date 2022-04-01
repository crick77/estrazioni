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
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import javax.annotation.PostConstruct;
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
import org.slf4j.Logger;

/**
 *
 * @author riccardo.iovenitti
 */
@Named
@ViewScoped
public class RiepilogoDatiController extends BaseController {
    @Inject
    @EstrazioniLogger
    Logger log;
    private String responsabile;
    private String segretario; 
    Estrazione e;

    @PostConstruct
    public void initialize() {
        e = (Estrazione)FacesContext.getCurrentInstance().getExternalContext().getFlash().get("estrazione");
        responsabile = null;
        segretario = null;
    }
    
    public String getResponsabile() {
        return responsabile;
    }

    public void setResponsabile(String responsabile) {
        this.responsabile = responsabile;
    }

    public String getSegretario() {
        return segretario;
    }

    public void setSegretario(String segretario) {
        this.segretario = segretario;
    }
    
    public void conferma() {                        
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-YYYY");
               
        try (PDDocument pdfDocument = PDDocument.load(ec.getResourceAsStream("/WEB-INF/Estrazioni.pdf"))) {
            PDDocumentCatalog docCatalog = pdfDocument.getDocumentCatalog();
            PDAcroForm acroForm = docCatalog.getAcroForm();
            if (acroForm != null) {
                PDField field = (PDField)acroForm.getField("DATA_ESTRAZIONE");
                field.setValue(sdf.format(e.getDataestrazione()));

                //field = (PDField)acroForm.getField("PERIODO");
                //int[] am = annoMeseSplit(e.getAnnomese());
                //String periodo = ((am[0]<10) ? "0"+am[0] : am[0])+"/"+am[1];
                //field.setValue(periodo);
                
                for(Dettaglio d : e.getDettaglioList()) {
                    String fieldName = "ORD"+d.getOrdinanza()+d.getTipo();
                    
                    field = (PDField)acroForm.getField(fieldName+"C");
                    StringJoiner joiner = new StringJoiner(", ");
                    List<Campione> lc = d.getCampioneList();
                    Collections.shuffle(lc);
                    for(Campione c : lc) {
                        joiner.add(String.valueOf(c.getIdpratica()));
                    }
                    field.setValue(joiner.toString());
                    
                    field = (PDField)acroForm.getField(fieldName+"S");
                    joiner = new StringJoiner(", ");
                    List<Pratica> lp = d.getPraticaList();
                    Collections.shuffle(lp);
                    for(Pratica p : lp) {
                        joiner.add(String.valueOf(p.getIdpratica()));
                    }
                    field.setValue(joiner.toString());
                }
                      
                field = (PDField)acroForm.getField("RESPONSABILE");
                field.setValue(responsabile);
                
                field = (PDField)acroForm.getField("SEGRETARIO");
                field.setValue(segretario);
                
                acroForm.flatten();
            }            
            
            ec.responseReset();
            ec.setResponseContentType("application/pdf"); 
            ec.setResponseHeader("Content-Disposition", "attachment; filename=\"Estrazioni_" + e.getAnnomese() + ".pdf\""); 

            OutputStream output = ec.getResponseOutputStream();
            pdfDocument.save(output);
            //output.flush();        
            fc.responseComplete(); 
            log.debug("File Estrazioni_" + e.getAnnomese()+".pdf generato.");
                                    
            PrimeFaces.current().dialog().closeDynamic("OK");
        }
        catch(Exception ioe) {                        
            log.debug("Impossibile generare il rapporto: {}", ioe);
            PrimeFaces.current().dialog().closeDynamic("KO");
        }
    }
    
    public void annulla() {
        PrimeFaces.current().dialog().closeDynamic(null);
    }
}
