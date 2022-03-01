/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.usr.web.estrazioni.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author riccardo.iovenitti
 */
@Entity
@Table(name = "estrazione")
@NamedQueries({
    @NamedQuery(name = "Estrazione.findAllStoricizzate", query = "SELECT e FROM Estrazione e WHERE e.storicizzata = 1"),
    @NamedQuery(name = "Estrazione.findById", query = "SELECT e FROM Estrazione e WHERE e.id = :id"),
    @NamedQuery(name = "Estrazione.findByAnnomese", query = "SELECT e FROM Estrazione e WHERE e.annomese = :annomese"),
    @NamedQuery(name = "Estrazione.findByDataestrazione", query = "SELECT e FROM Estrazione e WHERE e.dataestrazione = :dataestrazione"),
    @NamedQuery(name = "Estrazione.findByUtente", query = "SELECT e FROM Estrazione e WHERE e.utente = :utente")})
public class Estrazione implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 6)
    private String annomese;
    @Basic(optional = false)
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataestrazione;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 64)
    private String utente;
    @Basic(optional = false)
    @NotNull
    private int storicizzata;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "estrazione")
    private List<Dettaglio> dettaglioList;

    public Estrazione() {
    }

    public Estrazione(Integer id) {
        this.id = id;
    }

    public Estrazione(Integer id, String annomese, Date dataestrazione, String utente, int storicizzata) {
        this.id = id;
        this.annomese = annomese;
        this.dataestrazione = dataestrazione;
        this.utente = utente;
        this.storicizzata = storicizzata;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAnnomese() {
        return annomese;
    }

    public void setAnnomese(String annomese) {
        this.annomese = annomese;
    }

    public Date getDataestrazione() {
        return dataestrazione;
    }

    public void setDataestrazione(Date dataestrazione) {
        this.dataestrazione = dataestrazione;
    }

    public String getUtente() {
        return utente;
    }

    public void setUtente(String utente) {
        this.utente = utente;
    }

    public List<Dettaglio> getDettaglioList() {
        return dettaglioList;
    }

    public void setDettaglioList(List<Dettaglio> dettaglioList) {
        this.dettaglioList = dettaglioList;
    }

    public int getStoricizzata() {
        return storicizzata;
    }

    public void setStoricizzata(int storicizzata) {
        this.storicizzata = storicizzata;
    }
        
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Estrazione)) {
            return false;
        }
        Estrazione other = (Estrazione) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "it.usr.web.estrazioni.domain.Estrazione[ id=" + id + " ]";
    }
    
}
