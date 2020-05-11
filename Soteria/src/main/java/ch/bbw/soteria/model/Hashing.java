package ch.bbw.soteria.model;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hashing {
	public static String hashString(String text) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(text.getBytes());
			return byteArrayToString(md.digest());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return null;
	}

	private static String byteArrayToString(byte[] bytes) {
		String st = "";
		for (byte b : bytes) {
            st += String.format("%02X", b);
        }
		return st;
	}
}
