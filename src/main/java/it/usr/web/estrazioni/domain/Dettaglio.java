/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.usr.web.estrazioni.domain;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author riccardo.iovenitti
 */
@Entity
@Table(name = "dettaglio")
@NamedQueries({
    @NamedQuery(name = "Dettaglio.findAll", query = "SELECT d FROM Dettaglio d"),
    @NamedQuery(name = "Dettaglio.findById", query = "SELECT d FROM Dettaglio d WHERE d.id = :id"),
    @NamedQuery(name = "Dettaglio.findByOrdinanza", query = "SELECT d FROM Dettaglio d WHERE d.ordinanza = :ordinanza"),
    @NamedQuery(name = "Dettaglio.findByTipo", query = "SELECT d FROM Dettaglio d WHERE d.tipo = :tipo"),
    @NamedQuery(name = "Dettaglio.findByPercentuale", query = "SELECT d FROM Dettaglio d WHERE d.percentuale = :percentuale")})
public class Dettaglio implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer id;
    @Basic(optional = false)
    @NotNull
    private int ordinanza;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    private String tipo;
    @Basic(optional = false)
    @NotNull
    private int percentuale;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "iddettaglio")
    private List<Campione> campioneList;
    @JoinColumns({
        @JoinColumn(name = "idestrazione", referencedColumnName = "id"),
        @JoinColumn(name = "idestrazione", referencedColumnName = "id")})
    @ManyToOne(optional = false)
    private Estrazione estrazione;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "iddettaglio")
    private List<Pratica> praticaList;

    public Dettaglio() {
    }

    public Dettaglio(Integer id) {
        this.id = id;
    }

    public Dettaglio(Integer id, int ordinanza, String tipo, int percentuale) {
        this.id = id;
        this.ordinanza = ordinanza;
        this.tipo = tipo;
        this.percentuale = percentuale;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public int getPercentuale() {
        return percentuale;
    }

    public void setPercentuale(int percentuale) {
        this.percentuale = percentuale;
    }

    public List<Campione> getCampioneList() {
        return campioneList;
    }

    public void setCampioneList(List<Campione> campioneList) {
        this.campioneList = campioneList;
    }

    public Estrazione getEstrazione() {
        return estrazione;
    }

    public void setEstrazione(Estrazione estrazione) {
        this.estrazione = estrazione;
    }

    public List<Pratica> getPraticaList() {
        return praticaList;
    }

    public void setPraticaList(List<Pratica> praticaList) {
        this.praticaList = praticaList;
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
        if (!(object instanceof Dettaglio)) {
            return false;
        }
        Dettaglio other = (Dettaglio) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "it.usr.web.estrazioni.domain.Dettaglio[ id=" + id + " ]";
    }
    
}
