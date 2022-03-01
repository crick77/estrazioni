/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.usr.web.estrazioni.controller;

import it.usr.web.estrazioni.domain.Utente;
import it.usr.web.estrazioni.producer.EstrazioniLogger;
import it.usr.web.estrazioni.service.AuthService;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.slf4j.Logger;

/**
 *
 * @author riccardo.iovenitti
 */
@Named
@RequestScoped
public class AuthController extends BaseController {
    @Inject
    private AuthService as;
    @Inject
    @EstrazioniLogger
    Logger logger;
    private String username;
    private String password;
    private String message;
    
    public String doLogin() {
        Utente u = as.authenticate(username, password);
        if(u!=null) {
            putIntoSession(u);            
            logger.debug("L'utente [{}] ha effettuato l'accesso.", username);
            return redirect("/secure/estrazioni");
        }
        else {
            logger.debug("L'utente [{}] non esiste o la password è errata o l'utente non abilitato.", username);
            message = "Credenziali di accesso errate o utente non abilitato.";
            return SAME_VIEW;
        }
    }
    
    public String doLogout() {
        invalidateSession();
        logger.debug("L'utente [{}] ha effettuato la disconnessione.", username);
        return redirect("/index");
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }        

    public String getMessage() {
        return message;
    }        
}
