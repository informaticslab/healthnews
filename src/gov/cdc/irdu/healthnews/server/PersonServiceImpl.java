package gov.cdc.irdu.healthnews.server;

import gov.cdc.irdu.healthnews.client.PersonService;
import gov.cdc.irdu.healthnews.dao.PersonDAO;
import gov.cdc.irdu.healthnews.domain.Category;
import gov.cdc.irdu.healthnews.domain.Person;
import gov.cdc.irdu.healthnews.shared.CategoryDTO;
import gov.cdc.irdu.healthnews.shared.PersonDTO;
import gov.cdc.irdu.healthnews.shared.SessionID;
import gov.cdc.irdu.util.BCrypt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service("personService")
public class PersonServiceImpl implements PersonService {
    
    private static final String REALM = "edemo.phiresearchlab.org";
	
	@Autowired private PersonDAO personDAO;
	
	private Map<SessionID, Person> sessionMap = new HashMap<SessionID, Person>();
	
	private List<Category> defaultCategories;
	
	public PersonServiceImpl() {
		defaultCategories = new ArrayList<Category>();
		defaultCategories.add(createCategory("CDC", 0, "008185", "06A7AE", "11C4C9", "CDC health"));
		defaultCategories.add(createCategory("Chronic Disease", 1, "870965", "C20493", "E156BF", "chronic disease"));
		defaultCategories.add(createCategory("Environmental Health", 2, "007541", "059757", "26B876", "environmental health"));
		defaultCategories.add(createCategory("Health Statistics", 3, "AA2544", "CA3356", "E8486D", "health statistics"));
		defaultCategories.add(createCategory("Infection Disease", 4, "B6730A", "D58A16", "FAA624", "infectious disease"));
		defaultCategories.add(createCategory("Injury Surveillance", 5, "626364", "7E7F80", "919293", "injury surveillance"));
		defaultCategories.add(createCategory("Birth Defects / Developmental Disabilities", 6, "1C3687", "294BB6", "3964EA", "Birth defects, developmental disabilities"));
	}
	
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public PersonDTO createUser(String username, String password, String givenName, String familyName) {
        Person person = personDAO.findByUsername(username);
        
        if (null != person)
            throw new RuntimeException("Person " + username + " already exists.");
        
        password = BCrypt.hashpw(password, BCrypt.gensalt());

        person = new Person();
		person.setUsername(username);
		person.setPassword(password);
		person.setGivenName(givenName);
		person.setFamilyName(familyName);
		person.setCategories(defaultCategories);
		
		personDAO.persist(person);
		personDAO.flush(person);
		
		return person.toDTO();
	}
	
	public SessionID login(String username, String password) {
	    Person person = personDAO.findByUsername(username);
	    
	    if (!BCrypt.checkpw(password, person.getPassword()))
	        return null;
	    
	    UUID uuid = UUID.randomUUID();
	    SessionID sid = new SessionID(REALM, uuid.toString());
	    sessionMap.put(sid, person);
	    
	    return sid;
	}
	
	public PersonDTO getCurrentUser(SessionID sid) {
	    Person person = sessionMap.get(sid);
		return null == person ? null : person.toDTO();
	}
	
	public List<CategoryDTO> getDefaultCategories() {
		List<CategoryDTO> list = new ArrayList<CategoryDTO>();
		for (Category category: defaultCategories)
			list.add(category.toDTO());
		return list;
	}
	
	public List<CategoryDTO> getUserCategories(SessionID sid) {
		Person person = sessionMap.get(sid);
		
		// If the session id is no longer valid, lets fail gently
		List<Category> categories = null == person ? defaultCategories : person.getCategories();
		
		List<CategoryDTO> dtoList = new ArrayList<CategoryDTO>();
		
		for (Category category: categories)
			dtoList.add(category.toDTO());
		
		return dtoList;
	}
	
	private Category createCategory(String title, int ordering, String darkColor, String mediumColor, String brightColor, String queryTerms) {
		Category category = new Category();
		category.setTitle(title);
		category.setOrdering(ordering);
		category.setBrightColor(brightColor);
		category.setMediumColor(mediumColor);
		category.setDarkColor(darkColor);
		category.setQueryTerms(queryTerms);
		return category;
	}
	
}
