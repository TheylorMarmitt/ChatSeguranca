
const chaves = nacl.box.keyPair(); // já está em base 64;

/**
 * primeiro preicisa-se gerar a public key, vai ser gerado por aqui e aqui será criptografado e decriptografado
 */

/**
 * simples exemplo de uso: 
 * 	const chave = nacl.box.keyPair();
	const crypt = encrypt(chave.publicKey, "ola mundo crypto")
	const resultado = decrypt(chave.secretKey, crypt)
	resultado vai ser: "ola mundo crypto"

 * 
 */





var encrypt = (chave, msgParams) => {
  const ephemeralKeyPair = nacl.box.keyPair()  
  const pubKeyUInt8Array = chave //nacl.util.decodeBase64(chave)  
  const msgParamsUInt8Array = nacl.util.decodeUTF8(msgParams)  
  const nonce = nacl.randomBytes(nacl.box.nonceLength)
  const encryptedMessage = nacl.box(
     msgParamsUInt8Array,
     nonce,        
     pubKeyUInt8Array,
     ephemeralKeyPair.secretKey
  )  
  return {    
    ciphertext: nacl.util.encodeBase64(encryptedMessage),    
    ephemPubKey: nacl.util.encodeBase64(ephemeralKeyPair.publicKey),
    nonce: nacl.util.encodeBase64(nonce),     
    version: "x25519-xsalsa20-poly1305"  
  }
  
}

/* Decrypt a message with a base64 encoded secretKey (privateKey) */

function decrypt(secretKey, encryptedData) {  
  const receiverSecretKeyUint8Array = secretKey
  const nonce = nacl.util.decodeBase64(encryptedData.nonce)      
  const ciphertext = nacl.util.decodeBase64(encryptedData.ciphertext)      
  const ephemPubKey = nacl.util.decodeBase64(encryptedData.ephemPubKey)      
  const decryptedMessage = nacl.box.open(
      ciphertext, 
      nonce,          
      ephemPubKey, 
      receiverSecretKeyUint8Array
  )
  return nacl.util.encodeUTF8(decryptedMessage)        
}



