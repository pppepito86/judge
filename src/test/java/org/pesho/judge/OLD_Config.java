package org.pesho.judge;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class OLD_Config {
	
	@Bean
	public UserService userService() {
		return new UserService() {
			@Override
			public Integer getCurrentUserId() {
				return 1;
			}
		};
	}
	
}
