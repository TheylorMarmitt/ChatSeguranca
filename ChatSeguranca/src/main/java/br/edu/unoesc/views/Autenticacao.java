package br.edu.unoesc.views;

import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.router.Route;

@StyleSheet("frontend://styles/styles.css")
@Route("autenticacao")
public class Autenticacao extends VerticalLayout{

	private static final long serialVersionUID = -8274922127183851458L;
	
	public Autenticacao() {
	    MemoryBuffer buffer = new MemoryBuffer();
	    Upload upload = new Upload(buffer);

	    upload.addSucceededListener(event -> {
	        event.getMIMEType().getBytes();
	    });
	    
	    add(upload);
	    getStyle().set("align", "center");
	    getStyle().set("margin-top", "30px");

	}
	
	
	

}
