package at.ticketline.util;

import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Text;

/**
 * Klasse mit statischen Methoden, um Instant Search - Feature in Views zu
 * erleichtern (Sofortige Suche, sobald etwas eingegeben wird)
 * 
 * @author Georg Fuderer
 * 
 * @see at.ticketline.util.SearchView
 */
public class InstantSearch {

    /**
     * Fügt einem Text Widget einen Change Listener hinzu Führt anschließend die
     * searchHandler - Methode der übergebenen View aus
     * 
     * @param widget
     *            Widget, welches InstantSearch bei Eingabe unterstützen soll
     * @param view
     *            View, welche das Widget enthält und die searchHandler Funktion
     *            bereitstellt
     * 
     * @see at.ticketline.util.SearchView
     * 
     */
    public static void addInstantSearch(Text widget, final SearchView view) {

	widget.addListener(SWT.CHANGED, new Listener() {

	    @Override
	    public void handleEvent(Event e) {

		Text text = (Text) e.widget;

		if (!text.getText().contains("*")
			&& text.getText().length() > 0) {
		    text.setText(text.getText() + "*");
		    if (text.getText()
			    .substring(text.getText().length() - 1,
				    text.getText().length()).equals("*"))
			text.setSelection(text.getText().length() - 1);
		}
		if (e.character == '\b' && text.getText().equals("*"))
			text.setText("");
		
		view.searchHandler();
	    }
	});
    }

    /**
     * Fügt einem Combo Widget einen Change Listener hinzu Führt anschließend
     * die searchHandler - Methode der übergebenen View aus
     * 
     * @param widget
     *            Widget, welches InstantSearch bei Eingabe unterstützen soll
     * @param view
     *            View, welche das Widget enthält und die searchHandler Funktion
     *            bereitstellt
     * 
     * @see at.ticketline.util.SearchView
     */
    public static void addInstantSearch(Combo widget, final SearchView view) {

	widget.addListener(SWT.Selection, new Listener() {

	    @Override
	    public void handleEvent(Event e) {
		view.searchHandler();
	    }
	});
    }

    /**
     * Fügt einem ComboViewer Widget einen Change Listener hinzu Führt
     * anschließend die searchHandler - Methode der übergebenen View aus
     * 
     * @param widget
     *            Widget, welches InstantSearch bei Eingabe unterstützen soll
     * @param view
     *            View, welche das Widget enthält und die searchHandler Funktion
     *            bereitstellt
     * 
     * @see at.ticketline.util.SearchView
     */
    public static void addInstantSearch(ComboViewer widget,
	    final SearchView view) {

	widget.addSelectionChangedListener(new ISelectionChangedListener() {

	    @Override
	    public void selectionChanged(SelectionChangedEvent event) {
		view.searchHandler();

	    }
	});
    }

    /**
     * Fügt einem Scale Widget einen Change Listener hinzu Führt anschließend
     * die searchHandler - Methode der übergebenen View aus
     * 
     * @param scaleWidget
     *            Widget, welches InstantSearch bei Eingabe unterstützen soll
     * @param view
     *            View, welche das Widget enthält und die searchHandler Funktion
     *            bereitstellt
     * 
     * @see at.ticketline.util.SearchView
     */
    public static void addInstantSearch(Scale scaleWidget, final SearchView view) {

	scaleWidget.addSelectionListener(new SelectionListener() {

	    @Override
	    public void widgetSelected(SelectionEvent e) {
		view.searchHandler();
	    }

	    @Override
	    public void widgetDefaultSelected(SelectionEvent e) {
		view.searchHandler();
	    }
	});
    }

}
