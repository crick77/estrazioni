/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.usr.web.estrazioni.domain;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 *
 * @author riccardo.iovenitti
 */
@Entity
@Table(name = "pratica")
@NamedQueries({
    @NamedQuery(name = "Pratica.findAll", query = "SELECT p FROM Pratica p"),
    @NamedQuery(name = "Pratica.findById", query = "SELECT p FROM Pratica p WHERE p.id = :id"),
    @NamedQuery(name = "Pratica.findByIdpratica", query = "SELECT p FROM Pratica p WHERE p.idpratica = :idpratica")})
public class Pratica implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer id;
    @Basic(optional = false)
    @NotNull
    private int idpratica;
    @JoinColumn(name = "iddettaglio", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Dettaglio iddettaglio;

    public Pratica() {
    }

    public Pratica(Integer id) {
        this.id = id;
    }

    public Pratica(Integer id, int idpratica) {
        this.id = id;
        this.idpratica = idpratica;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getIdpratica() {
        return idpratica;
    }

    public void setIdpratica(int idpratica) {
        this.idpratica = idpratica;
    }

    public Dettaglio getIddettaglio() {
        return iddettaglio;
    }

    public void setIddettaglio(Dettaglio iddettaglio) {
        this.iddettaglio = iddettaglio;
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
        if (!(object instanceof Pratica)) {
            return false;
        }
        Pratica other = (Pratica) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "it.usr.web.estrazioni.domain.Pratica[ id=" + id + " ]";
    }
    
}
