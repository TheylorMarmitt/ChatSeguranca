 const messageWindow = document.getElementById("messages");
 
    const sendButton = document.getElementById("send");
    const chat = document.getElementById("chat");
    const messageInput = document.getElementById("msg");
 
    const sendImageButton = document.getElementById("sendImage");
 
    const socket = new WebSocket("ws://localhost:8080/socket");
    socket.binaryType = "arraybuffer";
 
    socket.onopen = function (event) {
        addMessageToWindow("Connected");
    };
 
    socket.onmessage = function (event) {
           $("#chat").append(`<p>${event.data}</p><br/>`);
    };
 
    sendButton.onclick = function (event) {
        sendMessage(messageInput.value);
        messageInput.value = "";
    };
 
 
    function sendMessage(message) {
        socket.send(message);
    }
 
    function addMessageToWindow(message) {
        messageWindow.innerHTML += `<div>${message}</div>`
    }
    