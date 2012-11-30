package gov.cdc.irdu.healthnews.client;

import gov.cdc.irdu.healthnews.shared.SearchDTO;
import gov.cdc.irdu.healthnews.shared.SessionID;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface GoogleServiceAsync {
	
    void search(SessionID sid, AsyncCallback<List<SearchDTO>> callback);
    
}
