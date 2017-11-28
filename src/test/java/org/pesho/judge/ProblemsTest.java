package org.pesho.judge;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.io.InputStream;
import java.util.Arrays;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.pesho.judge.daos.LanguageDao;
import org.pesho.judge.daos.ProblemDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(locations = "classpath:test.properties")
@TestConfiguration
public class ProblemsTest {

	@Autowired
	private WebApplicationContext context;
	
    @Value("${work.dir}")
    private String workDir;
	
	private MockMvc mvc;
	private ObjectMapper objectMapper = new ObjectMapper();

	@Before
	public void setUp() {
		this.mvc = MockMvcBuilders.webAppContextSetup(this.context).build();
	}
	
	@After
	public void clean() throws Exception {
		try {
			FileUtils.forceDelete(new File(workDir));
		} catch (Exception e) {
		}
	}

	@Test
	public void testCreateProblem() throws Exception {
		mvc.perform(get("/api/v1/problems")).andExpect(jsonPath("$", hasSize(0)));
		
		MockMultipartFile multipartMetadata = new MockMultipartFile("metadata", objectMapper.writeValueAsBytes(createProblem()));
		InputStream is = this.getClass().getClassLoader().getResourceAsStream("tests.zip");
		MockMultipartFile multipartFile = new MockMultipartFile("file", "tests.zip", "text/plain", is);
		this.mvc.perform(fileUpload("/api/v1/problems/1")
				.file(multipartMetadata)
				.file(multipartFile))
				.andExpect(status().isCreated());
		
		mvc.perform(get("/api/v1/problems"))
				.andExpect(jsonPath("$", hasSize(1)))
				.andExpect(jsonPath("$[0].problemname", is("a+b+c")));
	}
	
	private ProblemDao createProblem() {
		ProblemDao problem = new ProblemDao();
		problem.setProblemname("a+b+c");
		problem.setVersion("v1");
		problem.setText("namerete sbora na chislata a, b i c");
		problem.setTest("1 2 3\n#\n6\n");
		LanguageDao cpp = new LanguageDao("c++", 1000, 64);
		LanguageDao java = new LanguageDao("java", 1000, 64);
		problem.setLanguages(Arrays.asList(cpp, java));
		problem.setVisibility("public");
		problem.setPoints("100");
		problem.setTags("easy");
		return problem;
	}
	
}
