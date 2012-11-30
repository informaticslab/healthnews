package gov.cdc.irdu.healthnews.client;

import gov.cdc.irdu.healthnews.shared.SearchDTO;
import gov.cdc.irdu.healthnews.shared.SessionID;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("services/googleService")
public interface GoogleService extends RemoteService {

	public List<SearchDTO> search(SessionID sid);
	
}
