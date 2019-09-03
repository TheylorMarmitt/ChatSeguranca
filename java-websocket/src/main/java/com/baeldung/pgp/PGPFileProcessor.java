/**
 * 
 *  Faz a encriptação e decriptação com arquivos  /// GnuPGActions faz com texto
 *  
 * 
 */

package com.baeldung.pgp;
import java.io.FileInputStream;
import java.io.FileOutputStream;
 
public class PGPFileProcessor {
 
    private String passphrase;
 
    private String keyFile;
 
    private String inputFile;
 
    private String outputFile;
 
    private boolean asciiArmored = true;
 
    private boolean integrityCheck = true;
 
    public boolean encrypt(String chave, String arquivoSaida, String arquivoEntrada) throws Exception {
        FileInputStream keyIn = new FileInputStream(chave);
        FileOutputStream out = new FileOutputStream(arquivoSaida);
        PGPUtil.encryptFile(out, arquivoEntrada, PGPUtil.readPublicKey(keyIn),
            asciiArmored, integrityCheck);
        out.close();
        keyIn.close();
        return true;
    }
 
    public boolean decrypt(String chavePrivada, String arquivoSaida, String arquivoEntrada, String fraseSeguranca) throws Exception {
         FileInputStream in = new FileInputStream(arquivoEntrada);
         FileInputStream keyIn = new FileInputStream(chavePrivada);
         FileOutputStream out = new FileOutputStream(arquivoSaida);
         PGPUtil.decryptFile(in, out, keyIn, fraseSeguranca.toCharArray());
         in.close();
         out.close();
         keyIn.close();
         return true;
    }
 
    public boolean isAsciiArmored() {
        return asciiArmored;
    }
 
    public void setAsciiArmored(boolean asciiArmored) {
        this.asciiArmored = asciiArmored;
    }
 
    public boolean isIntegrityCheck() {
        return integrityCheck;
    }
 
    public void setIntegrityCheck(boolean integrityCheck) {
        this.integrityCheck = integrityCheck;
    }
 
    public String getPassphrase() {
        return passphrase;
    }
 
    public void setPassphrase(String passphrase) {
        this.passphrase = passphrase;
    }
 
    public String getKeyFile() {
        return keyFile;
    }
 
    public void setKeyFile(String keyFile) {
        this.keyFile = keyFile;
    }
 
    public String getInputFile() {
        return inputFile;
    }
 
    public void setInputFile(String inputFile) {
        this.inputFile = inputFile;
    }
 
    public String getOutputFile() {
        return outputFile;
    }
 
    public void setOutputFile(String outputFile) {
        this.outputFile = outputFile;
    }
 
}