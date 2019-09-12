//package br.edu.unoesc.gnupg;
//
//import java.io.File;
//
//public class TestTextMain {
//	
//	public static void main(String[] args) {
//		PGPFactory pgpFactory = PGPFactory.getInstance();
//		
//		String path = System.getProperty("user.home")+File.separator+"Documents"+File.separator+"certificado";
//		
//		pgpFactory.loadKeys(path + File.separator + "public_key.asc",
//				path + File.separator + "private_key.asc",
//				"theylor.marmitt@unoesc.edu.br");
//		
//		String original = "Sridhar Jena";
//		System.out.println("PLAIN DATA: " + original);
//		
//		String encryptedData = pgpFactory.encrypt(original);
//		System.out.println("Encrypted Data: " + encryptedData);
//		
//		String decrypted = pgpFactory.decrypt(encryptedData);
//		System.out.println("Decrypted Data :" + new String(decrypted));
//


//	}
//}