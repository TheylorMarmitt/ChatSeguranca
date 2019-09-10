package br.edu.unoesc.chat.controller;

import java.io.File;
import java.io.IOException;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

import br.edu.unoesc.chat.model.ChatMessage;
import br.edu.unoesc.chat.service.CertificadoService;

@Controller
public class ChatController {

	@Autowired
	private CertificadoService certificadoService;

	@GetMapping("/geral")
	public String geral(@RequestParam(value="nome") String nome, Model model) {
		if(!certificadoService.buscaCertificado()) {
			return "chat/conectar";
		}
		
		model.addAttribute("nome", nome);
		return "chat/geral";
	}

	@GetMapping("/privado")
	public String privado(@RequestParam(value="nome") String nome, Model model) {
		if(!certificadoService.buscaCertificado()) {
			return "chat/conectar";
		}
		
		model.addAttribute("nome", nome);
		return "chat/privado";
	}
	
	
	@GetMapping("/conectar")
	public String conectar() {
		return "chat/conectar";
	}
	
	@PostMapping(value = "/arquivo")
	@ResponseStatus(value=HttpStatus.OK)
	public void salvar(@RequestParam("file") MultipartFile file) throws IOException{
		
		String path = System.getProperty("user.home") + File.separator + "Documents" + File.separator
				+ "certificado";
		File novoArquivo = new File(path + File.separator + file.getOriginalFilename());
		System.out.println(novoArquivo);
		boolean criado;

		byte[] bs = file.getBytes();
		try {
			criado = novoArquivo.createNewFile();
			FileUtils.writeByteArrayToFile(novoArquivo, bs);
			System.out.println("arquivo criado ========= " + criado);
		} catch (IOException erro) {
			System.out.println("erro ======== " + erro);
		}
	}    
	
	
/**		-------------------- MENSAGENS -------------------- */
	
//	------------------------- PUBLIC --------------------	
	
	@MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(@Payload br.edu.unoesc.chat.model.ChatMessage chatMessage) {
        return chatMessage;
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessage addUser(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        // adiciona o nome de usuário na sessão
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        return chatMessage;
    }
    
//	------------------------- PRIVATE --------------------
    
    @MessageMapping("/chat.sendMessagePrivado")
    @SendTo("/topic/private")
    public ChatMessage sendMessagePrivado(@Payload br.edu.unoesc.chat.model.ChatMessage chatMessage) {
        return chatMessage;
    }

    @MessageMapping("/chat.addUserPrivado")
    @SendTo("/topic/private")
    public ChatMessage addUserPrivado(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        // adiciona o nome de usuário na sessão
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        return chatMessage;
    }

}
