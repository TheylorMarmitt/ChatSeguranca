package br.edu.unoesc.chat.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import br.edu.unoesc.chat.model.ChatMessage;

@Controller
public class PrivateController {

	@MessageMapping("/chat.sendMessagePrivado")
	@SendTo("/topic/private")
	public ChatMessage sendMessagePrivado(@Payload br.edu.unoesc.chat.model.ChatMessage chatMessage) {
		return chatMessage;
	}
	
	//  PUBLIC KEY
	@RequestMapping(path = "/buscarPubKey")
	public @ResponseBody String publicKey() throws IOException {
		String path = System.getProperty("user.home")+File.separator+"Documents"+File.separator+"certificado";
		File certificado = new File(path + File.separator + "public_key.asc");
		String dados = new String(Files.readAllBytes(certificado.toPath()));
		return dados;
	}
	
	//  PRIVATE KEY
	@RequestMapping(path = "/buscarPrivKey")
	public @ResponseBody String privateKey() throws IOException {
		String path = System.getProperty("user.home")+File.separator+"Documents"+File.separator+"certificado";
		File certificado = new File(path + File.separator + "private_key.asc");
		String dados = new String(Files.readAllBytes(certificado.toPath()));
		return dados;
	}
	
//  PRIVATE KEY
	@RequestMapping(path = "/fraseSeguranca")
	public @ResponseBody String frase() throws IOException {
		String path = System.getProperty("user.home")+File.separator+"Documents"+File.separator+"certificado";
		File frase = new File(path + File.separator + "frase_seguranca.txt");
		String dados = new String(Files.readAllBytes(frase.toPath()));
		return dados;
	}
	
	@RequestMapping(path = "/userPrivado")
	public @ResponseBody Integer quantidade() {
		return this.count;
	}
	
	private int count = 0;

	@MessageMapping("/chat.addUserPrivado")
	@SendTo("/topic/private")
	public ChatMessage addUserPrivado(@Payload ChatMessage chatMessage,  SimpMessageHeaderAccessor headerAccessor) {
		headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
		return chatMessage;
	}

}
