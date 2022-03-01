/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.usr.web.estrazioni.domain;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
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
@Table(name = "lav_campione")
@NamedQueries({
    @NamedQuery(name = "LavCampione.findAll", query = "SELECT l FROM LavCampione l"),
    @NamedQuery(name = "LavCampione.findById", query = "SELECT l FROM LavCampione l WHERE l.id = :id"),
    @NamedQuery(name = "LavCampione.findByAnnomese", query = "SELECT l FROM LavCampione l WHERE l.annomese = :annomese"),
    @NamedQuery(name = "LavCampione.findByOrdinanza", query = "SELECT l FROM LavCampione l WHERE l.ordinanza = :ordinanza"),
    @NamedQuery(name = "LavCampione.findByTipo", query = "SELECT l FROM LavCampione l WHERE l.tipo = :tipo"),
    @NamedQuery(name = "LavCampione.findByIdpratica", query = "SELECT l FROM LavCampione l WHERE l.idpratica = :idpratica"),
    @NamedQuery(name = "LavCampione.findByOrdinanzaTipo", query = "SELECT l FROM LavCampione l WHERE l.ordinanza = :ordinanza AND l.tipo = :tipo")})
public class LavCampione implements Serializable {

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
    private int ordinanza;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    private String tipo;
    @Basic(optional = false)
    @NotNull
    private int idpratica;

    public LavCampione() {
    }

    public LavCampione(Integer id) {
        this.id = id;
    }

    public LavCampione(Integer id, String annomese, int ordinanza, String tipo, int idpratica) {
        this.id = id;
        this.annomese = annomese;
        this.ordinanza = ordinanza;
        this.tipo = tipo;
        this.idpratica = idpratica;       
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

    public int getOrdinanza() {
        return ordinanza;
    }

    public void setOrdinanza(int ordinanza) {
        this.ordinanza = ordinanza;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getIdpratica() {
        return idpratica;
    }

    public void setIdpratica(int idpratica) {
        this.idpratica = idpratica;
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
        if (!(object instanceof LavCampione)) {
            return false;
        }
        LavCampione other = (LavCampione) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "it.usr.web.estrazioni.domain.LavCampione[ id=" + id + " ]";
    }
    
}
