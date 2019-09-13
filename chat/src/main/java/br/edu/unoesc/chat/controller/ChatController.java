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
	
	private int count = 0;

	@MessageMapping("/chat.addUserPrivado")
	@SendTo("/topic/private")
	public ChatMessage addUserPrivado(@Payload ChatMessage chatMessage,  SimpMessageHeaderAccessor headerAccessor) {
		
		if(count < 2) {
			headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
			count++;
		}else {
			return null;
		}
		
		return chatMessage;
	}

}
