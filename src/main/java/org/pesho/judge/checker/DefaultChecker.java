package org.pesho.judge.checker;

import java.io.BufferedReader;
import java.io.FileReader;

public class DefaultChecker {

	public static void main(String[] args) throws Exception {
		System.exit(check(args));
	}

	public static int check(String[] args) throws Exception {
		try (BufferedReader testOutput = new BufferedReader(new FileReader(args[1]));
				BufferedReader userOutput = new BufferedReader(new FileReader(args[2]))) {
			for (String testLine = testOutput.readLine(); testLine != null; testLine = testOutput.readLine()) {
				testLine = testLine.trim();
				String userLine = userOutput.readLine();
				if (userLine == null) {
					if (testLine.length() == 0) {
						continue;
					} else {
						return 1;
					}
				}
				userLine = userLine.trim();
				if (!testLine.equals(userLine)) {
					return 1;
				}
			}
		}
		return 0;
	}

}
