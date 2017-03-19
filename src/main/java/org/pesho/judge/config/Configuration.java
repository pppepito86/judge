package org.pesho.judge.config;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Configuration {
	
	private static final Configuration INSTANCE = new Configuration();
	
	public static Configuration getInstance() {
		return INSTANCE;
	}
	
	private Properties props = new Properties();
	
	private Configuration() {
		init();
	}
	
	public String getWorkDir() {
		try {
			return new File(props.getProperty("work_dir")).getCanonicalPath();
		} catch (IOException e) {
			e.printStackTrace();
			return new File(props.getProperty("work_dir")).getAbsolutePath();
		}
	}

	private void init() {
		try(InputStream is = this.getClass().getClassLoader().getResourceAsStream("config.properties")) {
			props.load(is);
		} catch (Exception e) {
			throw new IllegalStateException("Configuration not found");
		}
		new File(props.getProperty("work_dir")).mkdirs();
	}
	
}
