package com.baeldung.websocket;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;


@Controller
public class ArquivoController {

	@RequestMapping(value = "/conectar", method = RequestMethod.POST)
	public void salvar(@RequestParam("file") MultipartFile file) throws IOException{
		
		/**
		*	ARQUIVO NÃO ESTÁ CHEGANDO (REVER)
		*/
		
		String path = System.getProperty("user.home") + File.separator + "Documents" + File.separator
				+ "certificado";
		File novoArquivo = new File(path + File.separator + file.getName());
		boolean criado;

		byte[] bs = file.getBytes();
		try {
			criado = novoArquivo.createNewFile();
			FileUtils.writeByteArrayToFile(novoArquivo, bs);
			System.out.println("arquivo criado =========" + criado);
		} catch (IOException erro) {
			System.out.println("erro ========" + erro);
		}
	    
	}
}
