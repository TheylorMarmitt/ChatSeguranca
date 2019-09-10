package br.edu.unoesc.chat.service;


import java.io.File;

import org.springframework.stereotype.Component;

@Component
public class CertificadoService {

	public boolean buscaCertificado() {
		String path = System.getProperty("user.home")+File.separator+"Documents"+File.separator+"certificado";
		File certificado = new File(path + File.separator + "private_key.asc");		 
		if(certificado.exists()) {
			
			return true;
		} else {
			certificado.mkdir();
			return false;
		}
	}
	

}
