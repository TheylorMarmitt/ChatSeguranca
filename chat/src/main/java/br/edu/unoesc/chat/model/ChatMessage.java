package br.edu.unoesc.chat.model;

/**
 * Created by rajeevkumarsingh on 24/07/17.
 */
public class ChatMessage {
    private MessageType type;
    private String content;
    private String sender;
    
    private String chavePublica;

    public enum MessageType {
        CHAT,
        JOIN,
        LEAVE
    }
    
    public ChatMessage() {
    	
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

	public String getChavePublica() {
		return chavePublica;
	}

	public void setChavePublica(String chavePublica) {
		this.chavePublica = chavePublica;
	}

	@Override
	public String toString() {
		return "ChatMessage [type=" + type + ", content=" + content + ", sender=" + sender + ", chavePublica="
				+ chavePublica + "]";
	}
	
}
