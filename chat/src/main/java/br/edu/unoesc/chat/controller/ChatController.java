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

	@PostMapping(value = "/arquivo")
	@ResponseStatus(value = HttpStatus.OK)
	public void salvar(@RequestParam("file") MultipartFile file, String frase) throws IOException {

		String path = System.getProperty("user.home") + File.separator + "Documents" + File.separator + "certificado";
		File novoArquivo = new File(path + File.separator + file.getOriginalFilename());

		boolean criado;

		byte[] bs = file.getBytes();
		try {
			criado = novoArquivo.createNewFile();
			FileUtils.writeByteArrayToFile(novoArquivo, bs);
			System.out.println("arquivo criado ========= " + criado);
		} catch (IOException erro) {
			System.out.println("erro ======== " + erro);
		}

		File fileFrase = new File(path + File.separator + "frase.txt");

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

	/** -------------------- MENSAGENS -------------------- */

//	------------------------- PUBLIC --------------------	

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

//	------------------------- PRIVATE --------------------

	@MessageMapping("/chat.sendMessagePrivado")
	@SendTo("/topic/private")
	public ChatMessage sendMessagePrivado(@Payload br.edu.unoesc.chat.model.ChatMessage chatMessage) {
		return chatMessage;
	}
	
	//ajax pegando chave user 2
	@RequestMapping(path = "/buscarChaveUser2")
	public @ResponseBody JsonArray buscarChave() {
		return this.user2.getChavePublica();
	}
	
	private int count = 0;
	private ChatMessage user1 = new ChatMessage();
	private ChatMessage user2 = new ChatMessage();

	@MessageMapping("/chat.addUserPrivado")
	@SendTo("/topic/private")
	public ChatMessage addUserPrivado(@Payload ChatMessage chatMessage,  SimpMessageHeaderAccessor headerAccessor) {
		
		System.out.println("-------DEBUG------"+chatMessage);
		
		if(count == 0) {
			this.user1 = chatMessage;			
		}
		if(count < 2) {
			headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
			this.user2 = chatMessage;
			count++;
		}
		if(count == 2) {
			chatMessage.setChavePublica(this.user1.getChavePublica());
			count++;
		}
		if(count > 2) {
			return null;
		}
		return chatMessage;
	}

}
