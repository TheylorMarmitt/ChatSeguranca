package br.edu.unoesc;


import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.PWA;

@StyleSheet("frontend://styles/styles.css")
@Route("")
@PWA(name = "Vaadin Chat", shortName = "Vaadin Chat")
@Push
public class PaginaPrincipal extends VerticalLayout{
	
	private static final long serialVersionUID = 1L;

	public PaginaPrincipal() {
		
		HorizontalLayout layout = new HorizontalLayout();
	    Button startButton = new Button("Start chat");
	    MemoryBuffer buffer = new MemoryBuffer();
	    Upload upload = new Upload(buffer);

	    upload.addSucceededListener(event -> {
	        System.out.println(event.getMIMEType().getBytes());
	    });
	    
	    layout.add(upload);

	    add(layout);
	}
	
	
}
