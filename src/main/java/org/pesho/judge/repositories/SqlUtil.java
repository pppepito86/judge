package org.pesho.judge.repositories;

public class SqlUtil {

	public static String limit(int page, int size) {
		return "limit "+ (page-1)*size + "," + size;
	}
	
}
