package org.pesho.judge;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.pesho.judge.model.Role;
import org.pesho.judge.model.User;

@ApplicationScoped
public class TestDataCreator {

	@PersistenceContext
	private EntityManager em;

	@Transactional
	void createTestUser(@Observes @Initialized(ApplicationScoped.class) final Object event) {
		em.persist(Role.ADMIN);
		em.persist(Role.TEACHER);
		em.persist(Role.USER);
		
		em.persist(createAdminUser());
	}

	private User createAdminUser() {
		User admin = new User();
		admin.setUsername("admin");
		admin.setFirstname("online");
		admin.setLastname("judge");
		admin.setEmail("judge@pesho.org");
		admin.setPasswordHash("admin");
		admin.setRoles(Role.ADMIN);
		return admin;
	}
	
}