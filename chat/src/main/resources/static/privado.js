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
        
        
//        var options = {
//    		userIds: [{ name:'Jon Smith', email:'jon@example.com' }], // multiple user IDs
//    		numBits: 2048,                                            // RSA key size
//    		passphrase: 'super long and hard to guess secret'         // protects the private key
//    	};
//        
//        var privatekey;
//        var publickey;
//        
//        openpgp.generateKey(options).then(function(key) {
//        	privatekey = key.privateKeyArmored; 
//        	publickey = key.publicKeyArmored;
//        });
//        
//        var op = {
//    	    message: openpgp.message.fromBinary(new Uint8Array([0x01, 0x01, 0x01])), // input as Message object
//    	    passwords: ['secret stuff'],                                             // multiple passwords possible
//    	    armor: false                                                             // don't ASCII armor (for Uint8Array output)
//    	};
//
//    	openpgp.encrypt(options).then(function(ciphertext) {
//    	    console.log(ciphertext.message.packets.write()) // get raw encrypted packets as Uint8Array
//    	});        
        /** fim */
        
        
        /** Mais um teste */
        
        const passphrase = "segredo"; 
        const privkey = "";
        
        const pubKey = "-----BEGIN PGP PUBLIC KEY BLOCK-----" +
        		"" +
        		"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" +
        		"r+j1uOwYkQ==" +
        		"=up3c" +
        		"-----END PGP PUBLIC KEY BLOCK-----";
        
        
        const encryptDecryptFunction = async() => {
            const privKeyObj = (await openpgp.key.readArmored(privkey)).keys[0]
            await privKeyObj.decrypt(passphrase)

            const options = {
                message: openpgp.message.fromText('Hello, World!'),       // input as Message object
                publicKeys: (await openpgp.key.readArmored(pubkey)).keys, // for encryption
                privateKeys: [privKeyObj]                                 // for signing (optional)
            }

            openpgp.encrypt(options).then(ciphertext => {
                encrypted = ciphertext.data
                console.log(encrypted)// '-----BEGIN PGP MESSAGE ... END PGP MESSAGE-----'
                return encrypted
            })
            .then(async encrypted => {
                const options = {
                    message: await openpgp.message.readArmored(encrypted),    // parse armored message
                    publicKeys: (await openpgp.key.readArmored(pubkey)).keys, // for verification (optional)
                    privateKeys: [privKeyObj]                                 // for decryption
                }

                openpgp.decrypt(options).then(plaintext => {
                    console.log(plaintext.data)
                    return plaintext.data // 'Hello, World!'
                })

            })
        }

        encryptDecryptFunction()     
        
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
