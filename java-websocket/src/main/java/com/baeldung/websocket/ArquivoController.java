package com.baeldung.websocket;

import java.io.File;
import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class ArquivoController {

	@RequestMapping(value = "/conectar", method = RequestMethod.POST)
	public void salvar(@RequestParam("file") MultipartFile file){
		
		/**
		*	NAO FUNCIONA (REVER)
		*/
		
		String path = System.getProperty("user.home")+File.separator+"Documents"+File.separator+"certificado";
		File novoArquivo = new File(path + File.separator + file.getName());
		
        boolean criado;
		try {
			criado = novoArquivo.createNewFile();
			System.out.println("arquivo criado =========" + criado);
		} catch (IOException e) {
			System.out.println("erro ========"+ e);
		}
	    
	}
}
