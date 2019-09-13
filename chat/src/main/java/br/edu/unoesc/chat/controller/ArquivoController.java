package br.edu.unoesc.chat.controller;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

public class ArquivoController {

	@PostMapping(value = "/arquivo")
	@ResponseStatus(value = HttpStatus.OK)
	public void salvar(@RequestParam("file") MultipartFile filePriv, MultipartFile filePub, String frase) throws IOException {

		String path = System.getProperty("user.home") + File.separator + "Documents" + File.separator + "certificado";
		
		// arquivo private
		File novoArquivo = new File(path + File.separator + "private_key.asc");

		boolean criado;

		byte[] bs = filePriv.getBytes();
		try {
			criado = novoArquivo.createNewFile();
			FileUtils.writeByteArrayToFile(novoArquivo, bs);
			System.out.println("arquivo criado ========= " + criado);
		} catch (IOException erro) {
			System.out.println("erro ======== " + erro);
		}
		
		
		// arquivo public
		File pubKey = new File(path + File.separator + "public_key.asc");

		boolean criadoPub;
		byte[] bsPub = filePub.getBytes();
		try {
			criadoPub = pubKey.createNewFile();
			FileUtils.writeByteArrayToFile(pubKey, bsPub);
			System.out.println("arquivo criado ========= " + criadoPub);
		} catch (IOException erro) {
			System.out.println("erro ======== " + erro);
		}
		
		// frase
		File fileFrase = new File(path + File.separator + "frase_seguranca.txt");

		boolean criar;
		byte[] bite = frase.getBytes();
		try {
			criar = fileFrase.createNewFile();
			FileUtils.writeByteArrayToFile(fileFrase, bite);
			System.out.println("arquivo criado ========= " + criar);
		} catch (IOException erro) {
			System.out.println("erro ======== " + erro);
		}

	}
}
