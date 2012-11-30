/**
 * 
 */
package gov.cdc.irdu.healthnews.client;

import gov.cdc.irdu.healthnews.shared.CategoryDTO;
import gov.cdc.irdu.healthnews.shared.SessionID;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * This is the footer for the HealthNews page.
 * 
 * @author Joel M. Rives
 * Apr 27, 2011
 */
public class FooterPanel extends VerticalPanel {

	private final PersonServiceAsync personService = GWT.create(PersonService.class);
	
	private Grid keyChart;
	
	public FooterPanel() {
		setStyleName("footerPanel");
		
		VerticalPanel keyPanel = new VerticalPanel();
		keyPanel.setStyleName("keyPanel");
		add(keyPanel);
		setCellHorizontalAlignment(keyPanel, HasHorizontalAlignment.ALIGN_CENTER);
		setCellVerticalAlignment(keyPanel, HasVerticalAlignment.ALIGN_MIDDLE);
		
		HorizontalPanel top = new HorizontalPanel();
		keyPanel.add(top);
		
		Label key = new Label("Color key");
		key.setStyleName("largeLabel");
		top.add(key);
		top.setCellVerticalAlignment(key, HasVerticalAlignment.ALIGN_MIDDLE);
		
		CheckBox selectAll = new CheckBox("Select all");
		selectAll.setStyleName("selectAll");
		selectAll.setValue(true);
		selectAll.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				CheckBox cb = (CheckBox) event.getSource();
				selectAll(cb.getValue());
			}
		});
		top.add(selectAll);
		top.setCellVerticalAlignment(selectAll, HasVerticalAlignment.ALIGN_MIDDLE);
		
		HorizontalPanel bottom = new HorizontalPanel();
		keyPanel.add(bottom);
		
		keyChart = createKeyChart();
		bottom.add(keyChart);
	}
	
	public void updateSession(SessionID sid) {
		personService.getUserCategories(sid, new AsyncCallback<List<CategoryDTO>>() {
			public void onFailure(Throwable caught) {
				Window.alert("Failed to get user's categories");
			}

			public void onSuccess(List<CategoryDTO> result) {
				populateKeyChart(result);
			}
		});
	}
	
	private Grid createKeyChart() {
		Grid grid = new Grid(4, 8);
		grid.setStyleName("keyChart");
		
		grid.setText(1, 0, "Bright: < 1 day");
		grid.getCellFormatter().setStyleName(1, 0, "labelColumn");
		grid.setText(2, 0, "Medium: < 1 week");
		grid.getCellFormatter().setStyleName(2, 0, "labelColumn");
		grid.setText(3, 0, "Dark: > 1 week");
		grid.getCellFormatter().setStyleName(3, 0, "labelColumn");
		
		for (int column = 1; column < 8; column++)
			grid.getColumnFormatter().setStyleName(column, "categoryColumn");
		
		return grid;
	}
	
	private Widget createColumnSelector(final String title) {
		HorizontalPanel panel = new HorizontalPanel();
		
		CheckBox checkBox = new CheckBox();
		checkBox.setValue(true);
		checkBox.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				CheckBox cb = (CheckBox) event.getSource();
				selectColumn(title, cb.getValue());
			}
		});
		panel.add(checkBox);
		panel.setCellVerticalAlignment(checkBox, HasVerticalAlignment.ALIGN_BOTTOM);
		
		Label label = new Label(title);
		label.setStyleName("smallLabel");
		panel.add(label);
		panel.setCellVerticalAlignment(label, HasVerticalAlignment.ALIGN_MIDDLE);
		
		return panel;
	}
	
	private Widget createColorBand(String color) {
		AbsolutePanel colorBand = new AbsolutePanel();
		colorBand.getElement().setAttribute("style", "background-color: #" + color + ";");
		colorBand.addStyleName("colorBand");
		return colorBand;
	}
	
	private void populateKeyChart(List<CategoryDTO> categories) {
		int column = 1;
		
		for (CategoryDTO category: categories) {
			keyChart.setWidget(0, column, createColumnSelector(category.getTitle()));
			keyChart.getCellFormatter().setStyleName(0, column, "categorySelector");
			keyChart.setWidget(1, column, createColorBand(category.getBrightColor()));
			keyChart.setWidget(2, column, createColorBand(category.getMediumColor()));
			keyChart.setWidget(3, column, createColorBand(category.getDarkColor()));
			column++;
		}
	}
	
	private void selectAll(boolean selectThem) {
		Window.alert("Selecting all: " + selectThem);
	}
	
	private void selectColumn(String title, boolean selectIt) {
		// TODO: turn column related treemap items on or off
	}
	
}
