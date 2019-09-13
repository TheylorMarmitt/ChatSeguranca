package br.edu.unoesc.chat.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class ArquivoController {

	@PostMapping(value = "/arquivo")
	@ResponseStatus(value = HttpStatus.OK)
	public String salvar(@RequestParam("filePriv") MultipartFile filePriv, 
			@RequestParam("filePub") MultipartFile filePub, String frase) throws IOException {

		String path = System.getProperty("user.home") + File.separator + "Documents" + File.separator + "certificado";
		
		// arquivo private
		Path caminhoPrivada = Paths.get(path+"/private_key.asc");
		if (!Files.exists(caminhoPrivada)) {
			Files.write(caminhoPrivada, filePriv.getBytes(), StandardOpenOption.CREATE_NEW);
		}
		
		// arquivo private		
		Path caminhoPublica = Paths.get(path+"/public_key.asc");
		if (!Files.exists(caminhoPublica)) {
			Files.write(caminhoPublica, filePub.getBytes(), StandardOpenOption.CREATE_NEW);
		}
		// frase
		Path caminho = Paths.get(path+"/frase_seguranca.txt");
		if (!Files.exists(caminho)) {
			Files.write(caminho, frase.getBytes(), StandardOpenOption.CREATE_NEW);
		}
		
		return "index";
		
	}
}
