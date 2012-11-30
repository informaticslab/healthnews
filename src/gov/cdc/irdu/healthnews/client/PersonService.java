package gov.cdc.irdu.healthnews.client;

import gov.cdc.irdu.healthnews.shared.CategoryDTO;
import gov.cdc.irdu.healthnews.shared.PersonDTO;
import gov.cdc.irdu.healthnews.shared.SessionID;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("services/personService")
public interface PersonService extends RemoteService {

	PersonDTO createUser(String username, String password, String givenName, String familyName);
	
	PersonDTO getCurrentUser(SessionID sid);
	
	List<CategoryDTO> getDefaultCategories();

	List<CategoryDTO> getUserCategories(SessionID sid);
	
}
