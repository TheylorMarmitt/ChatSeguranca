package br.edu.unoesc.chat.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import br.edu.unoesc.chat.model.ChatMessage;
import br.edu.unoesc.chat.pgp.Simetrico;
import br.edu.unoesc.chat.service.CertificadoService;

@Controller
public class ChatController {

	@Autowired
	private CertificadoService certificadoService;

	private Simetrico simetrico = new Simetrico();

	@GetMapping("/geral")
	public String geral(@RequestParam(value = "nome") String nome, Model model) {
		if (!certificadoService.buscaCertificado()) {
			return "chat/conectar";
		}

		model.addAttribute("nome", nome);
		return "chat/geral";
	}

	@GetMapping("/privado")
	public String privado(@RequestParam(value = "nome") String nome, Model model) {
		if (!certificadoService.buscaCertificado()) {
			return "chat/conectar";
		}

		model.addAttribute("nome", nome);
		return "chat/privado";
	}

	@GetMapping("/conectar")
	public String conectar() {
		return "chat/conectar";
	}

	

	/** -------------------- MENSAGENS -------------------- */
	//----------------------- PUBLIC ----------------------	

	@MessageMapping("/chat.sendMessage")
	@SendTo("/topic/public")
	public ChatMessage sendMessage(@Payload br.edu.unoesc.chat.model.ChatMessage chatMessage) {

		try {
			String encriptado = simetrico.encrypt(chatMessage.getContent(), "senha");
			chatMessage.setContent(encriptado);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return chatMessage;

	}

	@RequestMapping(path = "/decriptar")
	public @ResponseBody String decriptar(String mensagem) {
		String msgResult = "";
		try {
			msgResult = simetrico.decrypt(mensagem, "senha");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return msgResult;
	}
	

	@MessageMapping("/chat.addUser")
	@SendTo("/topic/public")
	public ChatMessage addUser(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {

		// adiciona o nome de usuário na sessão
		headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
		return chatMessage;
	}

	
}
