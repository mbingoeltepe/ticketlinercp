package at.ticketline.kassa.ui.sitzplan;

import java.util.LinkedHashSet;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import at.ticketline.dao.DaoFactory;
import at.ticketline.dao.interfaces.SaalDao;
import at.ticketline.entity.Auffuehrung;
import at.ticketline.entity.Platz;
import at.ticketline.entity.PlatzStatus;
import at.ticketline.entity.PreisKategorie;
import at.ticketline.entity.Reihe;
import at.ticketline.entity.Saal;
import at.ticketline.entity.Transaktion;
import at.ticketline.kassa.Activator;
import at.ticketline.log.LogFactory;
import at.ticketline.log.Logger;

public class SitzplanImpl {

    Logger log = LogFactory.getLogger(SitzplanImpl.class);

    private int columns = 0;
    private int rows = 0;
    private Saal saal;
    private Auffuehrung auffuehrung;
    private Label lbls[][] = new Label[columns][rows];
    // private Object[] reihenArray;
    private int platzNummer = 1;

    // flag wenn noch keine Reihen für einen Saal vorhanden sind
    private boolean empty = false;

    private Reihe reihe = new Reihe(); // spezifische Reihe
    private Color gebucht = new Color(null, 255, 0, 0);
    private Color reserviert = new Color(null, 0, 255, 0);
    private Color frei = new Color(null, 230, 255, 220);
    private Color markiert = new Color(null, 255, 255, 0);

    private Set<Platz> selectedPlaces = new LinkedHashSet<Platz>();
    private Set<Reihe> reihen = new LinkedHashSet<Reihe>();

    private SaalDao saalDao = (SaalDao) DaoFactory.findDaoByEntity(Saal.class);

    /**
     * Konstruktor
     * 
     * @param auffuehrung
     *            Gibt an, zu welcher Auffuehrung die Matrix erstellt werden
     *            soll
     */
    public SitzplanImpl(Auffuehrung auffuehrung) {

	this.auffuehrung = auffuehrung;
	this.saal = auffuehrung.getSaal();

	saalDao.refresh(this.saal);

	reihen = saal.getReihen();
	if (reihen.size() > 0) {
	    rows = reihen.size();
	} else {
	    this.empty = true;
	    rows = 1;
	    // rows = MAX_ROWS;
	}

	// this.reihenArray = reihen.toArray();
	if (reihen.size() > 0) {
	    try {
		for (Reihe r : reihen) {
		    if (r.getAnzplaetze() > columns)
			columns = r.getAnzplaetze();
		}
	    } catch (NullPointerException ex) {
		// columns = MAX_COLUMNS;
		columns = 1;
	    }
	} else {
	    // columns = MAX_COLUMNS;
	    columns = 1;
	}
	lbls = new Label[rows][columns];
    }

    /**
     * aktualisiert aktuellen Sitzplan
     */
    public void refreshSitzplan() {

	if (this.empty == true)
	    return;

	saalDao.refresh(this.saal);
	reihen = saal.getReihen();

	for (int i = 0; i < lbls.length; i++) {
	    platzNummer = 1;
	    for (int j = 0; j < lbls[i].length; j++) {

		for (Reihe r : reihen) {
		    reihe = r;
		    int reihennr = Integer.valueOf(reihe.getBezeichnung().subSequence(0, 1).toString());
		    if ((reihennr - 1) == i) {
			try {
			    if (reihe.getAnzplaetze() != 0) {
				for (Platz pl : reihe.getPlaetze()) {
				    if (pl.getNummer() == platzNummer
					    && pl.getAuffuehrung() == auffuehrung) {
					if (pl.getStatus() == PlatzStatus.GEBUCHT) {
					    lbls[i][j].setBackground(gebucht);
					    /*
					     * nachschauen ob er im Editor
					     * selektiert wurde dann muss diese
					     * selektion gelöscht werden um
					     * Doppelabspeicherung zu verhindern
					     */
					    Platz newPlatz = new Platz();
					    newPlatz.setNummer(Integer
						    .valueOf(lbls[i][j]
							    .getText()));
					    newPlatz.setReihe(reihe);

					    for (Platz p : selectedPlaces) {
						if ((newPlatz.getNummer() == p
							.getNummer())
							&& (newPlatz.getReihe() == p
								.getReihe())) {
						    selectedPlaces.remove(p);
						    break;
						}
					    }
					    break;
					} else if (pl.getStatus() == PlatzStatus.RESERVIERT
						&& pl.getAuffuehrung() == auffuehrung) {
					    lbls[i][j]
						    .setBackground(reserviert);
					    /*
					     * nachschauen ob er im Editor
					     * selektiert wurde dann muss diese
					     * selektion gelöscht werden um
					     * Doppelabspeicherung zu verhindern
					     */
					    Platz newPlatz = new Platz();
					    newPlatz.setNummer(Integer
						    .valueOf(lbls[i][j]
							    .getText()));
					    newPlatz.setReihe(reihe);

					    for (Platz p : selectedPlaces) {
						if ((newPlatz.getNummer() == p
							.getNummer())
							&& (newPlatz.getReihe() == p
								.getReihe())) {
						    selectedPlaces.remove(p);
						    break;
						}
					    }
					    break;
					}
				    }
				}
				break;
			    }
			} catch (NullPointerException ex) {
			    log.error("Platz an der Stelle " + i + ", " + j
				    + " existiert noch nicht");
			}

		    }
		}
		platzNummer++;
	    }
	}
    }

