/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.usr.web.estrazioni.service;

import it.usr.web.estrazioni.domain.Campione;
import it.usr.web.estrazioni.domain.Dettaglio;
import it.usr.web.estrazioni.domain.Estrazione;
import it.usr.web.estrazioni.domain.LavCampione;
import it.usr.web.estrazioni.domain.LavPratica;
import it.usr.web.estrazioni.domain.Pratica;
import it.usr.web.estrazioni.model.Riepilogo;
import it.usr.web.estrazioni.producer.EstrazioniLogger;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.sql.DataSource;
import org.slf4j.Logger;

/**
 *
 * @author riccardo.iovenitti
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class EstrazioniService {
    @PersistenceContext
    EntityManager em;
    @Resource(lookup = "jdbc/mude")
    DataSource mudeDs;
    @Resource(lookup = "jdbc/decreti")
    DataSource decretiDs;
    @Inject
    @EstrazioniLogger
    Logger log;
    
    public List<Estrazione> getEstrazioniStoricizzate() {
        return em.createNamedQuery("Estrazione.findAllStoricizzate", Estrazione.class).getResultList();
    }

    public Estrazione getEstrazioneById(int idEstrazione) {
        return em.find(Estrazione.class, idEstrazione);
    }
    
    public Estrazione getEstrazioneNonStoricizzata() {        
        List<Estrazione> le = em.createQuery("SELECT e FROM Estrazione e WHERE e.storicizzata = 0", Estrazione.class).getResultList();
        return (!le.isEmpty()) ? le.get(0) : null;
    }
    
    public List<Dettaglio> getDettaglioEstrazione(int idEstrazione) {  
        Estrazione estrazione = em.find(Estrazione.class, idEstrazione);
        return estrazione.getDettaglioList();
    }
    
    public Dettaglio getDettaglioEstrazione(int idEstrazione, int ordinanza, String tipo) {  
        List<Dettaglio> ld = em.createQuery("SELECT DISTINCT d FROM Dettaglio d JOIN d.estrazione e WHERE e.id = :idestrazione AND d.ordinanza = :ordinanza AND d.tipo = :tipo")
                .setParameter("idestrazione", idEstrazione)
                .setParameter("ordinanza", ordinanza)
                .setParameter("tipo", tipo)
                .getResultList();
        return (ld.size()>0) ? ld.get(0) : null;
    }
    
    public List<Pratica> getPratiche(int idDettaglio) {
        Dettaglio dettaglio = em.find(Dettaglio.class, idDettaglio);
        return dettaglio.getPraticaList();
    }
    
    public List<Campione> getCampione(int idDettaglio) {
        Dettaglio dettaglio = em.find(Dettaglio.class, idDettaglio);
        return dettaglio.getCampioneList();
    }
    
    public Estrazione getEstrazioneAnnoMese(String annoMese) {        
        TypedQuery<Estrazione> tq = em.createNamedQuery("Estrazione.findByAnnomese", Estrazione.class);        
        tq.setParameter("annomese", annoMese);
        List<Estrazione> lEstrazioni = tq.getResultList();
        return !lEstrazioni.isEmpty() ? lEstrazioni.get(0) : null;
    }
    
    public List<Integer> getProgettiPresentatiOrd100(int mese, int anno) {
        LocalDate start = LocalDate.of(anno, mese, 1);
        LocalDate end = start.plusMonths(1).minusDays(1);
        try(Connection con = mudeDs.getConnection()) {
            String sql = "SELECT V.IdPratica FROM USR2016Privata.dbo.View_PratichePerEstrazione AS V WHERE (V.IdPratica>0) AND (V.IstanzaMUDEData = (SELECT MIN(V2.IstanzaMudeData) FROM USR2016Privata.dbo.View_PratichePerEstrazione AS V2 WHERE V2.idPratica = V.idPratica)) AND (V.IstanzaMUDEData BETWEEN ? AND ?)";
            try(PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setDate(1, Date.valueOf(start));
                ps.setDate(2, Date.valueOf(end));
                try(ResultSet rs = ps.executeQuery()) {
                    List<Integer> pratiche = new ArrayList<>();
                    while(rs.next()) {
                        pratiche.add(rs.getInt(1));
                    }
                    return pratiche;
                }
            }
        }
        catch(SQLException sqle) {
            throw new RuntimeException(sqle);
        }        
    }
    
    public List<Integer> getProgettiDecretatiOrd100(int mese, int anno) {
        LocalDate start = LocalDate.of(anno, mese, 1);
        LocalDate end = start.plusMonths(1).minusDays(1);
        try(Connection con = decretiDs.getConnection()) {
            String sql = "SELECT id_pratica from v_ord100_decretati where data_ora_provvedimento BETWEEN ? AND ?";
            try(PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setDate(1, Date.valueOf(start));
                ps.setDate(2, Date.valueOf(end));
                try(ResultSet rs = ps.executeQuery()) {
                    List<Integer> pratiche = new ArrayList<>();
                    while(rs.next()) {
                        pratiche.add(rs.getInt(1));
                    }
                    return pratiche;
                }
            }
        }
        catch(SQLException sqle) {
            throw new RuntimeException(sqle);
        }   
    }
    
    public List<Integer> getProgettiChiusiOrd100(int mese, int anno) {
        LocalDate start = LocalDate.of(anno, mese, 1);
        LocalDate end = start.plusMonths(1).minusDays(1);
        log.debug("Estrazione ORD100-C compresa tra {} e {}", start, end);
        try(Connection con = decretiDs.getConnection()) {
            String sql = "SELECT id_pratica FROM v_estrazioni_lavori WHERE ord100 = 1 AND data_finelavori BETWEEN ? AND ?";
            try(PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setDate(1, Date.valueOf(start));
                ps.setDate(2, Date.valueOf(end));
                try(ResultSet rs = ps.executeQuery()) {
                    List<Integer> pratiche = new ArrayList<>();
                    while(rs.next()) {
                        pratiche.add(rs.getInt(1));
                    }
                    return pratiche;
                }
            }
        }
        catch(SQLException sqle) {
            throw new RuntimeException(sqle);
        } 
    }
    
    public List<Integer> getProgettiDecretatiOrd59(int mese, int anno) {
        LocalDate start = LocalDate.of(anno, mese, 1);
        LocalDate end = start.plusMonths(1).minusDays(1);
        try(Connection con = decretiDs.getConnection()) {
            String sql = "SELECT id_pratica from v_ord59_decretati where data_ora_provvedimento BETWEEN ? AND ?";
            try(PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setDate(1, Date.valueOf(start));
                ps.setDate(2, Date.valueOf(end));
                try(ResultSet rs = ps.executeQuery()) {
                    List<Integer> pratiche = new ArrayList<>();
                    while(rs.next()) {
                        pratiche.add(rs.getInt(1));
                    }
                    return pratiche;
                }
            }
        }
        catch(SQLException sqle) {
            throw new RuntimeException(sqle);
        }   
    }
    
    public List<Integer> getProgettiOltre50Ord59(int mese, int anno) {
        LocalDate start = LocalDate.of(anno, mese, 1);
        LocalDate end = start.plusMonths(1).minusDays(1);
        try(Connection con = decretiDs.getConnection()) {
            String sql = "SELECT id_pratica FROM v_estrazioni_lavori WHERE ord100 = 0 AND data_metalavori BETWEEN ? AND ?";
            try(PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setDate(1, Date.valueOf(start));
                ps.setDate(2, Date.valueOf(end));
                try(ResultSet rs = ps.executeQuery()) {
                    List<Integer> pratiche = new ArrayList<>();
                    while(rs.next()) {
                        pratiche.add(rs.getInt(1));
                    }
                    return pratiche;
                }
            }
        }
        catch(SQLException sqle) {
            throw new RuntimeException(sqle);
        } 
    }
    
    public List<Integer> getProgettiChiusiOrd59(int mese, int anno) {
        LocalDate start = LocalDate.of(anno, mese, 1);
        LocalDate end = start.plusMonths(1).minusDays(1);
        log.debug("Estrazione ORD59-C compresa tra {} e {}", start, end);
        try(Connection con = decretiDs.getConnection()) {
            String sql = "SELECT id_pratica FROM v_estrazioni_lavori WHERE ord100 = 0 AND data_finelavori BETWEEN ? AND ?";
            try(PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setDate(1, Date.valueOf(start));
                ps.setDate(2, Date.valueOf(end));
                try(ResultSet rs = ps.executeQuery()) {
                    List<Integer> pratiche = new ArrayList<>();
                    while(rs.next()) {
                        pratiche.add(rs.getInt(1));
                    }
                    return pratiche;
                }
            }
        }
        catch(SQLException sqle) {
            throw new RuntimeException(sqle);
        } 
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void ripulisciEstrazione() {
        em.createQuery("DELETE FROM LavCampione").executeUpdate();
        em.createQuery("DELETE FROM LavPratica").executeUpdate();
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void salvaLavoro(List<Integer> lCamp, List<Integer> lExt, String annoMese, int ordinanza, String tipo) {
        for(Object idPratica : lCamp) {
            String _idPratica = String.valueOf(idPratica);
            LavCampione lc = new LavCampione();
            lc.setAnnomese(annoMese);
            lc.setIdpratica(Integer.parseInt(_idPratica));
            lc.setOrdinanza(ordinanza);
            lc.setTipo(tipo);
            em.persist(lc);
        }
        for(Object idPratica : lExt) {
            String _idPratica = String.valueOf(idPratica);
            LavPratica lp = new LavPratica();
            lp.setAnnomese(annoMese);
            lp.setIdpratica(Integer.parseInt(_idPratica));
            lp.setOrdinanza(ordinanza);
            lp.setTipo(tipo);
            em.persist(lp);
        }
        em.flush();
    }
    
    public List<LavCampione> getLavorazioneCampione(int ordinanza, String tipo) {
        TypedQuery<LavCampione> tq = em.createNamedQuery("LavCampione.findByOrdinanzaTipo", LavCampione.class);
        tq.setParameter("ordinanza", ordinanza);
        tq.setParameter("tipo", tipo);
        return tq.getResultList();
    }
    
    public List<LavPratica> getLavorazionePratica(int ordinanza, String tipo) {
        TypedQuery<LavPratica> tq = em.createNamedQuery("LavPratica.findByOrdinanzaTipo", LavPratica.class);
        tq.setParameter("ordinanza", ordinanza);
        tq.setParameter("tipo", tipo);
        return tq.getResultList();
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void salvaEstrazione(Estrazione e) {
        em.persist(e);
        em.flush();
    } 
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void storicizza() {        
        Estrazione e = getEstrazioneNonStoricizzata();
        
        for(Dettaglio d : e.getDettaglioList()) {
            d.setCampioneList(new ArrayList<>());
            d.setPraticaList(new ArrayList<>());
            
            List<LavCampione> lvc = em.createNamedQuery("LavCampione.findByOrdinanzaTipo", LavCampione.class)
                    .setParameter("ordinanza", d.getOrdinanza())
                    .setParameter("tipo", d.getTipo())
                    .getResultList();
            for(LavCampione lc : lvc) {
                Campione c = new Campione();
                c.setIdpratica(lc.getIdpratica());
                c.setIddettaglio(d);
                d.getCampioneList().add(c);
            }
            
            List<LavPratica> lvp = em.createNamedQuery("LavPratica.findByOrdinanzaTipo", LavPratica.class)
                    .setParameter("ordinanza", d.getOrdinanza())
                    .setParameter("tipo", d.getTipo())
                    .getResultList();
            for(LavPratica lp : lvp) {
                Pratica p = new Pratica();
                p.setIdpratica(lp.getIdpratica());
                p.setIddettaglio(d);
                d.getPraticaList().add(p);
                
                // Storicizza insieme al campione le pratiche estratte 
                Campione c = new Campione();
                c.setIdpratica(lp.getIdpratica());
                c.setIddettaglio(d);
                d.getCampioneList().add(c);                
            }                        
            
            Collections.shuffle(d.getCampioneList());
        }                         
                      
        e.setStoricizzata(1);
        em.persist(e);
        ripulisciEstrazione();
    }
    
    public List<Riepilogo> getRiepilogo(int idEstrazione) {
        List<Riepilogo> riepilogo = Collections.checkedList(
            em.createNamedQuery("Mude.Riepilogo", Riepilogo.class).setParameter(1, idEstrazione).getResultList(), Riepilogo.class);
        return riepilogo;
    }
    
    public boolean estrattaMesePrecedente(int idPratica, String annoMeseAttuale) {
        int anno = Integer.parseInt(annoMeseAttuale.substring(0, 4));
        int mese = Integer.parseInt(annoMeseAttuale.substring(4));
        LocalDate d = LocalDate.of(anno, mese, 1);
        d = d.minusMonths(1);
        String annoMesePrec = d.format(DateTimeFormatter.ofPattern("YYYYMM"));
        long count = em.createQuery("SELECT COUNT(c) FROM Campione c JOIN Dettaglio d JOIN Estrazione e WHERE e.annomese = :annomese AND c.idpratica = :idpratica", Long.class)
                .setParameter("annomese", annoMesePrec)
                .setParameter("idpratica", idPratica)
                .getSingleResult();
        return count>0;
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void elimina() {
        Estrazione e = em.createQuery("SELECT e FROM Estrazione e WHERE e.storicizzata = 0", Estrazione.class).getSingleResult();
        
        Query q = em.createQuery("DELETE FROM LavCampione lc WHERE lc.annomese = :annomese").setParameter("annomese", e.getAnnomese());
        q.executeUpdate();
        
        q = em.createQuery("DELETE FROM LavPratica lp WHERE lp.annomese = :annomese").setParameter("annomese", e.getAnnomese());
        q.executeUpdate();
        
        for(Dettaglio d : e.getDettaglioList()) {
            em.remove(d);
        }
        em.remove(e);
    }
}
