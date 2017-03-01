package org.pesho.judge.ejb;

import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

import org.pesho.judge.model.User;

@Stateful
public class UserManager {

	@PersistenceContext(unitName = "judge", type=PersistenceContextType.EXTENDED)
	EntityManager em;

	public User createCustomer(String name, String address) {
		System.out.println("create customer");
		System.out.println(em);
		User customer = new User();
		customer.setUsername("username");
		customer.setPasswordHash("hash");
		customer.setFirstname("firstname");
		customer.setLastname("lastname");
		customer.setEmail("dfadf@fsd");
		
		em.persist(customer);
		return customer;
	}
}