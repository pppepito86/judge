package org.pesho.judge.checker;

import java.io.BufferedReader;
import java.io.FileReader;

public class ExactChecker {

	public static void main(String[] args) throws Exception {
		System.exit(check(args));
	}

	public static int check(String[] args) throws Exception {
		try (BufferedReader testOutput = new BufferedReader(new FileReader(args[1]));
				BufferedReader userOutput = new BufferedReader(new FileReader(args[2]))) {
			for (String testLine = testOutput.readLine(); testLine != null; testLine = testOutput.readLine()) {
				String userLine = userOutput.readLine();
				if (!testLine.equals(userLine)) {
					return 1;
				}
			}
			if (userOutput.readLine() != null) {
				return 1;
			}
		}
		return 0;
	}

}
