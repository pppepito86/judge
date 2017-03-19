package org.pesho.judge.problem;

import org.junit.Test;
import org.pesho.judge.ClientTestBase;
import org.pesho.judge.dto.TokenDTO;

public class CreateProblemTest extends ClientTestBase {
	
	
	private TokenDTO adminToken;

	@Test
	public void test() {
		adminToken = createAdminToken();
		
	}
	

}
