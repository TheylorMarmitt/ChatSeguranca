package br.edu.unoesc.chat.pgp;

import java.security.Security;
import java.security.spec.KeySpec;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class Simetrico {

//	private static String secretKey = "12345678";
//	private static String salt = "12345678";
	
	public String encrypt(String strToEncrypt, String secret) {
		Security.setProperty("crypto.policy", "unlimited");
		try {
			byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
			IvParameterSpec ivspec = new IvParameterSpec(iv);

			SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
			KeySpec spec = new PBEKeySpec(secret.toCharArray(), secret.getBytes(), 65536, 256);
			SecretKey tmp = factory.generateSecret(spec);
			SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");

			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivspec);
			return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
		} catch (Exception e) {
			System.out.println("Error while encrypting: " + e.toString());
		}
		return null;
	}
	
	public String decrypt(String strToDecrypt, String secret) {
		Security.setProperty("crypto.policy", "unlimited");
	    try
	    {
	        byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
	        IvParameterSpec ivspec = new IvParameterSpec(iv);
	         
	        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
	        KeySpec spec = new PBEKeySpec(secret.toCharArray(), secret.getBytes(), 65536, 256);
	        SecretKey tmp = factory.generateSecret(spec);
	        SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");
	         
	        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
	        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivspec);
	        return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
	    }
	    catch (Exception e) {
	        System.out.println("Error while decrypting: " + e.toString());
	    }
	    return null;
	}
}
	
//	public String encrypt(String texto) throws Exception {
//		Security.setProperty("crypto.policy", "unlimited");
//		Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
//		byte[] mensagem = texto.getBytes();
//
//		// Usando chave de 128-bits (16 bytes)
//		byte[] chave = "novachaveaulaseg".getBytes();
//		System.out.println("Tamanho da chave: " + chave.length);
//
//		// Encriptando...
//		cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(chave, "AES"));
//		byte[] encrypted = cipher.doFinal(mensagem);
//		return new String(encrypted);
//	}
//
//	public String decrypt(String texto) throws Exception {
//		Security.setProperty("crypto.policy", "unlimited");
//		Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
//
//		byte[] encrypted = texto.getBytes();
//		byte[] chave = "novachaveaulaseg".getBytes();
//
//		// Decriptando...
//		cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(chave, "AES"));
//		byte[] decrypted = cipher.doFinal(encrypted);
//
//		return new String(decrypted);
//	}
