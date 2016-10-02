package at.ticketline.service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.io.IOException;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

import at.ticketline.entity.News;
import at.ticketline.log.LogFactory;
import at.ticketline.log.Logger;
import at.ticketline.service.interfaces.NewsService;

/**
 * Auslesen von RSS-Feeds zur News-Generierung
 * 
 * @author Bernhard Sadransky
 */
public class RSSNewsService implements NewsService {
    
    protected Logger log = LogFactory.getLogger(this.getClass());

    private URL feedURL;
    
    public RSSNewsService(String url) throws MalformedURLException {
	this.feedURL = new URL(url);
    }
    
    /**
     * Erzeugt eine News-Liste aus einem RSS-Feed
     * 
     * @return News-Liste
     * @throws IOException falls ein Fehler beim Auslesen des RSS-Feeds auftritt
     */
    @Override
    public List<News> getNews() throws IOException {
	SyndFeedInput input = new SyndFeedInput();
	SyndFeed feed;
	
        try {
	    feed = input.build(new XmlReader(feedURL));
	} catch (Exception e) {
	    throw new IOException();
	}
	
	List<SyndEntry> entries = (List <SyndEntry>) feed.getEntries();
	
	List<News> newsList = new ArrayList<News>();
	
        for (SyndEntry entry : entries) {
            News news = parseNews(entry);
            
            if(news != null)
        	newsList.add(news);
        }
        
        return newsList;
    }
    
    /* erzeugt ein News-Objekt falls alle benoetigten Parameter vorhanden */
    private News parseNews(SyndEntry entry) {	
	String title = entry.getTitle();
	String link = entry.getLink();
	Date date = entry.getPublishedDate();
	String desc = null;
	if(entry.getDescription() != null)
	    desc = entry.getDescription().getValue();
	
	if(title == null || link == null || desc == null || date == null) {
	    log.error("Incomplete rss-feed entry");
	    return null;
	}
	
	News news = new News();
	news.setTitel(title);
	news.setLink(link);
	news.setText(desc);
	news.setDatum(date);
	
	return news;
    }
}