    /**
     * Liefert eine Matrix mit dem Sitzplan.
     * 
     * @param parent
     *            Es muss ein Parent angegeben werden, welcher ein Typ Composite
     *            ist. Label sind dann Kinder von dem.
     * @param hinzufuegbar
     *            Hier wird bestimmt ob Labels, welche leere Sitze darstellen,
     *            klickbar sind oder nicht.
     * @param transaktion
     *            gibt eine Transaktion an, fuer weche der Sitzplatz angezeigt
     *            werden soll, wenn null, dann wird das einfach ignoriert
     * @return Eine Matrix mit Labeln, die den Sitzplan darstellen.
     */
    public Label[][] getSitzplan(Composite parent, boolean hinzufuegbar,
	    Transaktion transaktion) {
	if (this.empty == true) {
	    // Saal hat keine Reihen
	    Label lbl = new Label(parent, SWT.CENTER);
	    lbl.setText("Kein Sitzplan zu diesem Saal vorhanden");
	    this.lbls[0][0] = lbl;
	    return lbls;
	}
	for (int i = 0; i < lbls.length; i++) {
	    platzNummer = 1;
	    for (int j = 0; j < lbls[i].length; j++) {

		Label lbl = new Label(parent, SWT.LEFT);
		/*
		 * Tooltip: wichtig, um die Reihe, in welcher sich das Label
		 * befindet, beim Clickhandler herauszufinden! Also wenn man den
		 * Text aendert, auch dementsprechend den Code beim Clickhandler
		 * aendern!
		 */
		lbl.setToolTipText("Reihe " + (i + 1) + "; Sitznummer "
			+ platzNummer);
		lbl.setLayoutData(new GridData(20, 20));
		lbl.setBackground(frei);
		lbl.setText("" + platzNummer);

		for (Reihe r : reihen) {
		    reihe = r;
		    int reihennr = Integer.valueOf(reihe.getBezeichnung().subSequence(0, 1).toString());
		    if ((reihennr -1) == i) {
			try {
			    if (reihe.getAnzplaetze() != 0) {
				for (Platz pl : reihe.getPlaetze()) {
				    if (pl.getNummer() == platzNummer
					    && pl.getAuffuehrung() == auffuehrung) {
					if (pl.getStatus() == PlatzStatus.GEBUCHT) {
					    lbl.setBackground(gebucht);

					    break;
					} else if (pl.getStatus() == PlatzStatus.RESERVIERT
						&& pl.getAuffuehrung() == auffuehrung) {
					    lbl.setBackground(reserviert);

					    break;
					}
				    }
				}
				break;
			    }
			} catch (NullPointerException ex) {
			    log.error("Platz an der Stelle " + i + ", " + j
				    + " existiert noch nicht");
			}

		    }
		}
		if (transaktion != null) {
		    for (Platz pl : transaktion.getPlaetze()) {
			int rNr = Integer.valueOf(pl.getReihe().getBezeichnung().subSequence(0, 1).toString());
			if (pl.getNummer() == platzNummer
				&& pl.getAuffuehrung() == auffuehrung
				&& (rNr - 1) == i) {
			    lbl.setBackground(markiert);
			    selectedPlaces.add(pl);
			}
		    }
		}

		lbls[i][j] = lbl;
		if (hinzufuegbar) {
		    lbl.addMouseListener(new MouseListener() {

			@Override
			public void mouseUp(MouseEvent e) {
			    // TODO Auto-generated method stub

			}

			@Override
			public void mouseDown(MouseEvent e) {
			    // TODO Auto-generated method stub
			    
			    Label selectedLabel = ((Label) e.getSource());
			    Platz newPlatz = new Platz();
			    int row = Integer.parseInt(selectedLabel
				    .getToolTipText().substring(6, 7));
			    newPlatz.setNummer(Integer.valueOf(selectedLabel
				    .getText()));
			    for (Reihe r : reihen) {
				reihe = r;
				int reihennr = Integer.valueOf(reihe.getBezeichnung().subSequence(0, 1).toString());
				if (reihennr == row) {
				    newPlatz.setReihe(reihe);
				    break;
				}
			    }
			    newPlatz.setKategorie(newPlatz.getReihe().getKategorie());
			    newPlatz.setAuffuehrung(auffuehrung);
			    if (selectedLabel.getBackground().equals(frei)) {
				selectedLabel.setBackground(markiert);
				selectedPlaces.add(newPlatz);
			    } else if (selectedLabel.getBackground().equals(
				    markiert)) {
				selectedLabel.setBackground(frei);
				/*
				 * Suche im Set selectedPlaces nach dem Platz,
				 * der deselektiert wurde, um ihn wieder aus dem
				 * Set zu entfernen
				 */
				for (Platz p : selectedPlaces) {
				    if ((newPlatz.getNummer() == p.getNummer())
					    && (newPlatz.getReihe() == p
						    .getReihe())) {
					p.setStatus(PlatzStatus.FREI);
					p.setTransaktion(null);
					selectedPlaces.remove(p);
					break;
				    }
				}

			    }
			    Activator.getDefault().refreshPrice(); // Um die Preise immer aktuell zu halten, wird der Editor refreshed
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
			    // TODO Auto-generated method stub

			}
		    });
		}
		platzNummer++;
	    }
	}

	return lbls;
    }

