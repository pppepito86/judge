package org.pesho.judge;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import org.pesho.judge.dto.UserDTO;
import org.pesho.judge.model.Role;
import org.pesho.judge.model.User;

public class CreateUserResourceTest extends CreateResourceTest<User, UserDTO> {

	@Override
	protected String getRousurceURI() {
		return "users";
	}
	
	@Override
	protected User createResource() {
		User user = new User();
		user.setUsername("testuser");
		user.setPasswordHash("password");
		user.setEmail("user@email.com");
		user.setFirstname("firstname");
		user.setLastname("lastname");
		user.setRoles(Role.USER);
		return user;
	}
	
	@Override
	protected void assertCreatedResourceIsValid(UserDTO addedUser) {
		assertThat(addedUser.getEmail(), is("user@email.com"));
		assertThat(addedUser.getFirstname(), is("firstname"));
	}

	@Override
	protected Class<User> getDtoClassForPost() {
		return User.class;
	}

	@Override
	protected Class<UserDTO> getDtoClassForGet() {
		return UserDTO.class;
	}
}
