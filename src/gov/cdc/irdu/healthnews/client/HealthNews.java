package gov.cdc.irdu.healthnews.client;

import gov.cdc.irdu.gwtjit.client.data.TreeNode;
import gov.cdc.irdu.gwtjit.client.event.ClickCallback;
import gov.cdc.irdu.gwtjit.client.event.ComputeCallback;
import gov.cdc.irdu.gwtjit.client.event.LabelCallback;
import gov.cdc.irdu.gwtjit.client.event.TipsCallback;
import gov.cdc.irdu.gwtjit.client.option.CanvasOptions;
import gov.cdc.irdu.gwtjit.client.option.ControllerOptions;
import gov.cdc.irdu.gwtjit.client.option.EventsOptions;
import gov.cdc.irdu.gwtjit.client.option.TipsOptions;
import gov.cdc.irdu.gwtjit.client.vis.SquarifiedTreeMap;
import gov.cdc.irdu.healthnews.shared.ArticleDTO;
import gov.cdc.irdu.healthnews.shared.PersonDTO;
import gov.cdc.irdu.healthnews.shared.SearchDTO;
import gov.cdc.irdu.healthnews.shared.SessionID;

import java.awt.Font;
import java.awt.FontMetrics;
import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class HealthNews implements EntryPoint {
	
    private static SessionID sid = null;
	
    private final GoogleServiceAsync googleService = GWT.create(GoogleService.class);
	private final PersonServiceAsync personService = GWT.create(PersonService.class);
	
	private HeaderPanel headerPanel;
	private AbsolutePanel treePanel;
	private FooterPanel footerPanel;
	
	private PersonDTO currentUser = null;
	private List<JitPanel> nodeList = new ArrayList<JitPanel>();
	
	public void onModuleLoad() {
		RootPanel root = RootPanel.get();
		
		VerticalPanel mainPanel = new VerticalPanel();
		mainPanel.setStyleName("mainPanel");
		root.add(mainPanel);
		
		headerPanel = new HeaderPanel(this);
		mainPanel.add(headerPanel);
		
		treePanel = new AbsolutePanel();
		treePanel.getElement().setId("tree-space");
		treePanel.setStyleName("treePanel");
		mainPanel.add(treePanel);
		mainPanel.setCellHorizontalAlignment(treePanel, HasHorizontalAlignment.ALIGN_CENTER);
		mainPanel.setCellVerticalAlignment(treePanel, HasVerticalAlignment.ALIGN_MIDDLE);
		
		footerPanel = new FooterPanel();
		mainPanel.add(footerPanel);
				
		if (null == sid) {
		    updateUser(null);
		    return;
		}
		
		personService.getCurrentUser(sid, new AsyncCallback<PersonDTO>() {
            public void onFailure(Throwable caught)
            {
                sid = null; // Session must be invalid
                updateUser(null);
            }

            public void onSuccess(PersonDTO person)
            {
                updateUser(person);   
            }		    
		});
	}
	
	public void updateUser(PersonDTO person) {
	    currentUser = person;
	    footerPanel.updateSession(sid);
	    showTreeMap();
	}
	
	private ControllerOptions createController() {
		ControllerOptions controller = new ControllerOptions();
		
		controller.setLabelCallback(new LabelCallback() {
			public void onCreateLabel(JavaScriptObject domElement, JavaScriptObject node) {
				JitPanel dom = new JitPanel((Element) domElement);
				TreeNode treeNode = new TreeNode(node);				
				dom.getElement().setInnerHTML(treeNode.getName());
				dom.setStyleName("treeNode");
				nodeList.add(dom);
			}

			public void onPlaceLabel(JavaScriptObject domElement, JavaScriptObject node) {
				// NO-OP
			}
	    });
		
		controller.setComputeCallback(new ComputeCallback() {
            public void onBeforeCompute(JavaScriptObject node)
            {
                // NO-OP
            }

            public void onAfterCompute()
            {
                for (JitPanel node: nodeList) {
                    if (0 == node.getOffsetHeight() || 0 == node.getOffsetWidth())
                        continue;
                    
                    if (node.getElement().getId() == "root") {
                        node.setStyleName("rootNode");
                    } else {
                        fitTextInBox(node);
                    }
                }
            }
		});
		
		return controller;
	}
	
	private EventsOptions createEvents(final SquarifiedTreeMap treeMap) {
	    EventsOptions events = new EventsOptions();
	    events.setEnable(true);
	    
	    events.setClickCallback(new ClickCallback() {
	    	public void onClick(JavaScriptObject jsNode, JavaScriptObject eventInfo) {
				if (null == jsNode)
					return;
				
				TreeNode treeNode = new TreeNode(jsNode);
				
				if (treeNode.getId().equals("root"))
					return;
				
				String url = treeNode.getStringDataValue("url");
				Window.open(url, "news", "");
			}
	    });
	    
	    events.setRightClickCallback(new ClickCallback() {
			public void onClick(JavaScriptObject jsNode, JavaScriptObject eventInfo) {
				if (null == jsNode)
					return;
				
				treeMap.enter(jsNode);
			}
	    });
	    
	    return events;
	}
	
	private TipsOptions createTips() {
		TipsOptions tips = new TipsOptions();
		tips.setEnable(true);
		tips.setOffsetX(20);
		tips.setOffsetY(20);
		
		tips.setTipsCallback(new TipsCallback() {
			public void onShow(JavaScriptObject tip, JavaScriptObject node) {
				JitPanel root = new JitPanel((Element) tip);
				TreeNode treeNode = new TreeNode(node);
				
				while (root.getElement().hasChildNodes()) 
					root.getElement().removeChild(root.getElement().getFirstChild());
				
				VerticalPanel mainPanel = new VerticalPanel();
				mainPanel.setStyleName("tipPanel");
				root.add(mainPanel);
				
				Label title = new Label(treeNode.getName());
				mainPanel.add(title);
				
				HorizontalPanel bodyPanel = new HorizontalPanel();
				bodyPanel.setSpacing(6);
				mainPanel.add(bodyPanel);
				
				String imageUrl = treeNode.getStringDataValue("imageUrl");
				Image image = new Image(null == imageUrl ? "images/image_placeholder.gif" : imageUrl);
				bodyPanel.add(image);
				
				String content = treeNode.getStringDataValue("content");
				TextArea body = new TextArea();
				body.setText(content);
				body.setReadOnly(true);
				body.setStyleName("tipContent");
				bodyPanel.add(body);
				
				HorizontalPanel bottomPanel = new HorizontalPanel();
				bottomPanel.setSpacing(6);
				mainPanel.add(bottomPanel);
				
				String dateString = treeNode.getStringDataValue("publishedDate");
				Label date = new Label(null == dateString ? "No date" : dateString);
				date.setStyleName("tipDate");
				bottomPanel.add(date);
				
				int area = treeNode.getIntegerDataValue("$area");
				Label related = new Label(area + " related articles");
				related.setStyleName("tipRelated");
				bottomPanel.add(related);
				
				String publisher = treeNode.getStringDataValue("publisher");
				Label source = new Label("source: " + publisher);
				source.setStyleName("tipSource");
				bottomPanel.add(source);
			}			
		});
		
		return tips;
	}
	
	private SquarifiedTreeMap createTreeMap(String here, int width, int height) {
	    CanvasOptions canvas = new CanvasOptions(here, width, height);
	    final SquarifiedTreeMap treeMap = new SquarifiedTreeMap(canvas);
	    treeMap.setTitleHeight(0);
	    treeMap.setAnimate(true);
	    treeMap.setOffset(1);
	    treeMap.setLevelsToShow(2);
	    treeMap.setDuration(700);
	    
	    treeMap.setEventsOptions(createEvents(treeMap));
	    treeMap.setTipsOptions(createTips());
	    treeMap.setControllerOptions(createController());
    
	    return treeMap;
	}
	
	private void fitTextInBox(JitPanel node) {
	    String text = node.getElement().getInnerHTML();
	    int maxWidth = node.getOffsetWidth();
	    int maxHeight = node.getOffsetHeight();
        int fontSize = 1;
	    
	    Label label = new Label(text);
	    label.setStyleName("treeNode");
	    Style style = label.getElement().getStyle();
	    style.setFontSize(1, Unit.PX);
	    label.setWidth((maxWidth - 2) + "px");
	    label.setWordWrap(true);
	    Document.get().getBody().appendChild(label.getElement());

	    while(label.getOffsetHeight() < (maxHeight - 4)) 
	        style.setFontSize(++fontSize, Unit.PX);

	    Document.get().getBody().removeChild(label.getElement());
	    
	    String word = getLongestWord(text);
	    int width = word.length() * fontSize;
	    
	    while (width > maxWidth - 4) {
	        fontSize--;
	        width = word.length() * fontSize;
	    }
        
	    node.getElement().getStyle().setFontSize(fontSize, Unit.PX);
	}
	
	private String getLongestWord(String line) {
	    String[] words = line.split(" ");
	    String longest = words[0].trim();
	    
	    for (String word: words) {
	        word = word.trim();
	        if (word.length() > longest.length())
	            longest = word;
	    }
	    
	    return longest;
	}
	
	private void showTreeMap() {
        treePanel.clear();
        
		int width = Window.getClientWidth() - 2;
		int height = Window.getClientHeight() - headerPanel.getOffsetHeight() - footerPanel.getOffsetHeight() - 2;
		
		final SquarifiedTreeMap treeMap = createTreeMap("tree-space", width, height);

		googleService.search(sid, new AsyncCallback<List<SearchDTO>>() {
			public void onFailure(Throwable caught)
			{
				Window.alert("Failed to get data from Google");                
			}
	
			public void onSuccess(List<SearchDTO> searches)
			{
				TreeNode root = new TreeNode("root", "Root Node");
	          
				for (SearchDTO search: searches) {
					root.addChild(toTreeNode(search));
				}
	          
				treeMap.loadData(root);
				treeMap.plot();
				treeMap.refresh();
			}
		});
	}

	private TreeNode toTreeNode(SearchDTO search) {
	    TreeNode node = new TreeNode(search.getTitle(), search.getTitle());
	    int totalArea = 0;
	    
	    for (ArticleDTO article: search.getArticles()) {
	        TreeNode child = new TreeNode(article.getTitle(), article.getTitle());
	        child.addData("$area", article.getRelatedStories());
	        child.addData("$color", "#" + article.getColor());
	        child.addData("content", article.getContent());
	        child.addData("imageUrl", article.getImageUrl());
	        child.addData("publishedDate", article.getPublishedDate());
	        child.addData("publisher", article.getPublisher());
	        child.addData("relatedStories", article.getRelatedStories());
	        child.addData("url", article.getUrl());
	        node.addChild(child);
	        totalArea += article.getRelatedStories();
	    }
	    
	    node.addData("$area", totalArea);
	    node.addData("$color", "#FFFFFF");
	    return node;
	}

}
