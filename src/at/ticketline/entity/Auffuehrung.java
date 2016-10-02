package at.ticketline.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.swing.text.DateFormatter;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.log4j.lf5.util.DateFormatManager;

import at.ticketline.util.Comparable;

/**
 * Unter einer Auffuehrung versteht man eine Darbietung einer Veranstaltung an
 * einem bestimmten Datum, zu einer bestimmten Uhrzeit und in einem bestimmten
 * Saal. Eine Auffuehrung ist natuerlich genau einer Veranstaltung zugeordnet.
 * Von einer Veranstaltung koennen beliebig viele Auffuehrungen gemacht werden.
 * Zusaetzlich kann fuer jede Auffuehrung ein Hinweis hinterlegt werden, ob die
 * Auffuehrung storniert wurde, und eine Preiskategorie (entweder Minimalpreis,
 * Standardpreis oder Maximalpreis) gespeichert werden.
 * 
 */
@Entity
public class Auffuehrung extends BaseEntity implements Serializable, Comparable {

    private static final long serialVersionUID = 7953200412305670075L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    @NotNull
    private Date datumuhrzeit;

    private boolean storniert = false;

    @Column(nullable = false)
    @Enumerated(EnumType.ORDINAL)
    @NotNull
    private PreisKategorie preis;

    @Size(max = 255)
    private String hinweis;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SAAL_ID")
    private Saal saal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "VERANSTALTUNG_ID")
    private Veranstaltung veranstaltung;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "auffuehrung")
    private Set<Platz> plaetze;

    public Integer getId() {
	return this.id;
    }

    public void setId(Integer id) {
	this.id = id;
    }

    public Date getDatumuhrzeit() {
	return this.datumuhrzeit;
    }

    public void setDatumuhrzeit(Date datumuhrzeit) {
	this.datumuhrzeit = datumuhrzeit;
    }

    /**
     * true (storniert), false (nicht storniert)
     */
    public boolean isStorniert() {
	return this.storniert;
    }

    public void setStorniert(boolean storniert) {
	this.storniert = storniert;
    }

    /**
     * Preiskategorie
     * 
     */
    public PreisKategorie getPreis() {
	return this.preis;
    }

    public void setPreis(PreisKategorie preis) {
	this.preis = preis;
    }

    /**
     * Hinweis zur Auffuehrung Darbietung einer Veranstaltung an einem
     * bestimmten Ort zu einer bestimmten Zeit.
     * 
     */
    public String getHinweis() {
	return this.hinweis;
    }

    public void setHinweis(String hinweis) {
	this.hinweis = hinweis;
    }

    public Saal getSaal() {
	return this.saal;
    }

    public void setSaal(Saal saal) {
	this.saal = saal;
    }

    public Veranstaltung getVeranstaltung() {
	return this.veranstaltung;
    }

    public void setVeranstaltung(Veranstaltung veranstaltung) {
	this.veranstaltung = veranstaltung;
    }

    public Set<Platz> getPlaetze() {
	return this.plaetze;
    }

    public void setPlaetze(Set<Platz> plaetze) {
	this.plaetze = plaetze;
    }

    @Override
    public String toString() {
	StringBuilder builder = new StringBuilder();
	builder.append("Auffuehrung [");
	if (this.id != null) {
	    builder.append("id=").append(this.id).append(", ");
	}
	if (this.datumuhrzeit != null) {
	    builder.append("datumuhrzeit=").append(this.datumuhrzeit)
		    .append(", ");
	}
	if (this.hinweis != null) {
	    builder.append("hinweis=").append(this.hinweis).append(", ");
	}
	if (this.preis != null) {
	    builder.append("preis=").append(this.preis).append(", ");
	}
	builder.append("storniert=").append(this.storniert).append("]");
	return builder.toString();
    }

    public String format() {
	DateFormatManager dfm = new DateFormatManager("dd.MM.yyyy, HH:mm");
	// DateFormatManager dfm = new
	// DateFormatManager("dd.MM.yyyy, HH:mm 'Uhr'");
	DateFormatter df = new DateFormatter(dfm.getDateFormatInstance());

	String s = "";
	
	try {
	    s += df.valueToString(this.datumuhrzeit) + " - ";
	    s += this.getVeranstaltung().getBezeichnung() + " - ";
	    s += this.preis;
	} catch (Exception e) {
	    s = "";
//	    e.printStackTrace();
	}

	return s;
    }

    @Override
    public Boolean equalsComparable(Comparable obj) {
	if (!(obj.getClass() == this.getClass()))
	    return false;
	if (this.getId() == null || this.getClass().cast(obj).getId() == null) {
	    if (this.getDatumuhrzeit() != this.getClass().cast(obj)
		    .getDatumuhrzeit())
		return false;
	    if (this.getHinweis() != this.getClass().cast(obj).getHinweis())
		return false;
	    if (this.getPlaetze() != this.getClass().cast(obj).getPlaetze())
		return false;
	    if (this.getPreis() != this.getClass().cast(obj).getPreis())
		return false;
	    if (this.getSaal() != this.getClass().cast(obj).getSaal())
		return false;
	    if (this.getVeranstaltung() != this.getClass().cast(obj)
		    .getVeranstaltung())
		return false;
	}
	if (this.getId() == this.getClass().cast(obj).getId())
	    return true;
	return false;
    }

}
