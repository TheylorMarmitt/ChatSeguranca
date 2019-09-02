package br.edu.unoesc.service;

import java.io.File;

public class CertificadoService {

	public boolean buscaCertificado() {
		String path = System.getProperty("user.home")+File.separator+"Documents"+File.separator+"certificado";
		File certificado = new File(path);
		if(certificado.exists()) {
			
			return true;
		} else {
			certificado.mkdir();
			return false;
		}
	}
	

}
