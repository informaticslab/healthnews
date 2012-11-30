package gov.cdc.irdu.healthnews.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * 
 * @author <a href="mailto:jmrives@spiral-soft.com">Joel M. Rives</a>
 * Created on Sep 14, 2011
 *
 */
public class ArticleDTO implements IsSerializable
{
    private String title;
    private String color;
    private String imageUrl;
    private String url;
    private String publisher;
    private String publishedDate;
    private String content;
    private int relatedStories;
    
    public ArticleDTO() { }

    /**
     * @return the title
     */
    public String getTitle()
    {
        return this.title;
    }

    /**
     * @return the color
     */
    public String getColor()
    {
        return this.color;
    }

    /**
     * @return the imageUrl
     */
    public String getImageUrl()
    {
        return this.imageUrl;
    }

    /**
     * @return the url
     */
    public String getUrl()
    {
        return this.url;
    }

    /**
     * @return the publisher
     */
    public String getPublisher()
    {
        return this.publisher;
    }

    /**
     * @return the publishedDate
     */
    public String getPublishedDate()
    {
        return this.publishedDate;
    }

    /**
     * @return the content
     */
    public String getContent()
    {
        return this.content;
    }

    /**
     * @return the relatedStories
     */
    public int getRelatedStories()
    {
        return this.relatedStories;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title)
    {
        this.title = title;
    }

    /**
     * @param color the color to set
     */
    public void setColor(String color)
    {
        this.color = color;
    }

    /**
     * @param imageUrl the imageUrl to set
     */
    public void setImageUrl(String imageUrl)
    {
        this.imageUrl = imageUrl;
    }

    /**
     * @param url the url to set
     */
    public void setUrl(String url)
    {
        this.url = url;
    }

    /**
     * @param publisher the publisher to set
     */
    public void setPublisher(String publisher)
    {
        this.publisher = publisher;
    }

    /**
     * @param publishedDate the publishedDate to set
     */
    public void setPublishedDate(String publishedDate)
    {
        this.publishedDate = publishedDate;
    }

    /**
     * @param content the content to set
     */
    public void setContent(String content)
    {
        this.content = content;
    }

    /**
     * @param relatedStories the relatedStories to set
     */
    public void setRelatedStories(int relatedStories)
    {
        this.relatedStories = relatedStories;
    }
    
}
