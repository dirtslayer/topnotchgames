package ca.topnotchgames;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.google.appengine.api.memcache.ErrorHandlers;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;


public enum Dao {
	INSTANCE;

	@SuppressWarnings("unchecked")
	public List<Item> listItems() {
		synchronized (this) {
			List<Item> Itemss = null;
			
			
			String key = "listitems";

			LinkedList<Item> serlist = null;

			MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
			syncCache.setErrorHandler(ErrorHandlers
						.getConsistentLogAndContinue(Level.INFO));
			serlist = (LinkedList<Item>) syncCache.get(key); // read from cache

			if (serlist != null) {
					return serlist;
			}

			
			try {
				EntityManager em = EMFService.get().createEntityManager();
				em.getTransaction().begin();
				Query q = em.createQuery("select m from Item m");
				//q.setMaxResults(100);
				
				Itemss = (List<Item>) q.getResultList();
				em.getTransaction().commit();
				em.close();
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
			serlist = new LinkedList<Item>();
	        for (Item a:Itemss) {
	                serlist.add(a);
	        }
	        syncCache.put(key, serlist); // populate cache
			return Itemss;
		}
	}

	

	public void persistItem(Item it) {
		synchronized (this) {
			try {
				EntityManager em = EMFService.get().createEntityManager();
				em.getTransaction().begin();
				em.persist(it);
				em.getTransaction().commit();
				em.close();
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
	}
	

	
	public void persistList(List<Item> il) {
		synchronized (this) {
			try {
				EntityManager em = EMFService.get().createEntityManager();
				em.getTransaction().begin();
				Query q = em.createQuery("DELETE FROM Item i");
				q.executeUpdate();
				em.getTransaction().commit();
				em.close();
				
				int i = 0;
				int tsize = 500;
				boolean emopen = false;
				
				for (Item it : il) {
					if ( 0 == i % tsize) {
						em = EMFService.get().createEntityManager();
						em.getTransaction().begin();
						emopen = true;
					}
						if (emopen == false ) {
							em = EMFService.get().createEntityManager();
							em.getTransaction().begin();
							emopen = true;
						}
					 	em.persist(it);
					 	i++;
					if ( 0 == i % tsize) {
							em.getTransaction().commit();
							em.close();
							emopen = false;
					}
					 	
				}
				
				if (emopen == true) {
					em.getTransaction().commit();
					em.close();
					emopen = false;
				}
				
				
				

			} catch (Exception e) {
				System.out.println(e.getMessage());
				e.printStackTrace(System.out);
			}
		}
	}
}
	
	
	
	