package br.edu.unoesc.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Push;
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
	    Button pri = new Button("Chat Privado");
	    Button pub = new Button("Chat Público");
	    
	    layout.add(pri, pub);
	    add(layout);
	    getStyle().set("align", "center");
	    getStyle().set("margin-top", "40px");
	    pri.addClickListener(e ->{
	    	pri.getUI().get().navigate("privado");
	    });
	    
	    pub.addClickListener(e ->{
	    	pub.getUI().get().navigate("publico");
	    });
	    
	}
	
	
}
