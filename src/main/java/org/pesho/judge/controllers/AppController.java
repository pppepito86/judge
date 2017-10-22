package org.pesho.judge.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AppController {
	
	@GetMapping("/app/**")
	public String createAssignment() {
		return "redirect:/index.html";
	}
	
}
