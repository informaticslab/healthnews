package gov.cdc.irdu.healthnews.client;

import gov.cdc.irdu.healthnews.shared.CategoryDTO;
import gov.cdc.irdu.healthnews.shared.PersonDTO;
import gov.cdc.irdu.healthnews.shared.SessionID;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface PersonServiceAsync {
	
	void createUser(String username, String password, String givenName, String familyName, AsyncCallback<PersonDTO> callback);
	
	void getCurrentUser(SessionID sid, AsyncCallback<PersonDTO> callback);
	
	void getDefaultCategories(AsyncCallback<List<CategoryDTO>> callback);
	
	void getUserCategories(SessionID sid, AsyncCallback<List<CategoryDTO>> callback);

}
