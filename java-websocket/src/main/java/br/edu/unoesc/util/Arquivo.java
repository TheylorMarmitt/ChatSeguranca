package br.edu.unoesc.util;
//package com.baeldung.websocket;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.Set;
//import java.util.concurrent.CopyOnWriteArraySet;
//
//import javax.websocket.EncodeException;
//import javax.websocket.OnClose;
//import javax.websocket.OnError;
//import javax.websocket.OnMessage;
//import javax.websocket.OnOpen;
//import javax.websocket.Session;
//import javax.websocket.server.PathParam;
//import javax.websocket.server.ServerEndpoint;
//
//import org.springframework.web.multipart.MultipartFile;
//
//
//import br.edu.unoesc.service.CertificadoService;
/**
 * 
 * 		TENTANDO PEGAR ARQUIVO POR WEBSOCKET
 * 
 */
//
//@ServerEndpoint(value = "/conectar.html/{file}")
//public class Arquivo {
//	
//    private Session session;
//    private CertificadoService service = new CertificadoService();
//    private static final Set<ChatEndpoint> chatEndpoints = new CopyOnWriteArraySet<>();
//    private static HashMap<String, String> users = new HashMap<>();
//
//    @OnOpen
//    public void aberto(Session session, @PathParam("file") MultipartFile file) throws IOException, EncodeException {
//    	
//    	String path = System.getProperty("user.home")+File.separator+"Documents"+File.separator+"certificado";
//		File novoArquivo = new File(path + File.separator + file.getName());
//		
//        boolean criado;
//		try {
//			criado = novoArquivo.createNewFile();
//			System.out.println("arquivo criado =========" + criado);
//		} catch (IOException e) {
//			System.out.println("erro ========"+ e);
//		}
//        	
//    	this.session = session;
//        	
//
//
//    }
//
//}
