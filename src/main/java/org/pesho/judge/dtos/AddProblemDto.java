package org.pesho.judge.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AddProblemDto {
	
	public static class Language {
		@JsonProperty("Language")
		private String language;
		@JsonProperty("TimeLimit")
		private int timeLimit;
		@JsonProperty("MemoryLimit")
		private int memoryLimit;
		
		public Language() {
		}
		
		public Language(String name, int time, int memory) {
			this.language = name;
			this.timeLimit = time;
			this.memoryLimit = memory;
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
	
	public static class Languages {
	    @JsonProperty("c++")
	    private Language cpp;
	    private Language java;
	    
	    public Languages() {
	    }
	    
	    public Languages(Language cpp, Language java) {
	    	this.cpp = cpp;
	    	this.java = java;
	    }

	    public Language getCpp() {
			return cpp;
		}
	    
	    public void setCpp(Language cpp) {
			this.cpp = cpp;
		}
	    
	    public Language getJava() {
			return java;
		}
	    
	    public void setJava(Language java) {
			this.java = java;
		}
		
	}

    private String problemname;
    private String version;
    private String tags;
    private String text;
    private String test;
    private String visibility;
    private String points;
    private Languages languages;

    public String getProblemname() {
        return problemname;
    }

    public void setProblemname(String problemname) {
        this.problemname = problemname;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }
    
    public Languages getLanguages() {
		return languages;
	}
    
    public void setLanguages(Languages languages) {
		this.languages = languages;
	}
    
    public String getLanguagesJson() {
    	ObjectMapper objectMapper = new ObjectMapper();
    	try {
			return objectMapper.writeValueAsString(getLanguages());
		} catch (JsonProcessingException e) {
			throw new IllegalStateException(e);
		}
    }

}
