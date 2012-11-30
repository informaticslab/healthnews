package gov.cdc.irdu.healthnews.shared;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * 
 * @author <a href="mailto:jmrives@spiral-soft.com">Joel M. Rives</a>
 * Created on Sep 14, 2011
 *
 */
public class SearchDTO implements IsSerializable
{
    private String title;
    private List<ArticleDTO> articles;
    
    public SearchDTO() { }
    
    public  SearchDTO(String title) {
        this.title = title;
        this.articles = new ArrayList<ArticleDTO>();
    }

    /**
     * @return the title
     */
    public String getTitle()
    {
        return this.title;
    }

    /**
     * @return the articles
     */
    public List<ArticleDTO> getArticles()
    {
        return this.articles;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title)
    {
        this.title = title;
    }

    /**
     * @param articles the articles to set
     */
    public void setArticles(List<ArticleDTO> articles)
    {
        this.articles = articles;
    }

}
