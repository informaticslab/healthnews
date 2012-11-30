package gov.cdc.irdu.healthnews.server;

import gov.cdc.irdu.healthnews.client.GoogleService;
import gov.cdc.irdu.healthnews.client.PersonService;
import gov.cdc.irdu.healthnews.shared.ArticleDTO;
import gov.cdc.irdu.healthnews.shared.CategoryDTO;
import gov.cdc.irdu.healthnews.shared.SearchDTO;
import gov.cdc.irdu.healthnews.shared.SessionID;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.nodes.TagNode;
import org.htmlparser.tags.CompositeTag;
import org.htmlparser.tags.Div;
import org.htmlparser.tags.ImageTag;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.SimpleNodeIterator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 
 * @author <a href="mailto:jmrives@spiral-soft.com">Joel M. Rives</a>
 * Created on Apr 27, 2011
 *
 */

@Service("googleService")
public class GoogleServiceImpl implements GoogleService
{
    private static final String PREFIX = "http://news.google.com/news/search?aq=f&pz=1&cf=all&ned=us&hl=en&q=";
    private static final long CACHE_TIMEOUT = 1000 * 60 * 15; // 15 minutes
    private static final long RETRY_INTERVAL = 1000 * 60 * 10; // 10 minutes

    @Autowired PersonService personService;
    
    private static Date queryFailureTimestamp;
    private static Map<String, CacheEntry> cache = new HashMap<String, CacheEntry>();
    private static Logger LOG = Logger.getLogger(GoogleServiceImpl.class);

    public SearchDTO search(CategoryDTO category) {
        String query = createQuery(category);
        String urlString;
        try
        {
            urlString = PREFIX + URLEncoder.encode(query, "UTF-8");
        }
        catch (UnsupportedEncodingException e)
        {
            throw new RuntimeException("Unable to encode query string", e);
        }

        SearchDTO search = getCachedResponse(urlString);
        
        if (null != search) {
            LOG.info("Using cached response");
            return search;
        }
        
        if (inFailureMode()) 
            throw new RuntimeException("Google is temporarily unavailable");
        
        try
        {
            LOG.info("Sending: " + urlString);
            URL url = new URL(urlString);
            Parser parser = new Parser(url.openConnection());
            NodeList nodes = parser.parse(null);
            nodes = extractStories(nodes);
            search = buildResponse(category, nodes);
            cache.put(query, new CacheEntry(search));
        }
        catch (Exception e)
        {
            queryFailureTimestamp = new Date();
            LOG.error(e.getMessage());
            throw new RuntimeException("Query failed", e);
        }
        
        return search;
    }
    
    public List<SearchDTO> search(SessionID sid) {
        List<CategoryDTO> categories = personService.getUserCategories(sid);                
        List<SearchDTO> searches = new ArrayList<SearchDTO>(categories.size());
        
        for (CategoryDTO category: categories) {
            searches.add(search(category));
        }
        
        return searches;
    }
    
    private SearchDTO buildResponse(CategoryDTO category, NodeList stories) {
        SearchDTO search = new SearchDTO(category.getTitle());
        
        for (SimpleNodeIterator iter = stories.elements(); iter.hasMoreNodes();) {
            Node storyNode = iter.nextNode();
            ArticleDTO article = extractStory(storyNode);
            String dateString = article.getPublishedDate();
            article.setColor(getColor(category, dateString));
            search.getArticles().add(article);
        }

        return search;
    }
    
    private SimpleDateFormat Formatter = new SimpleDateFormat("MMM d, yyyy");
    private static final long ONE_WEEK = 1000 * 60 * 60 * 24 * 7;
    
    private String getColor(CategoryDTO category, String dateString) {
        if (null == dateString)
            return null;
        
        if (dateString.contains("ago")) 
            return category.getBrightColor();
       
            
        Date date;
        try
        {
            date = Formatter.parse(dateString);
        }
        catch (ParseException e)
        {
            return null;
        }
        
        Date oneWeekAgo = new Date(new Date().getTime() - ONE_WEEK);
       
        if (date.before(oneWeekAgo)) 
                return category.getDarkColor();
        
        return category.getMediumColor();
    }
    
    private String createQuery(CategoryDTO category) {
        StringBuffer buffer = new StringBuffer();
        
        String[] queryTerms = category.getQueryTerms().split(",");
        for (String term: queryTerms) {
            buffer.append("\"");
            buffer.append(term.trim());
            buffer.append("\" ");
        }
        
        if (null != category.getExcludeTerms()) {
            queryTerms = category.getExcludeTerms().split(",");
            for (String term: queryTerms) {
                buffer.append("-\"");
                buffer.append(term.trim());
                buffer.append("\" ");
            }
        }
        
        return buffer.toString();
    }
    
