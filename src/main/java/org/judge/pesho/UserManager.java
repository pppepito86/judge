package org.judge.pesho;

import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

@Stateful
public class UserManager {

	@PersistenceContext(unitName = "judge", type=PersistenceContextType.EXTENDED)
	EntityManager em;

	public User createCustomer(String name, String address) {
		System.out.println("create customer");
		System.out.println(em);
		User customer = new User("pesho");
		em.persist(customer);
		return customer;
	}
}