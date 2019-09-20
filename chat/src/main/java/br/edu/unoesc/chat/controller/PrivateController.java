package br.edu.unoesc.chat.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import br.edu.unoesc.chat.model.ChatMessage;
import br.edu.unoesc.chat.service.CertificadoService;

@Controller
public class PrivateController {

	@Autowired
	private CertificadoService certificadoService;
	
	@PostMapping("/privado")
	public String privado(@RequestParam(value = "nome") String nome, @RequestParam("filePriv") MultipartFile filePriv, 
			@RequestParam("filePub") MultipartFile filePub, String frase,  Model model) throws IOException {
		
		if(frase.isEmpty() || filePub.isEmpty() || filePriv.isEmpty()) {
			return "index";
		}
		
		if (!certificadoService.buscaCertificado()) {
			return "chat/conectar";
		}
		
		String privada = new String(filePriv.getBytes());
		String publica = new String(filePub.getBytes());

		model.addAttribute("nome", nome);
		model.addAttribute("publica", publica);
		model.addAttribute("privada", privada);
		model.addAttribute("frase", frase);
		
		return "chat/privado";
	}

	
	@MessageMapping("/chat.sendMessagePrivado")
	@SendTo("/topic/private")
	public ChatMessage sendMessagePrivado(@Payload br.edu.unoesc.chat.model.ChatMessage chatMessage) {
		return chatMessage;
	}
	
//	//  PUBLIC KEY
//	@RequestMapping(path = "/buscarPubKey")
//	public @ResponseBody String publicKey() throws IOException {
//		String path = System.getProperty("user.home")+File.separator+"Documents"+File.separator+"certificado";
//		File certificado = new File(path + File.separator + "public_key.asc");
//		String dados = new String(Files.readAllBytes(certificado.toPath()));
//		return dados;
//	}
//	
//	//  PRIVATE KEY
//	@RequestMapping(path = "/buscarPrivKey")
//	public @ResponseBody String privateKey() throws IOException {
//		String path = System.getProperty("user.home")+File.separator+"Documents"+File.separator+"certificado";
//		File certificado = new File(path + File.separator + "private_key.asc");
//		String dados = new String(Files.readAllBytes(certificado.toPath()));
//		return dados;
//	}
//	
////  frase
//	@RequestMapping(path = "/fraseSeguranca")
//	public @ResponseBody String frase() throws IOException {
//		String path = System.getProperty("user.home")+File.separator+"Documents"+File.separator+"certificado";
//		File frase = new File(path + File.separator + "frase_seguranca.txt");
//		String dados = new String(Files.readAllBytes(frase.toPath()));
//		return dados;
//	}
	
	// quantidade de usuarios no privado
	@RequestMapping(path = "/userPrivado")
	public @ResponseBody Integer quantidade() {
		return this.count;
	}
	
	
	/** CASO PRECISE TROCAR DE CHAVE
	 *	chama json em js 
	 *
	 *	compara com a chave e fica com a diferente.
	 * @throws IOException 
	 */
//	@RequestMapping(path = "/chaveUser")
//	public @ResponseBody String user() throws IOException {
//		String path = System.getProperty("user.home")+File.separator+"Documents"+File.separator+"certificado";
//		File certificado = new File(path + File.separator + "private_key.asc");
//		String dados = new String(Files.readAllBytes(certificado.toPath()));
//		if(dados.contentEquals(chaveUser1)) {
//			return this.chaveUser2;
//		}
//		return this.chaveUser1;
//			
//	}
	
	@RequestMapping(path = "/chave1")
	public @ResponseBody String user1(){
		return this.chaveUser1;
	}
	
	@RequestMapping(path = "/chave2")
	public @ResponseBody String user2(){
		return this.chaveUser2;
	}
	
	// users para salvar chaves 
	private String chaveUser1 = "";
	private String chaveUser2 = "";
	// contador de usuarios
	private int count = 0;

	@MessageMapping("/chat.addUserPrivado")
	@SendTo("/topic/private")
	public ChatMessage addUserPrivado(@Payload ChatMessage chatMessage,  SimpMessageHeaderAccessor headerAccessor) {
		headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
		if(count == 0) {
			chaveUser1 = chatMessage.getChavePublica();
		}else {
			chaveUser2 = chatMessage.getChavePublica();
		}
		this.count++;
		return chatMessage;
	}

}
