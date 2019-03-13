package br.com.caronasegura.security;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CustomPasswordEncoder {

	
	public String encode(String rawPassword) {
		StringBuilder hexString = new StringBuilder();
		try {
			MessageDigest algorithm = MessageDigest.getInstance("SHA-256");
			byte messageDigest[] = algorithm.digest(rawPassword.toString().getBytes("UTF-8"));
			for (byte b : messageDigest) {
				hexString.append(String.format("%02X", 0xFF & b));
			}
		} catch (NoSuchAlgorithmException ex) {
			System.out.println("Erro criptografar senha "+ ex.toString());
		} catch (UnsupportedEncodingException ex) {
			System.out.println("Erro criptografar senha "+ ex.toString());
		}
		return hexString.toString();
	}
}
