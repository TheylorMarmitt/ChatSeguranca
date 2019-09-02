package com.baeldung.pgp;


public class GnuPGActions {

	
	public String assinarEncriptar(String text, String keyID, String passPhrase) {
		boolean		result;
		
		GnuPG pgp = new GnuPG ();
		result = pgp.signAndEncrypt(text, keyID, passPhrase);
		
		if (result){
			return pgp.getResult();
		}
		else{
			return pgp.getErrorString();
		}
	}
	
	public String decriptar(String text, String keyID) {
//		String		keyID = '8AC1';
		boolean		result;

		GnuPG pgp = new GnuPG ();
		result = pgp.encrypt (text, keyID);
		
		if (result){
			return pgp.getResult();
		}
		else{
			return pgp.getErrorString();
		}
	}
	
	public String assinar(String text, String passPhrase) {
		boolean		result;

		GnuPG pgp = new GnuPG ();
		result = pgp.sign (text, passPhrase);
		
		if (result){
			return pgp.getResult();
		}
		else{
			return pgp.getErrorString();
		}
	}	
	
}