    private String extract(Node node, String className) {
        NodeList children = node.getChildren();
        NodeList nodes = children.extractAllNodesThatMatch(new ClassNameNodeFilter(className), true);
        if (null == nodes || nodes.size() == 0)
            return null;
        CompositeTag tag = (CompositeTag) nodes.elementAt(0);
        return tag.getStringText();
    }
    
    private String extractImageUrl(Node node) {
        LinkTag linkTag = (LinkTag) node.getFirstChild();
        ImageTag imageTag = (ImageTag) linkTag.getFirstChild();
        return imageTag.getImageURL();
    }
    
    private ArticleDTO extractStory(Node storyNode) {
        ArticleDTO article = new ArticleDTO();
        NodeList children = storyNode.getChildren();
        NumberFormat formatter = DecimalFormat.getInstance();
        
        for (SimpleNodeIterator iter = children.elements(); iter.hasMoreNodes();) {
            Node node = iter.nextNode();
            
            if (!(node instanceof TagNode))
                continue;
            
            TagNode child = (TagNode) node;
            String classValue = child.getAttribute("class");
            
            if ("thumbnail".equals(classValue)) {
                article.setImageUrl(extractImageUrl(child));
            } else if ("title".equals(classValue)) {
                String title = extract(child, "titletext");
                article.setTitle(title);
                article.setUrl(extractUrl(child));
            } else if ("sub-title".equals(classValue)) {
                String date = extract(child, "date");
                if (null != date && date.length() > 0) 
                    date = date.substring(5, date.length() - 5);
                article.setPublishedDate(date);
                article.setPublisher(extract(child, "source"));
            } else if ("body".equals(classValue)) {
                article.setContent(extract(child, "snippet"));
                String moreCoverage = extract(child, "more-coverage-text");
                if (null != moreCoverage) {
                    String[] words = moreCoverage.split(" ");
                    try
                    {
                        long value = (Long) formatter.parse(words[1]);
                        article.setRelatedStories((int) value);
                    }
                    catch (ParseException e)
                    {
                        LOG.error("Failed to parse related stories nummber", e);
                        article.setRelatedStories(0);
                    }
                } else {
                    article.setRelatedStories(0);
                }
            }
        }

        return article;
    }
    
    private NodeList extractStories(NodeList list) {
        NodeList stories = list.extractAllNodesThatMatch(new NodeFilter() {
            private static final long serialVersionUID = 1L;
            public boolean accept(Node node) {
                if (!(node instanceof Div))
                    return false;
                
                Div div = (Div) node;
                String value = div.getAttribute("class");
                
                if (null == value || value.length() == 0)
                    return false;
                
                if (value.startsWith("story"))
                    return true;
                
                return false;
            }               
        }, true);
        
        return stories;
    }
    
    private String extractUrl(Node node) {
        Node firstNode = node.getFirstChild();
        LinkTag linkTag = (LinkTag) firstNode.getNextSibling();
        return linkTag.extractLink();
    }
    
    private SearchDTO getCachedResponse(String key) {
        if (!cache.containsKey(key))
            return null;
        
        CacheEntry entry = cache.get(key);
        Date now = new Date();
        long elapsed = now.getTime() - entry.getTimestamp().getTime();
        
        if (elapsed > CACHE_TIMEOUT) {
            cache.remove(key);
            return null;
        }
        
        return entry.getSearch();
    }
    
    private boolean inFailureMode() {
        if (null == queryFailureTimestamp)
            return false;
        
        Date now = new Date();
        long elapsed = now.getTime() - queryFailureTimestamp.getTime();
        boolean failureMode = elapsed < RETRY_INTERVAL;
        
        if (!failureMode)
            queryFailureTimestamp = null;
        
        return failureMode;
    }

    
    private class CacheEntry {
        private SearchDTO search;
        private Date timestamp;
        
        public CacheEntry(SearchDTO search) {
            this.search = search;
            this.timestamp = new Date();
        }

        public SearchDTO getSearch()
        {
            return this.search;
        }

        public Date getTimestamp()
        {
            return this.timestamp;
        }        
    }
    
    private class ClassNameNodeFilter implements NodeFilter {
        private static final long serialVersionUID = 1L;

        private String className;
        
        public ClassNameNodeFilter(String className) {
            this.className = className;
        }
        
        public boolean accept(Node node) {
            if (node instanceof TagNode) {
                TagNode tag = (TagNode) node;
                String value = tag.getAttribute("class");
                if (null != value && value.length() > 0) {
                    if (value.trim().startsWith(className))
                        return true;
                }
            }
            return false;
        }
        
    }

}
