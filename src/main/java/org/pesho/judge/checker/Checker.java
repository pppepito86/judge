package org.pesho.judge.checker;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Checker {

	public static void main(String[] args) throws Exception {
		String[] command = {"/bin/bash", "-c", "/usr/local/bin/docker-machine help"};
		Process process = Runtime.getRuntime().exec(command);
		BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
		String line;
		while ((line = br.readLine()) != null) {
			System.out.println(line);
		}
		BufferedReader br2 = new BufferedReader(new InputStreamReader(process.getErrorStream()));
		String line2;
		while ((line2 = br2.readLine()) != null) {
			System.out.println(line2);
		}
		process.waitFor();
	}

}
