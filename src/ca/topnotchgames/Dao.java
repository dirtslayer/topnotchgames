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
			try {
				EntityManager em = EMFService.get().createEntityManager();
				em.getTransaction().begin();
				Query q = em.createQuery("select m from Item m");
				Itemss = (List<Item>) q.getResultList();
				em.getTransaction().commit();
				em.close();
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
			return Itemss;
		}
	}

	public void addItem(String category, String group, String imageurl,
			String name, String price, String quantity, String notes) {

		synchronized (this) {
			try {
				EntityManager em = EMFService.get().createEntityManager();
				em.getTransaction().begin();
				Item it = new Item(category, group, imageurl, name, price,
						quantity, notes);
				em.persist(it);
				em.getTransaction().commit();
				em.close();
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
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
				for (Item it:il) {
					Item init = getItem(it.getCategory(),it.getName());
					if (init == null) {
						EntityManager em = EMFService.get().createEntityManager();
						em.getTransaction().begin();
						
						em.persist(it);
						System.out.println("not in, persist " + it);
						em.flush();
						em.getTransaction().commit();
						
						em.close();
						
					} else {
						it.setImageurl(init.getImageurl());
						it.setNotes(init.getNotes());
						System.out.println("found copying " + init);
						
						EntityManager em = EMFService.get().createEntityManager();
						em.getTransaction().begin();
						
						em.persist(it);
						em.flush();
						em.getTransaction().commit();
					
						em.close();
						
						System.out.println("removeing ");
						
						removeItem(init.getId());						
					}
					
				}
				
				
			} catch (Exception e) {
				System.out.println(e.getMessage());
				e.printStackTrace(System.out);
			}
		}
	}
	

	public Item getItem(String category, String name) {

	//	String key = category + name;

	//	Item ser = null;

	//	MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
	//	syncCache.setErrorHandler(ErrorHandlers
	//			.getConsistentLogAndContinue(Level.INFO));
	//	ser = (Item) syncCache.get(key); // read from cache

	//	if (ser != null) {
	//		return ser;
	//	}

		synchronized (this) {
			Item it = null;
			try {
				EntityManager em = EMFService.get().createEntityManager();
				em.getTransaction().begin();
				Query q = em
						.createQuery("select i from Item i where i.category = :category and i.name = :name");
				q.setParameter("category", category);
				q.setParameter("name", name);
				
				
				@SuppressWarnings("unchecked")
				List<Item> results = ( List<Item> )q.getResultList();
				
				Item foundEntity = null;
				if(!results.isEmpty()){
				    // ignores multiple results
				    foundEntity = results.get(0);
				    System.out.println("found item :" + results.get(0));
				} else {
					System.out.println("item NOT found");
				}
				
				it = foundEntity;    //q.getResultList();
				em.getTransaction().commit();
				em.close();
			} catch (Exception e) {
				 System.out.println("getItem: " + e.getMessage());
			}
			
			if (it != null) {
		//		syncCache.put(key, it); // populate cache
			}
			return it;
		}
	}


	
	@SuppressWarnings("unchecked")
	public List<Item> getItembycategory(String category) {

		String key = "Itemsincategory" + category;

		LinkedList<Item> serList = null;

		MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
		syncCache.setErrorHandler(ErrorHandlers
				.getConsistentLogAndContinue(Level.INFO));
		serList = (LinkedList<Item>) syncCache.get(key); // read from cache

		if (serList != null) {
			return serList;
		}

		synchronized (this) {
			List<Item> Items = null;
			try {
				EntityManager em = EMFService.get().createEntityManager();
				em.getTransaction().begin();
				Query q = em
						.createQuery("select i from Item i where i.category = :category");
				q.setParameter("category", category);
				Items = q.getResultList();
				em.getTransaction().commit();
				em.close();
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}

			serList = new LinkedList<Item>();
			for (Item i : Items) {
				serList.add(i);
			}
			syncCache.put(key, serList); // populate cache

			return Items;
		}
	}

	public void removeItem(long id) {
		// invalidate("all");
		synchronized (this) {
			EntityManager em = null;
			try {
				em = EMFService.get().createEntityManager();
				em.getTransaction().begin();
				Item i = em.find(Item.class, id);
				em.remove(i);
				
				em.flush();
				em.getTransaction().commit();
				
				em.close();
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
	}

}