    /**
     * gibt die selektierten Plaetze zurueck
     * 
     * @return Set<Platz> mit selektierten plaetzen
     */
    public Set<Platz> getSelectedPlaetze() {

	return selectedPlaces;

    }
    
    /**
     * Gibt den Preis der selektierten Plaetze zurueck
     * Berechnet sich aus der Kategorie in welcher die Auffuehrung stattfindet
     * und wird gleichgesetzt der Reihe in der sich der Sitz befindet.
     *   
     * @return den Preis aller selektierten Plaetze
     * 
     */
    public String getPriceForPlaces() {
	int price = 0;
	for (Platz p : selectedPlaces) {
	    if(this.auffuehrung.getPreis() == PreisKategorie.MAXIMALPREIS)
		price += p.getKategorie().getPreismax().intValue();
	    else if(this.auffuehrung.getPreis() == PreisKategorie.MINDESTPREIS)
		price += p.getKategorie().getPreismin().intValue();
	    else if(this.auffuehrung.getPreis() == PreisKategorie.STANDARDPREIS)
		price += p.getKategorie().getPreisstd().intValue();
	    
	}
	return String.valueOf(price);
	
    }
    
    
    /**
     * erstellt Labels welche die Legende fuer die Matrix darstellen
     * 
     * @param parent
     *            Gibt an wo die Legende angezeigt werden soll
     * @return Ein LabelArray welches die Labels beinhaltet
     */
    public Label[] getLegende(Composite parent) {
	Label[] legends = new Label[8];

	Label lblFrei = new Label(parent, SWT.LEFT);
	lblFrei.setLayoutData(new GridData(20, 20));
	lblFrei.setBackground(frei);
	lblFrei.setText("");
	legends[0] = lblFrei;

	Label lblFreiData = new Label(parent, SWT.LEFT);
	lblFreiData.setText(" Frei");
	legends[1] = lblFreiData;

	Label lblReserviert = new Label(parent, SWT.LEFT);
	lblReserviert.setLayoutData(new GridData(20, 20));
	lblReserviert.setBackground(reserviert);
	lblReserviert.setText("");
	legends[2] = lblReserviert;

	Label lblReserviertData = new Label(parent, SWT.LEFT);
	lblReserviertData.setText(" Reserviert");
	legends[3] = lblReserviertData;

	Label lblVerkauft = new Label(parent, SWT.LEFT);
	lblVerkauft.setLayoutData(new GridData(20, 20));
	lblVerkauft.setBackground(gebucht);
	lblVerkauft.setText("");
	legends[4] = lblVerkauft;

	Label lblVerkauftData = new Label(parent, SWT.LEFT);
	lblVerkauftData.setText(" Verkauft");
	legends[5] = lblVerkauftData;

	Label lblMarkiert = new Label(parent, SWT.LEFT);
	lblMarkiert.setLayoutData(new GridData(20, 20));
	lblMarkiert.setBackground(markiert);
	lblMarkiert.setText("");
	legends[6] = lblMarkiert;

	Label lblMarkiertData = new Label(parent, SWT.LEFT);
	lblMarkiertData.setText(" Markiert");
	legends[7] = lblMarkiertData;

	// Label lblLegende = new Label(parent, SWT.LEFT);
	// lblLegende.setText("Legende: ");
	// lblLegende.setBackground(new Color(null, 255, 255, 255));
	// legends[0] = lblLegende;
	//
	// Label lblReserviert = new Label(parent, SWT.LEFT);
	// lblReserviert.setText("Reserviert: Grün");
	// lblReserviert.setBackground(new Color(null, 255, 255, 255));
	// legends[1] = lblReserviert;
	//
	// Label lblGebucht = new Label(parent, SWT.LEFT);
	// lblGebucht.setText("Gebucht: Rot");
	// lblGebucht.setBackground(new Color(null, 255, 255, 255));
	// legends[2] = lblGebucht;
	//
	// Label lblFrei = new Label(parent, SWT.LEFT);
	// lblFrei.setText("Frei: Hellgrün");
	// lblFrei.setBackground(new Color(null, 255, 255, 255));
	// legends[3] = lblFrei;

	return legends;
    }

    /**
     * Setzt die Columnanzahl des Parent für die Labels.
     * 
     * @param parent
     *            Gibt den Parent an, in welchen die Labels kommen sollen.
     * @return Einen Int der angibt, wie viele Columns gebraucht werden
     */
    public int getGridSize(Composite parent) {
	return lbls[0].length;
    }

}
