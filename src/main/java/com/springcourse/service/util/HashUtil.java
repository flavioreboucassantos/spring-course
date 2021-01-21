package com.springcourse.service.util;

import org.apache.commons.codec.digest.DigestUtils;

public class HashUtil {

	public static String getSecureHash(String text) {
		// SHA = Secure Hash Algorithm
		String hash = DigestUtils.sha256Hex(text);
		return hash;
	}
}
