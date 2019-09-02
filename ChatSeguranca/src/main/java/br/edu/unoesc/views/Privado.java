package br.edu.unoesc.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;

import java.security.NoSuchAlgorithmException;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import br.edu.unoesc.ChatMessage;
import br.edu.unoesc.MessageList;
import br.edu.unoesc.pgp.Simetrico;
import br.edu.unoesc.service.CertificadoService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.UnicastProcessor;

@StyleSheet("frontend://styles/styles.css")
@Route("privado")
public class Privado extends VerticalLayout {

	private static final long serialVersionUID = -2294292083024384647L;
	
	private final UnicastProcessor<ChatMessage> publisher;
	private final Flux<ChatMessage> messages;
	private String username;
	private CertificadoService service = new CertificadoService();
	private Simetrico simetrico = new Simetrico(); 
	private String m = null;

	public Privado(UnicastProcessor<ChatMessage> publisher, Flux<ChatMessage> messages) {
		this.publisher = publisher;
		this.messages = messages;
		addClassName("main-view");
		setSizeFull();
		setDefaultHorizontalComponentAlignment(Alignment.CENTER);

		H1 header = new H1("Chatchê");
		header.getElement().getThemeList().add("dark");

		add(header);
		if(service.buscaCertificado()) {
			showChat();
		}else {
			getUI().get().navigate("autenticacao");
		}
	}

	private void showChat() {
		MessageList messageList = new MessageList();

		add(messageList, createInputLayout());
		expand(messageList);

		
		
		messages.subscribe(message -> {
			
			KeyGenerator keyGenerator;
			SecretKey secretKey = null;
			
			try {
				keyGenerator = KeyGenerator.getInstance("chave");
				keyGenerator.init(168);
				secretKey = keyGenerator.generateKey();
			} catch (NoSuchAlgorithmException e1) {
				e1.printStackTrace();
			}
			
			try {
				m = simetrico.decrypt(message.getMessage().getBytes(), secretKey).toString();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
			getUI().ifPresent(ui -> ui
					.access(() -> messageList.add(new Paragraph(
							message.getFrom() + ": " + m ))));

		});
	}

	private Component createInputLayout() {
		HorizontalLayout layout = new HorizontalLayout();
		layout.setWidth("100%");

		TextField messageField = new TextField();
		Button sendButton = new Button("Send");
		sendButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

		layout.add(messageField, sendButton);
		layout.expand(messageField);

		sendButton.addClickListener(click -> {
			String msg = messageField.getValue();
			
			String m = null;
			KeyGenerator keyGenerator;
			SecretKey secretKey = null;
			
			try {
				keyGenerator = KeyGenerator.getInstance("chave");
				keyGenerator.init(168);
				secretKey = keyGenerator.generateKey();
			} catch (NoSuchAlgorithmException e1) {
				e1.printStackTrace();
			}
			
			try {
				m = simetrico.encrypt(msg.getBytes(), secretKey).toString();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			publisher.onNext(new ChatMessage(username, m));
			messageField.clear();
			messageField.focus();
		});
		messageField.focus();

		return layout;
	}

}
