package com.baeldung.pgp;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class Simetrico {
	private static Cipher cipher = null;

	public String encrypt(String texto, String chave) throws Exception {
		// converte texto em bytes
		byte[] plainTextByte = texto.getBytes("UTF8");
		//cria secretKey com chave
		KeyGenerator keyGenerator = KeyGenerator.getInstance(chave);
		keyGenerator.init(168);
		SecretKey secretKey = keyGenerator.generateKey();
		// encripta
		cipher.init(Cipher.ENCRYPT_MODE, secretKey);
		byte[] encryptedBytes = cipher.doFinal(plainTextByte);
		return new String(encryptedBytes);
	}

	public String decrypt(String texto, String chave) throws Exception {
		
		byte[] plainTextByte = texto.getBytes("UTF8");
		
		KeyGenerator keyGenerator = KeyGenerator.getInstance(chave);
		keyGenerator.init(168);
		SecretKey secretKey = keyGenerator.generateKey();
		// decripta
		cipher.init(Cipher.DECRYPT_MODE, secretKey);
		byte[] decryptedBytes = cipher.doFinal(plainTextByte);
		return new String(decryptedBytes, "UTF8");
	}
	
	/**
	 * USAGE 
	 * 

		KeyGenerator keyGenerator = KeyGenerator.getInstance("DESede");
		keyGenerator.init(168);
		SecretKey secretKey = keyGenerator.generateKey();
		cipher = Cipher.getInstance("DESede");

		String plainText = "Java Cryptography Extension";
		/////System.out.println("Plain Text Before Encryption: " + plainText);

		byte[] plainTextByte = plainText.getBytes("UTF8");
		byte[] encryptedBytes = encrypt(plainTextByte, secretKey);

		String encryptedText = new String(encryptedBytes, "UTF8");
		//////System.out.println("Encrypted Text After Encryption: " + encryptedText);

		byte[] decryptedBytes = decrypt(encryptedBytes, secretKey);
		String decryptedText = new String(decryptedBytes, "UTF8");
		
		/////System.out.println("Decrypted Text After Decryption: " + decryptedText);
	 * 
	 * 
	 */
}