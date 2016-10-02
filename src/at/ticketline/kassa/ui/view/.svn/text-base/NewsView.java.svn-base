package at.ticketline.kassa.ui.view;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.awt.Desktop;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.widgets.FormText;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Hyperlink;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;
import org.eclipse.ui.part.ViewPart;

import at.ticketline.entity.News;
import at.ticketline.kassa.ui.interfaces.INeedLogin;
import at.ticketline.log.LogFactory;
import at.ticketline.log.Logger;
import at.ticketline.service.RSSNewsService;
import at.ticketline.service.interfaces.NewsService;

/**
 * Liest News aus RSS-Feed und zeigt diese an
 * 
 * @author Bernhard Sadransky
 * 
 */
public class NewsView extends ViewPart implements INeedLogin {

    protected Logger log = LogFactory.getLogger(this.getClass());

    public static final String ID = "at.ticketline.view.news";

    private static final String newsFeed = "http://rss.orf.at/science.xml";

    private FormToolkit toolkit;
    private ScrolledForm form;

    private Composite c;

    NewsService newsService;

    @Override
    public void createPartControl(Composite parent) {
	this.c = parent;
	toolkit = new FormToolkit(parent.getDisplay());
	form = toolkit.createScrolledForm(parent);
	form.setText("Unternehmensnews");
	form.getBody().setLayout(new TableWrapLayout());

	List<News> newsList;

	try {
	    newsList = newsService.getNews();

	} catch (IOException e) {
	    log.error("Could not retrieve news");
	    MessageDialog.openError(parent.getShell(), "News anzeigen",
		    "News konnten nicht abgerufen werden!");
	    return;
	}

	StringBuffer sb = new StringBuffer("<form>");

	for (News news : newsList) {
	    sb.append("<p>");
	    sb.append("<b>" + news.getTitel() + "</b>");
	    sb.append("<br/>");
	    sb.append(news.getDatum());
	    sb.append("<br/>");
	    sb.append(stripNewlinesAtEnd(news.getText()));
	    sb.append("</p>");
	}
	sb.append("</form>");

	FormText titel = toolkit.createFormText(form.getBody(), false);
	titel.setText(sb.toString(), true, false);

	PlatformUI.getWorkbench().getHelpSystem()
		.setHelp(c, "TicketlineRCP.news"); // contextsensitive hilfe
    }

    @Override
    public void init(IViewSite site) throws PartInitException {
	super.init(site);
	try {
	    newsService = new RSSNewsService(newsFeed);
	} catch (MalformedURLException e) {
	    log.error("Invalid rss-feed url");
	    throw new PartInitException("Invalid rss-feed url");
	}
    }

    @Override
    public void setFocus() {
	form.setFocus();
	if (c != null) // Context sensitive help
	    c.setFocus(); // context sensitive help

    }

    @Override
    public void dispose() {
	toolkit.dispose();
	super.dispose();
    }

    // entfernt alle aufeinander folgende Zeilenumbrueche vom Ende des String
    private String stripNewlinesAtEnd(String s) {
	int j = 0;

	for (int i = (s.length() - 1); s.charAt(i) == '\n'; i--)
	    j++;

	return s.substring(0, s.length() - j);
    }
}