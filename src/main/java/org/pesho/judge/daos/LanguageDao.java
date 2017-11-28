package org.pesho.judge.daos;

public class LanguageDao {

	private String language;
	private int timeLimit; // ms
	private int memoryLimit; // MB

	public LanguageDao() {}
	
	public LanguageDao(String language, int timeLimit, int memoryLimit) {
		this.language = language;
		this.timeLimit = timeLimit;
		this.memoryLimit = memoryLimit;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public int getTimeLimit() {
		return timeLimit;
	}
	
	public void setTimeLimit(int timeLimit) {
		this.timeLimit = timeLimit;
	}

	public int getMemoryLimit() {
		return memoryLimit;
	}

	public void setMemoryLimit(int memoryLimit) {
		this.memoryLimit = memoryLimit;
	}

}
