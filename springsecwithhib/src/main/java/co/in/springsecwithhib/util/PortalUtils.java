package co.in.springsecwithhib.util;

import java.util.UUID;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public final class PortalUtils {

	public static BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	public static String bcryptPasswordEncoder(final String plainText) {

		return passwordEncoder.encode(plainText);
	}

	public static String getRandomAlphaNumericString() {

		return UUID.randomUUID().toString().replaceAll("-", "");
	}

}
