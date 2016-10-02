package at.ticketline.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * 
 * Im Kunden werden alle Daten eines Kunden abgelegt. Jeder Kunde ist einem Ort
 * zugeordnet. Ein Kunde kann optional eine Ticketcard besitzen. Er besitzt
 * darueber hinaus ueber einen Web Account, wenn der Username und das
 * Passwort gesetzt sind.
 */
@Entity
@DiscriminatorValue(value = "K")
public class Kunde extends Person implements Serializable {

	private static final long serialVersionUID = 4438968516066993490L;

	/**
	 * Nur wenn der Kunde die Ermaechtigung erteilt hat,
	 * darf die Kreditkarte belastet werden
	 */
	private boolean ermaechtigung = false;

	@Column(length = 16)
	@Pattern(regexp = "^[0-9]{12,16}$")
	private String kreditkartennr;

	@Enumerated(EnumType.ORDINAL)
	private Kreditkartentyp kreditkartentyp;

	@Temporal(TemporalType.DATE)
	private GregorianCalendar kreditkarteGueltigBis;

	@Min(value = 0)
	@Digits(integer = 8, fraction = 2)
	private BigDecimal kontostand;

	@Min(value = 0)
	@Digits(integer = 8, fraction = 2)
	private BigDecimal kontolimit;

	@Min(value = 0)
	@Digits(integer = 8, fraction = 2)
	private BigDecimal ermaessigung;

	@Size(max = 20)
	@Column(length = 20)
	private String ticketcardnr;

	@Temporal(TemporalType.DATE)
	private GregorianCalendar ticketcardGueltigBis;

	private boolean gesperrt = false;

	@Enumerated(EnumType.ORDINAL)
	@NotNull
	private Kundengruppe gruppe = null;

	@Size(max = 255)
	private String vorlieben;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "kunde")
	private Set<Bestellung> bestellungen = new HashSet<Bestellung>();

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "kunde")
	private Set<Transaktion> ktransaktionen = new HashSet<Transaktion>();

	public boolean isErmaechtigung() {
		return this.ermaechtigung;
	}

	public void setErmaechtigung(boolean ermaechtigung) {
		this.ermaechtigung = ermaechtigung;
	}

	public String getKreditkartennr() {
		return this.kreditkartennr;
	}

	public void setKreditkartennr(String kreditkartennr) {
		this.kreditkartennr = kreditkartennr;
	}

	public Kreditkartentyp getKreditkartentyp() {
		return this.kreditkartentyp;
	}

	public void setKreditkartentyp(Kreditkartentyp kreditkartentyp) {
		this.kreditkartentyp = kreditkartentyp;
	}

	public GregorianCalendar getKreditkarteGueltigBis() {
		return this.kreditkarteGueltigBis;
	}

	public void setKreditkarteGueltigBis(GregorianCalendar kkgueltigbis) {
		this.kreditkarteGueltigBis = kkgueltigbis;
	}

	public BigDecimal getKontostand() {
		return this.kontostand;
	}

	public void setKontostand(BigDecimal kontostand) {
		this.kontostand = kontostand;
	}

	public BigDecimal getKontolimit() {
		return this.kontolimit;
	}

	public void setKontolimit(BigDecimal kontolimit) {
		this.kontolimit = kontolimit;
	}

	public BigDecimal getErmaessigung() {
		return this.ermaessigung;
	}

	public void setErmaessigung(BigDecimal ermaessigung) {
		this.ermaessigung = ermaessigung;
	}

	public String getTicketcardnr() {
		return this.ticketcardnr;
	}

	public void setTicketcardnr(String ticketcardnr) {
		this.ticketcardnr = ticketcardnr;
	}

	public GregorianCalendar getTicketcardGueltigBis() {
		return this.ticketcardGueltigBis;
	}

	public void setTicketcardGueltigBis(GregorianCalendar tkgueltigbis) {
		this.ticketcardGueltigBis = tkgueltigbis;
	}

	public boolean isGesperrt() {
		return this.gesperrt;
	}

	public void setGesperrt(boolean gesperrt) {
		this.gesperrt = gesperrt;
	}

	public Kundengruppe getGruppe() {
		return this.gruppe;
	}

	public void setGruppe(Kundengruppe gruppe) {
		this.gruppe = gruppe;
	}

	public String getVorlieben() {
		return this.vorlieben;
	}

	public void setVorlieben(String vorlieben) {
		this.vorlieben = vorlieben;
	}

	public Set<Bestellung> getBestellungen() {
		return this.bestellungen;
	}

	public void setBestellungen(Set<Bestellung> bestellungen) {
		this.bestellungen = bestellungen;
	}

	public Set<Transaktion> getTransaktionen() {
		return this.ktransaktionen;
	}

	public void setTransaktionen(Set<Transaktion> transaktionen) {
		this.ktransaktionen = transaktionen;
	}

	/**
	 * Hier wurde Nachname, Vorname und gesperrt Attributen hinzugefügt. 
	 * Für KundeSearch.
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Kunde [");
		if (this.ermaechtigung) {
			builder.append("ermaechtigung=").append(this.ermaechtigung)
			.append(", ");		    
		}
		if (this.ermaessigung != null) {
			builder.append("ermaessigung=").append(this.ermaessigung).append(
					", ");
		}
		if (this.gesperrt) {
			builder.append("gesperrt=").append(this.gesperrt)
			.append(", ");		    
		}
		if (this.gruppe != null) {
			builder.append("gruppe=").append(this.gruppe).append(", ");
		}
		if (this.geschlecht != null) {
			builder.append("geschlecht=").append(this.geschlecht).append(", ");
		}
		if (this.nachname != null) {
			builder.append("nachname=").append(this.nachname).append(", ");
		}
		if (this.vorname != null) {
			builder.append("vorname=").append(this.vorname);
		}
		if (this.kreditkarteGueltigBis != null) {
			builder.append("kreditkarteGueltigBis=").append(
					this.kreditkarteGueltigBis).append(", ");
		}
		if (this.kontolimit != null) {
			builder.append("kontolimit=").append(this.kontolimit).append(", ");
		}
		if (this.kontostand != null) {
			builder.append("kontostand=").append(this.kontostand).append(", ");
		}
		if (this.kreditkartennr != null) {
			builder.append("kreditkartennr=").append(this.kreditkartennr)
					.append(", ");
		}
		if (this.kreditkartentyp != null) {
			builder.append("kreditkartentyp=").append(this.kreditkartentyp)
					.append(", ");
		}
		if (this.ticketcardnr != null) {
			builder.append("ticketcardnr=").append(this.ticketcardnr).append(
					", ");
		}
		if (this.ticketcardGueltigBis != null) {
			builder.append("ticketcardGueltigBis=").append(
					this.ticketcardGueltigBis).append(", ");
		}
		if (this.vorlieben != null) {
			builder.append("vorlieben=").append(this.vorlieben);
		}
		builder.append("]");
		return builder.toString();
	}

}
