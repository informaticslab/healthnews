/**
 * 
 */
package gov.cdc.irdu.healthnews.dao;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.springframework.orm.jpa.JpaCallback;
import org.springframework.orm.jpa.support.JpaDaoSupport;

/**
 * 
 * @author Joel M. Rives
 * Mar 21, 2011
 */

public abstract class JpaDAO<K, E> extends JpaDaoSupport {
	protected Class<E> entityClass;
	
	@SuppressWarnings("unchecked")
	public JpaDAO() {
		ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
		this.entityClass = (Class<E>) genericSuperclass.getActualTypeArguments()[1];
	}
	
	public List<E> findAll() {
		List<E> results = getJpaTemplate().execute(new JpaCallback<List<E>>() {
			@SuppressWarnings("unchecked")
			public List<E> doInJpa(EntityManager em) throws PersistenceException {
				Query q = em.createQuery("SELECT h FROM " + entityClass.getName() + " h");
				return (List<E>) q.getResultList();
			}			
		});
		
		return results;
	}
	
	public E findById(K id) {
		return getJpaTemplate().find(entityClass, id);
	}

	public E flush(E entity) {
		getJpaTemplate().flush();
		return entity;
	}
	
	public E merge(E entity) {
		return getJpaTemplate().merge(entity);
	}
	
	public void persist(E entity) {
		getJpaTemplate().persist(entity);
	}
	
	public void refresh(E entity) {
		getJpaTemplate().refresh(entity);
	}
	
	public void remove(E entity) {
		getJpaTemplate().remove(entity);
	}
	
}
