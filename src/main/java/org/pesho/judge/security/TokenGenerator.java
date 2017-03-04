package org.pesho.judge.security;

import java.util.Hashtable;

public class TokenGenerator {
	
	private static final int TOKENS_THRESHOLD = 1000;
	
	public static Hashtable<String, Integer> tokensToUser = new Hashtable<String, Integer>();
	public static Hashtable<Integer, Token> usersToTokens = new Hashtable<Integer, Token>();
	
	public static synchronized String createToken(int userId) {
		if (usersToTokens.size() > TOKENS_THRESHOLD) {
			deleteExpiredTokens();
		}
		Token token = new Token();
		tokensToUser.put(token.getToken(), userId);
		usersToTokens.put(userId, token);
		return token.getToken();
	}
	
	public static synchronized void deleteToken(String token) {
		int userId = tokensToUser.remove(token);
		usersToTokens.remove(userId);
	}

	
	public static synchronized Integer getUser(String userToken) {
		Integer userId = tokensToUser.get(userToken);
		if (userId == null) {
			return null;
		}
		Token token = usersToTokens.get(userId);
		token.extendExpirationDate(5);
		return userId;
	}
	
	private static void deleteExpiredTokens() {
		for (int key: usersToTokens.keySet()) {
			if (usersToTokens.get(key).hasExpired()) {
				usersToTokens.remove(key);
			}
		}
	}

}
