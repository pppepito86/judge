package org.pesho.judge;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.pesho.judge.model.Role;

@ApplicationScoped
public class TestDataCreator {

	@PersistenceContext
	private EntityManager em;

	@Transactional
	void createTestUser(@Observes @Initialized(ApplicationScoped.class) final Object event) {
		em.persist(Role.ADMIN);
		em.persist(Role.TEACHER);
		em.persist(Role.USER);
	}
	
}