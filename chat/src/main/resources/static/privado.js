'use strict';

var usernameForm = document.querySelector('#form');
var messageForm = document.querySelector('#messageForm');
var messageInput = document.querySelector('#message');
var messageArea = document.querySelector('#messageArea');
var connectingElement = document.querySelector('.connecting');

var stompClient = null;
var username = null;

var colors = [
    '#2196F3', '#32c787', '#00BCD4', '#ff5652',
    '#ffc107', '#ff85af', '#FF9800', '#39bbb0'
];


var pubKey;
var privKey;

function connect(event) {
    username = $('#nome').val().trim();

    if(username) {

        var socket = new SockJS('/ws');
        stompClient = Stomp.over(socket);

        /** gerando chaves */
        
//        var opcoes = {
//		  userIds: [{ name: username, email: username+"@unoesc.br" }],
//		  numBits: 2048,
//		  passphrase: "chave"
//		}
//        		
//        openpgp.generateKey(opcoes).then(key => {		
//        	privKey = key.privateKeyArmored
//			pubKey = key.publicKeyArmored
//        		  
//			console.log('Key generated')			
//        })
//        console.log(pubKey)
        
        
        /** fim */
        
        /** teste encrypt retornando objeto Promise */ 
        
//        openpgp.generateKey({
//            userIds: 1,
//            curve: "ed25519",
//            passphrase: "frase"
//        }).then(function (key) {
//            pubKey = key.publicKeyArmored;
//            privKey = key.privateKeyArmored;
//            
//        });
//        
//        var encrypt = function () {
//            var senderPrivateKeys, receiverPublicKeys;
//            
//            return new Promise(function (resolve) {
//                openpgp.key.readArmored(privKey).then(function (readKeys) {
//                    senderPrivateKeys = readKeys;
//                    return senderPrivateKeys.keys[0].decrypt("frase");
//                }).then(function () {
//                    return openpgp.key.readArmored(pubKey);
//                }).then(function (readKeys) {
//                    receiverPublicKeys = readKeys;
//                    return openpgp.encrypt({
//                        armor: true,
//                        compression: openpgp.enums.compression.zlib,
//                        message: openpgp.message.fromText("confidencial"),
//                        publicKeys: receiverPublicKeys.keys,
//                        privateKeys: senderPrivateKeys.keys
//                    });
//                }).then(function (encrypted) {
//                    resolve(encrypted.data);
//                });
//            });
//        };
//        
//        encrypt().then( dados => {
//        	console.log(dados);
//        });
        
        /** fim */
        
        /** outro teste */
        
        
        var options = {
    		userIds: [{ name:'Jon Smith', email:'jon@example.com' }], // multiple user IDs
    		numBits: 2048,                                            // RSA key size
    		passphrase: 'super long and hard to guess secret'         // protects the private key
    	};
        
        var privatekey;
        var publickey;
        
        openpgp.generateKey(options).then(function(key) {
        	privatekey = key.privateKeyArmored; 
        	publickey = key.publicKeyArmored;
        });
        
        var op = {
			data: 'Hello, World!',                             // input as String (or Uint8Array)
			publicKeys: openpgp.key.readArmored(publickey).keys,  // for encryption
			// privateKeys: openpgp.key.readArmored(myKey.privkey).keys // for signing (optional)
        };

        openpgp.encrypt(op).then(function(ciphertext) {
    		console.log(ciphertext.data);
        });
        
        /** fim */
		
        stompClient.connect({}, onConnected, onError);
    }
    
    event.preventDefault();
    
}


function onConnected() {
    // Subscribe to the Private Topic
    stompClient.subscribe('/topic/private', onMessageReceived);

    // Tell your username to the server
    stompClient.send("/app/chat.addUserPrivado",
        {},
        JSON.stringify({sender: username, type: 'JOIN'})
    )

    connectingElement.classList.add('hidden');
}


function onError(error) {
    connectingElement.textContent = 'Não foi possível se conectar ao websocket!';
    connectingElement.style.color = 'red';
}


function sendMessage(event) {
    var messageContent = messageInput.value.trim();

    if(messageContent && stompClient) {
        var chatMessage = {
            sender: username,
            content: messageInput.value,
            chave: pubKey,
            type: 'CHAT'
        };
        
        console.log(chatMessage);
        
        stompClient.send("/app/chat.sendMessagePrivado", {}, JSON.stringify(chatMessage));
        messageInput.value = '';
    }
    event.preventDefault();
}


function onMessageReceived(payload) {
    var message = JSON.parse(payload.body);

    var messageElement = document.createElement('li');

    if(message.type === 'JOIN') {
        messageElement.classList.add('event-message');
        message.content = message.sender + ' conectou-se!';
    } else if (message.type === 'LEAVE') {
        messageElement.classList.add('event-message');
        message.content = message.sender + ' saiu!';
    } else {
        messageElement.classList.add('chat-message');

        var avatarElement = document.createElement('i');
        var avatarText = document.createTextNode(message.sender[0]);
        avatarElement.appendChild(avatarText);
        avatarElement.style['background-color'] = getAvatarColor(message.sender);

        messageElement.appendChild(avatarElement);

        var usernameElement = document.createElement('span');
        var usernameText = document.createTextNode(message.sender);
        usernameElement.appendChild(usernameText);
        messageElement.appendChild(usernameElement);
    }

    var textElement = document.createElement('p');
    var messageText = document.createTextNode(message.content);
    textElement.appendChild(messageText);

    messageElement.appendChild(textElement);

    messageArea.appendChild(messageElement);
    messageArea.scrollTop = messageArea.scrollHeight;
}


function getAvatarColor(messageSender) {
    var hash = 0;
    for (var i = 0; i < messageSender.length; i++) {
        hash = 31 * hash + messageSender.charCodeAt(i);
    }

    var index = Math.abs(hash % colors.length);
    return colors[index];
}

$("#chat-container").ready(e => this.connect(e));
messageForm.addEventListener('submit', sendMessage, true)
