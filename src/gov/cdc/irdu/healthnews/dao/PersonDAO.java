/**
 * 
 */
package gov.cdc.irdu.healthnews.dao;

import gov.cdc.irdu.healthnews.domain.Person;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Joel M. Rives
 * Apr 26, 2011
 */

@Repository("personDAO")
public class PersonDAO extends JpaDAO<Long, Person> {

	@Autowired
	EntityManagerFactory entityManagerFactory;
	
	@PostConstruct
	public void init() {
		super.setEntityManagerFactory(entityManagerFactory);
	}

	public Person findByUsername(String name) {
		@SuppressWarnings("unchecked")
		List<Person> results = getJpaTemplate().find("select p from Person p where p.username = ?1", name);		
		
		if (results.size() == 0)
			return null;
		
		return results.get(0);
	}
	

}
