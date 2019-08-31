package br.edu.unoesc.pgp;

public class Teste3 {

	
	public static void main(String[] args) throws Exception {
		
		PGPFileProcessor pgp = new PGPFileProcessor();
		
//		pgp.encrypt();
		pgp.decrypt("/Users/TheylorMarmitt/Desktop/Seguranca/chavePrivada.asc",
				"/Users/TheylorMarmitt/Desktop/Seguranca/decriptato.txt",
				"/Users/TheylorMarmitt/Desktop/Seguranca/message.txt.gpg", "seguranca");
	}
}
