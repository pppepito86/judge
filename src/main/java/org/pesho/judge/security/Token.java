package org.pesho.judge.security;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class Token {

	private static final int DEFAULT_TIMEOUT = 3 * 60;

	private String token;
	private Date expirationDate;

	public Token() {
		this(UUID.randomUUID().toString(), DEFAULT_TIMEOUT);
	}

	public Token(String token, int timeoutInMinutes) {
		this.token = token;
		this.expirationDate = createNewDate(timeoutInMinutes);
	}

	public void extendExpirationDate(int minutesFromNow) {
		Date newDate = createNewDate(minutesFromNow);
		if (newDate.after(expirationDate)) {
			this.expirationDate = newDate;
		}
	}

	public String getToken() {
		return token;
	}

	public boolean hasExpired() {
		return expirationDate.before(new Date());
	}

	public void invalidate() {
		this.expirationDate = createNewDate(-10);
	}

	private Date createNewDate(int minutesFromNow) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.MINUTE, minutesFromNow);
		return cal.getTime();
	}

}
