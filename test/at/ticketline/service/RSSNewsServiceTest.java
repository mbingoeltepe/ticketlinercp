package at.ticketline.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.notNullValue;

import org.junit.Test;

import at.ticketline.entity.News;

public class RSSNewsServiceTest {
    
    private RSSNewsService newsService;
    private String newsFeed = "http://rss.orf.at/science.xml";

    @Test
    public void constructorShouldNotThrowException() throws MalformedURLException {
	newsService = new RSSNewsService(newsFeed);
	
	assertThat(newsService, notNullValue());
    }
    
    @Test(expected=MalformedURLException.class)
    public void constructorShouldThrowException() throws MalformedURLException {
	newsService = new RSSNewsService("!!!malformedURL!!!");
    }
    
    @Test
    public void getNewsShouldReturnNewsList() throws IOException {
	newsService = new RSSNewsService(newsFeed);

	List<News> newsList = newsService.getNews();
		
	assertThat(newsList, notNullValue());
    }
}
