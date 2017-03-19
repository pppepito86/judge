package org.pesho.judge.config;

import java.io.File;

import static org.junit.Assert.*;

import org.junit.Test;
import org.pesho.judge.ClientTestBase;

public class ConfigTest extends ClientTestBase {
	
	@Test
	public void testWorkDir() {
		String workDir = Configuration.getInstance().getWorkDir();
		assertTrue(workDir.endsWith("work_dir"));
		assertTrue(new File(workDir).exists());
	}

}
