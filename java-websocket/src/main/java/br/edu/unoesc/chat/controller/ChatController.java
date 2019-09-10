package br.edu.unoesc.chat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import br.edu.unoesc.chat.model.ChatMessage;
import br.edu.unoesc.chat.service.CertificadoService;

@Controller
public class ChatController {

	@Autowired
	private CertificadoService certificadoService;

	@GetMapping("/geral")
	public String geral(@RequestParam(value="nome") String nome, Model model) {
		
		certificadoService.buscaCertificado();
		 model.addAttribute("nome", nome);
		return "chat/geral";
	}

	@GetMapping("/privado")
	public String privado(@RequestParam(value="nome") String nome) {
		certificadoService.buscaCertificado();
		
		return "chat/privado";
	}
	
	
	 @MessageMapping("/chat.sendMessage")
	    @SendTo("/topic/public")
	    public ChatMessage sendMessage(@Payload br.edu.unoesc.chat.model.ChatMessage chatMessage) {
	        return chatMessage;
	    }

	    @MessageMapping("/chat.addUser")
	    @SendTo("/topic/public")
	    public ChatMessage addUser(@Payload ChatMessage chatMessage,
	                               SimpMessageHeaderAccessor headerAccessor) {
	        // adiciona o nome de usuário na sessão
	        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
	        return chatMessage;
	    }

}
