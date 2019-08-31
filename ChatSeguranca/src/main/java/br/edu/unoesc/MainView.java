package br.edu.unoesc;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.component.textfield.TextField;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import reactor.core.publisher.Flux;
import reactor.core.publisher.UnicastProcessor;

@StyleSheet("frontend://styles/styles.css")
@Route("inicial")
@Push
public class MainView extends VerticalLayout {

  private final UnicastProcessor<ChatMessage> publisher;
  private final Flux<ChatMessage> messages;
  private String username;


  public MainView(UnicastProcessor<ChatMessage> publisher,
                  Flux<ChatMessage> messages) {
    this.publisher = publisher;
    this.messages = messages;
    addClassName("main-view");
    setSizeFull();
    setDefaultHorizontalComponentAlignment(Alignment.CENTER);

    H1 header = new H1("Vaadin Chat");
    header.getElement().getThemeList().add("dark");

    add(header);

    showChat();
  }


  private void showChat() {
    MessageList messageList = new MessageList();

    add(messageList, createInputLayout());
    expand(messageList);


    messages.subscribe(message -> {
      getUI().ifPresent(ui ->
          ui.access(() ->
              messageList.add(
                  new Paragraph(message.getFrom() + ": " +
                      message.getMessage())
              )
          ));

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
      publisher.onNext(new ChatMessage(username, messageField.getValue()));
      messageField.clear();
      messageField.focus();
    });
    messageField.focus();

    return layout;
  }

}
