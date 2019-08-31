package br.edu.unoesc;

class ChatMessage {
  private String from;
  private String message;

  ChatMessage(String from, String message) {
    this.from = from;
    this.message = message;
  }

  String getFrom() {
    return from;
  }

  String getMessage() {
    return message;
  }
}