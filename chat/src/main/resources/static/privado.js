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

//const chave = nacl.box.keyPair();
// const crypt = encrypt(chave.publicKey, "ola mundo crypto");
// const resultado = decrypt(chave.secretKey, crypt);

var alicePub = ""
var alicePriv = ""
var frase = ""	
	

function connect(event) {
    username = $('#nome').val().trim();

    
    if(username) {

    	///
    	$.ajax({
  		  url: 'buscarPubKey',
  		  success: function(result) {

  			var alice_pgp_key = result
  			
	    	kbpgp.KeyManager.import_from_armored_pgp({
	    	  armored: alice_pgp_key
	    	}, function(err, alice) {
	    	  if (!err) {
	    		  alicePub = alice;
	    		  console.log(alice.armored_pgp_public);
	    	  }else{
	    		  console.log("erro em import"+ err);
	    	  }
	    	  
	    	});

  		  }
    	});
    	
    	$.ajax({
  		  url: 'fraseSeguranca',
  		  success: function(frase) {
  			
  			$.ajax({
  			  url: 'buscarPrivKey',
  			  success: function(result) {

  				  var alice_pgp_key    = result;
  				  var alice_passphrase = frase;

  		    	kbpgp.KeyManager.import_from_armored_pgp({
  		    	  armored: alice_pgp_key
  		    	}, function(err, alice) {
  		    		alicePriv = alice
  		    	  if (!err) {
  		    	    if (alice.is_pgp_locked()) {
  		    	      alice.unlock_pgp({
  		    	    	  passphrase: alice_passphrase
  		    	      }, function(err) {
  		    	        if (!err) {
  		    	          console.log("Loaded private key with passphrase");
  		    	        }
  		    	      });
  		    	    } else {
  		    	      console.log("Loaded private key w/o passphrase");
  		    	    }
  		    	  }
  		    	});

  			  }
  	      	});
  			  
  		  }
    	});
    	
    	
    	$.ajax({
    		  url: 'userPrivado',
    		  success: function(result) {
    			  if(result < 2){
    				  var socket = new SockJS('/ws');
    				  stompClient = Stomp.over(socket);
    				  stompClient.connect({}, onConnected, onError);   
    			  }else{
    				  connectingElement.textContent = 'Não foi possível se conectar ao websocket!';
    				  connectingElement.style.color = 'red';
    			  }
    		  }
    	});
    }
    
    event.preventDefault();
    
}

function onConnected() {
    // Subscribe to the Private Topic
    stompClient.subscribe('/topic/private', onMessageReceived);

    // Tell your username to the server
    stompClient.send("/app/chat.addUserPrivado",
        {},
        JSON.stringify({sender: username, type: 'JOIN', chavePublica: alicePub.armored_pgp_public})        	
    )

    connectingElement.classList.add('hidden');
}


function onError(error) {
    connectingElement.textContent = 'Não foi possível se conectar ao websocket!';
    connectingElement.style.color = 'red';
}


function sendMessage(event) {
	
    var messageContent = messageInput.value.trim();
    
    /** TROCANDO DE CHAVE EM POSSIVEL ERRO FUTURO EM USERS EM PCs DIFERENTES*/
//    $.ajax({
//		  url: 'chaveUser1',
//		  success: function(chave1) {
//			  if(alicePub.armored_pgp_public === chave1){
//				  $.ajax({
//					  url: 'chaveUser2',
//					  success: function(chave2){
//						  alicePub.armored_pgp_public = chave2
//					  }
//				  });
//			  }else{
//				  alicePub.armored_pgp_public = chave1
//			  }
//		  }
//    });
    
    // criptografando e assinando
    var params = {
  		  msg: messageInput.value,
  		  encrypt_for: alicePub,
  		  sign_with:   alicePriv
  		};
  	
	kbpgp.box(params, function(err, result_string, result_buffer) {
		
		if(messageContent && stompClient) {
	        var chatMessage = {
	            sender: username,
	            content: result_string,
	            type: 'CHAT'
	        };
	        
	        stompClient.send("/app/chat.sendMessagePrivado", {}, JSON.stringify(chatMessage));
	        messageInput.value = '';
	    }
		
	});
	// fim
	
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
    
    // descriptar
    var ring = new kbpgp.keyring.KeyRing;
    var kms = [ alicePriv ];
    var pgp_msg = message.content
    
    for (var i in kms) {
      ring.add_key_manager(kms[i]);
    }
    
    kbpgp.unbox({keyfetch: ring, armored: pgp_msg }, function(err, literals) {
      if (err != null) {
        return console.log("Problem: " + err);
      } else {
        console.log("decrypted message");
        console.log(literals[0].toString());
        
      //  problemas com variavel km => verificando fingerprint   
        
//        var ds = km = null;
//        ds = literals[0].get_data_signer();
//        if (ds) { km = ds.get_key_manager(); }
//        if (km) {
//          console.log(km.get_pgp_fingerprint().toString('hex'));
//        }
        
      }
      
      // mensagem 
      var messageText = document.createTextNode(literals[0].toString());
      textElement.appendChild(messageText);
      
    });
	// fim
    
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
