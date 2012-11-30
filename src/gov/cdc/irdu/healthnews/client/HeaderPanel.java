/**
 * 
 */
package gov.cdc.irdu.healthnews.client;

import gov.cdc.irdu.healthnews.shared.PersonDTO;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * This is the header for the HealthNews page.
 * 
 * @author Joel M. Rives
 * Apr 27, 2011
 */
public class HeaderPanel extends HorizontalPanel {

	private Label welcomeLabel;
	private Label loginLabel;
	
	private HealthNews controller;

	public HeaderPanel(HealthNews parent) {
	    controller = parent;
		setStyleName("headerPanel");
		
		Image logo = new Image("images/header.gif");
		logo.setStyleName("logo");
		add(logo);
		
		VerticalPanel headerInfoPanel = new VerticalPanel();
		headerInfoPanel.setStyleName("headerInfoPanel");
		headerInfoPanel.setSpacing(20);
		add(headerInfoPanel);
	//	setCellHorizontalAlignment(headerInfoPanel, HasHorizontalAlignment.ALIGN_RIGHT);
		setCellVerticalAlignment(headerInfoPanel, HasVerticalAlignment.ALIGN_TOP);
		
		HorizontalPanel controlPanel = new HorizontalPanel();
		controlPanel.setSpacing(6);
		headerInfoPanel.add(controlPanel);
		
		controlPanel.add(new Image("images/aqua_arrow.gif"));
		controlPanel.add(createWelcomeLabel());
		controlPanel.add(new Label("|"));
		controlPanel.add(createCustomizeLabel());
		controlPanel.add(new Label("|"));
		controlPanel.add(createLoginLabel());
		controlPanel.add(createHelpButton());
		
		Label version = new Label("Version 1.0");
		headerInfoPanel.add(version);
		headerInfoPanel.setCellHorizontalAlignment(version, HasHorizontalAlignment.ALIGN_CENTER);
		setCurrentUser(null);
	}
	
	private Label createCustomizeLabel() {
		Label customize = new Label("Customize");
		customize.setStyleName("labelButton");
		customize.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				// TODO Launch Customize dialog	
				Window.alert("Not implemented yet");
			}			
		});

		return customize;
	}
	
	private Image createHelpButton() {
		Image help = new Image("images/help_icon.gif");
		help.setStyleName("help");
		help.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				Window.alert("Not implemented yet");
			}			
		});

		return help;
	}
	
	private Label createLoginLabel() {
		loginLabel = new Label("Log in");
		loginLabel.setStyleName("labelButton");
		loginLabel.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				Window.alert("Goodbye");
			}			
		});

		return loginLabel;
	}
	
	private Label createWelcomeLabel() {
		welcomeLabel = new Label("Register");
		welcomeLabel.setStyleName("labelButton");
		welcomeLabel.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				Label label = (Label) event.getSource();
				if (label.getText().equalsIgnoreCase("Register"))
					register();
				else
					editAccount();
			}			
		});
		
		return welcomeLabel;
	}
	
	private void editAccount() {
		Window.alert("Not implemented yet");
	}
	
	private void register() {
		Window.alert("Not implemented yet");
	}
	
    private void setCurrentUser(PersonDTO person) {
        if (null == person) {
            welcomeLabel.setText("Register");
            loginLabel.setText("Log in");
            return;
        }
        
        welcomeLabel.setText("Welcome, " + person.getGivenName());
        loginLabel.setText("Log out");
    }
    
}
