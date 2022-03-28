/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.usr.web.estrazioni.domain;

import it.usr.web.estrazioni.model.Riepilogo;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SqlResultSetMapping;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author riccardo.iovenitti
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "Mude.findAll", query = "SELECT m FROM Mude m"),
    @NamedQuery(name = "Mude.findByIdpratica", query = "SELECT m FROM Mude m WHERE m.idpratica = :idpratica"),
    @NamedQuery(name = "Mude.findByIntestatario", query = "SELECT m FROM Mude m WHERE m.intestatario = :intestatario"),
    @NamedQuery(name = "Mude.findByComune", query = "SELECT m FROM Mude m WHERE m.comune = :comune"),
    @NamedQuery(name = "Mude.findByIndirizzo", query = "SELECT m FROM Mude m WHERE m.indirizzo = :indirizzo"),
    @NamedQuery(name = "Mude.findByTecnico", query = "SELECT m FROM Mude m WHERE m.tecnico = :tecnico"),
    @NamedQuery(name = "Mude.findByPectecnico", query = "SELECT m FROM Mude m WHERE m.pectecnico = :pectecnico")})
@SqlResultSetMapping(
    name="Mude.RiepilogoResult",
    classes={
        @ConstructorResult(
            targetClass=Riepilogo.class,
            columns={
                @ColumnResult(name="idpratica", type=Integer.class),
                @ColumnResult(name="intestatario", type=String.class),
                @ColumnResult(name="comune", type=String.class),
                @ColumnResult(name="indirizzo", type=String.class),
                @ColumnResult(name="tecnico", type=String.class),
                @ColumnResult(name="pectecnico", type=String.class),
                @ColumnResult(name="ordinanza", type=Integer.class),
                @ColumnResult(name="tipo", type=String.class),
            }
        ) 
    }
)
@NamedNativeQuery(
        name = "Mude.Riepilogo", 
        query = "SELECT p.idpratica, m.intestatario, m.comune, m.indirizzo, m.tecnico, m.pectecnico, d.ordinanza, d.tipo from pratica p inner join dettaglio d on p.iddettaglio = d.id INNER JOIN estrazione e ON d.idestrazione = e.id LEFT JOIN mude m on p.idpratica = m.idpratica WHERE e.id = ? order by ordinanza desc, tipo", 
        resultSetMapping = "Mude.RiepilogoResult"
)
public class Mude implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    private Integer idpratica;
    @Size(max = 255)
    private String intestatario;
    @Size(max = 255)
    private String comune;
    @Size(max = 255)
    private String indirizzo;
    @Size(max = 255)
    private String tecnico;
    @Size(max = 255)
    private String pectecnico;

    public Mude() {
    }

    public Mude(Integer idpratica) {
        this.idpratica = idpratica;
    }

    public Mude(Integer idpratica, String intestatario, String comune, String indirizzo, String tecnico, String pectecnico) {
        this.idpratica = idpratica;
        this.intestatario = intestatario;
        this.comune = comune;
        this.indirizzo = indirizzo;
        this.tecnico = tecnico;
        this.pectecnico = pectecnico;
    }
        
    public Integer getIdpratica() {
        return idpratica;
    }

    public void setIdpratica(Integer idpratica) {
        this.idpratica = idpratica;
    }

    public String getIntestatario() {
        return intestatario;
    }

    public void setIntestatario(String intestatario) {
        this.intestatario = intestatario;
    }

    public String getComune() {
        return comune;
    }

    public void setComune(String comune) {
        this.comune = comune;
    }

    public String getIndirizzo() {
        return indirizzo;
    }

    public void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
    }

    public String getTecnico() {
        return tecnico;
    }

    public void setTecnico(String tecnico) {
        this.tecnico = tecnico;
    }

    public String getPectecnico() {
        return pectecnico;
    }

    public void setPectecnico(String pectecnico) {
        this.pectecnico = pectecnico;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idpratica != null ? idpratica.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Mude)) {
            return false;
        }
        Mude other = (Mude) object;
        if ((this.idpratica == null && other.idpratica != null) || (this.idpratica != null && !this.idpratica.equals(other.idpratica))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "it.usr.web.estrazioni.domain.Mude[ idpratica=" + idpratica + " ]";
    }
    
}
