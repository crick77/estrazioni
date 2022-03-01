/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.usr.web.estrazioni.service;

import it.usr.web.estrazioni.domain.Utente;
import it.usr.web.estrazioni.producer.EstrazioniLogger;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.naming.NamingException;
import javax.naming.ldap.LdapContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.slf4j.Logger;

/**
 *
 * @author riccardo.iovenitti
 */
@Stateless
@Named
public class AuthService {
    public final static String SERVER_NAME = "aqadmc04"; //aqadmc05
    public final static String DOMAIN_NAME = "abruzzo.loc";
    @PersistenceContext
    EntityManager em;
    @EstrazioniLogger
    @Inject
    Logger logger;
    
    public Utente authenticate(String username, String password) {
        try {
            LdapContext ctx = ActiveDirectory.getConnection(username, password, DOMAIN_NAME, SERVER_NAME);
            ctx.close();
            
            Utente u = em.find(Utente.class, username);            
            return (u!=null && u.getAbilitato()!=0) ? u : null;            
        }
        catch(NamingException ne) {
            logger.debug("Autenticazione non riuscita a causa di: ", ne);
            return null;
        }
    }
}
