package com.baeldung.websocket;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class Arquivo {

	@PostMapping(path="/conectar")
	private void salvar(File arquivo) throws IOException {
		
		/**
		*	NAO FUNCIONA (REVER)
		*/
		
		String savePath = System.getProperty("user.home")+File.separator+"Documents"+File.separator+"certificado";
		File saveLocation = new File(savePath);
		if(!saveLocation.exists()){
			saveLocation.mkdir(); 
		}
		FileWriter fw = new FileWriter(arquivo);
		fw.close();
	    
	}
}
