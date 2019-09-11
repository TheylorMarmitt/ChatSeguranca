//package br.edu.unoesc.gnupg;
//
//import java.io.ByteArrayInputStream;
//import java.io.ByteArrayOutputStream;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.security.NoSuchProviderException;
//import java.security.SecureRandom;
//import java.security.Security;
//import java.util.Arrays;
//import java.util.Date;
//import java.util.Iterator;
//import java.util.List;
//
//import org.bouncycastle.bcpg.ArmoredOutputStream;
//import org.bouncycastle.jce.provider.BouncyCastleProvider;
//import org.bouncycastle.openpgp.PGPCompressedData;
//import org.bouncycastle.openpgp.PGPCompressedDataGenerator;
//import org.bouncycastle.openpgp.PGPEncryptedData;
//import org.bouncycastle.openpgp.PGPEncryptedDataGenerator;
//import org.bouncycastle.openpgp.PGPEncryptedDataList;
//import org.bouncycastle.openpgp.PGPException;
//import org.bouncycastle.openpgp.PGPLiteralData;
//import org.bouncycastle.openpgp.PGPLiteralDataGenerator;
//import org.bouncycastle.openpgp.PGPObjectFactory;
//import org.bouncycastle.openpgp.PGPOnePassSignatureList;
//import org.bouncycastle.openpgp.PGPPrivateKey;
//import org.bouncycastle.openpgp.PGPPublicKey;
//import org.bouncycastle.openpgp.PGPPublicKeyEncryptedData;
//import org.bouncycastle.openpgp.PGPPublicKeyRing;
//import org.bouncycastle.openpgp.PGPPublicKeyRingCollection;
//import org.bouncycastle.openpgp.PGPSecretKey;
//import org.bouncycastle.openpgp.PGPSecretKeyRingCollection;
//import org.bouncycastle.openpgp.PGPUtil;
//import org.bouncycastle.openpgp.operator.KeyFingerPrintCalculator;
//import org.bouncycastle.openpgp.operator.PBESecretKeyDecryptor;
//import org.bouncycastle.openpgp.operator.PGPKeyEncryptionMethodGenerator;
//import org.bouncycastle.openpgp.operator.PublicKeyDataDecryptorFactory;
//import org.bouncycastle.openpgp.operator.bc.BcPGPDataEncryptorBuilder;
//import org.bouncycastle.openpgp.operator.bc.BcPublicKeyDataDecryptorFactory;
//import org.bouncycastle.openpgp.operator.bc.BcPublicKeyKeyEncryptionMethodGenerator;
//import org.bouncycastle.openpgp.operator.jcajce.JcaKeyFingerprintCalculator;
//import org.bouncycastle.openpgp.operator.jcajce.JcePBESecretKeyDecryptorBuilder;
//import org.bouncycastle.openpgp.operator.jcajce.JcePublicKeyKeyEncryptionMethodGenerator;
//
//public class PGPFactory {
//
//	private PGPPublicKey pgpPublicKey = null;
//	private PGPSecretKeyRingCollection pgpSecretKeyRingCollection = null;
//	private KeyFingerPrintCalculator fingerPrintCalculator = null;
//	private char[] passphrase = null;
//	private static PGPFactory pgpFactory = null;
//
//	private PGPFactory() {
//	}
//
//	public static PGPFactory getInstance() {
//		if (null == pgpFactory) {
//			Security.addProvider(new BouncyCastleProvider());
//			pgpFactory = new PGPFactory();
//		}
//		return pgpFactory;
//	}
//
//	/**
//				 *      * Load Public Key,Private Key,Passphrase      
//				 */
//		public void loadKeys(String pubKeyPath, String privKeyPath, String password) {
//			FileInputStream pubKey = null;
//			FileInputStream secKey = null;
//			try {
//				fingerPrintCalculator = new JcaKeyFingerprintCalculator();
//				pubKey = new FileInputStream(pubKeyPath);
//				pgpPublicKey = readPublicKey(pubKey);
//				secKey = new FileInputStream(privKeyPath);
//				pgpSecretKeyRingCollection = new PGPSecretKeyRingCollection(PGPUtil.getDecoderStream(secKey), fingerPrintCalculator);
//				passphrase = password.toCharArray();
//			} catch (IOException ioException) {
//				System.err.println("IOException in init: " + ioException.getMessage());
//			} catch (PGPException pgpException) {
//				System.err.println("PGPException in init: " + pgpException.getMessage());
//			} finally {
//				try {
//					if (null != pubKey) {
//						pubKey.close();
//					}
//					if (null != secKey) {
//						secKey.close();
//					}
//				} catch (IOException ioException) {
//					System.err.println("IOException in init: " + ioException.getMessage());
//				}
//			}
//		}
//		
//		private PGPPrivateKey findSecretKey(PGPSecretKeyRingCollection pgpSec, long keyID, char[] pass) throws PGPException, NoSuchProviderException {
//			PGPSecretKey pgpSecKey = pgpSec.getSecretKey(keyID);
//			if (pgpSecKey == null) {
//				return null;
//			}
//			PBESecretKeyDecryptor secretKeyDecryptor = new JcePBESecretKeyDecryptorBuilder().setProvider(BouncyCastleProvider.PROVIDER_NAME).build(pass);
//			return pgpSecKey.extractPrivateKey(secretKeyDecryptor);
//		}
//		
//		private byte[] decrypt(byte[] encrypted) {
//			byte[] returnBytes = null;
//			InputStream inputStream = null;
//			InputStream clear = null;
//			InputStream unc = null;
//			ByteArrayOutputStream out = null;
//			try {
//				inputStream = new ByteArrayInputStream(encrypted);
//				inputStream = PGPUtil.getDecoderStream(inputStream);
//				PGPObjectFactory pgpF = new PGPObjectFactory(inputStream, fingerPrintCalculator);
//				PGPEncryptedDataList enc = null;
//				Object object = pgpF.nextObject();
//				if (object instanceof PGPEncryptedDataList) {
//					enc = (PGPEncryptedDataList) object;
//				} else {
//					enc = (PGPEncryptedDataList) pgpF.nextObject();
//				}
//				Iterator it = enc.getEncryptedDataObjects();
//				PGPPrivateKey sKey = null;
//				PGPPublicKeyEncryptedData pbe = null;
//				while (sKey == null && it.hasNext()) {
//					pbe = (PGPPublicKeyEncryptedData) it.next();
//					sKey = findSecretKey(pgpSecretKeyRingCollection, pbe.getKeyID(), passphrase);
//				}
//				if (sKey == null) {
//					throw new IllegalArgumentException("secret key for message not found.");
//				}
//				PublicKeyDataDecryptorFactory decryptorFactory = new BcPublicKeyDataDecryptorFactory(sKey);
//				clear = pbe.getDataStream(decryptorFactory);
//				PGPObjectFactory pgpFact = new PGPObjectFactory(clear, fingerPrintCalculator);
//				PGPCompressedData cData = (PGPCompressedData) pgpFact.nextObject();
//				pgpFact = new PGPObjectFactory(cData.getDataStream(), fingerPrintCalculator);
//				PGPLiteralData literalData = (PGPLiteralData) pgpFact.nextObject();
//				unc = literalData.getInputStream();
//				out = new ByteArrayOutputStream();
//				int ch;
//				while ((ch = unc.read()) >= 0) {
//					out.write(ch);
//				}
//				returnBytes = out.toByteArray();
//			} catch (IOException ioException) {
//				System.err.println("IOException in decrypt: " + ioException.getMessage());
//			} catch (PGPException pgpException) {
//				System.err.println("PGPException in decrypt: " + pgpException.getMessage());
//			} catch (NoSuchProviderException noSuchProviderException) {
//				System.err.println("NoSuchProviderException in decrypt: " + noSuchProviderException.getMessage());
//			}finally {
//				try {
//					if (null != out) {
//						out.close();
//					}
//					if (null != inputStream) {
//						inputStream.close();
//					}
//					if (null != clear) {
//						clear.close();
//					}
//					if (null != unc) {
//						unc.close();
//					}
//				} catch (IOException exception) {
//					System.err.println("IOException in decrypt: " + exception.getMessage());
//				}
//			}
//			return returnBytes;
//		}
//		
//		private void decryptFile(InputStream in, OutputStream out) {
//        InputStream unc = null;
//        InputStream clear = null;
//        try {
//            in = org.bouncycastle.openpgp.PGPUtil.getDecoderStream(in);
//            PGPObjectFactory pgpF = new PGPObjectFactory(in, fingerPrintCalculator);
//            PGPEncryptedDataList enc;
// 
//            Object object = pgpF.nextObject();
//            if (object instanceof PGPEncryptedDataList) {
//                enc = (PGPEncryptedDataList) object;
//            } else {
//                enc = (PGPEncryptedDataList) pgpF.nextObject();
//            }
//            Iterator<pgppublickeyencrypteddata> it = enc.getEncryptedDataObjects();
//            PGPPrivateKey sKey = null;
//            PGPPublicKeyEncryptedData pbe = null;
// 
//            while (sKey == null && it.hasNext()) {
//                pbe = it.next();
//                sKey = findSecretKey(pgpSecretKeyRingCollection, pbe.getKeyID(), passphrase);
//            }
// 
//            if (sKey == null) {
//                throw new IllegalArgumentException("Secret key for message not found.");
//            }
// 
//            clear = pbe.getDataStream(new BcPublicKeyDataDecryptorFactory(sKey));
//            PGPObjectFactory plainFact = new PGPObjectFactory(clear, fingerPrintCalculator);
//            Object message = plainFact.nextObject();
// 
//            if (message instanceof PGPCompressedData) {
//                PGPCompressedData cData = (PGPCompressedData) message;
//                PGPObjectFactory pgpFact = new PGPObjectFactory(cData.getDataStream(), fingerPrintCalculator);
//                message = pgpFact.nextObject();
//            }
// 
//            if (message instanceof PGPLiteralData) {
//                PGPLiteralData ld = (PGPLiteralData) message;
//                unc = ld.getInputStream();
//                int ch;
//                while ((ch = unc.read()) >= 0) {
//                    out.write(ch);
//                }
//            } else if (message instanceof PGPOnePassSignatureList) {
//                throw new PGPException("Encrypted message contains a signed message - not literal data.");
//            } else {
//                throw new PGPException("Message is not a simple encrypted file - type unknown.");
//            }
// 
//            if (pbe.isIntegrityProtected()) {
//                if (!pbe.verify()) {
//                    throw new PGPException("Message failed integrity check");
//                }
//            }
//        } catch (IOException ioException) {
//            System.err.println("IOException in decryptFile: " + ioException.getMessage());
//        } catch (NoSuchProviderException suchProviderException) {
//            System.err.println("NoSuchProviderException in decryptFile: " + suchProviderException.getMessage());
//        } catch (PGPException pgpException) {
//            System.err.println("PGPException in decryptFile: " + pgpException.getMessage());
//        } finally {
//            try {
//                if (null != clear) {
//                    clear.close();
//                }
//                if (null != unc) {
//                    unc.close();
//                }
//                if (null != out) {
//                    out.close();
//                }
//                if (null != in) {
//                    in.close();
//                }
//            } catch (IOException ioException) {
//                System.err.println("IOException in decryptFile: " + ioException.getMessage());
//            }
//        }
//    }
//	
//	private byte[] encrypt(byte[] clearData, String fileName, boolean armor,boolean withIntegrityCheck) {
//		ByteArrayOutputStream encOut = null;
//		OutputStream out = null;
//		ByteArrayOutputStream bOut = null;
//		OutputStream cos = null;
//		OutputStream pOut = null;
//		OutputStream cOut = null;
//		try {
//			if (fileName == null) {
//				fileName = PGPLiteralData.CONSOLE;
//			}
//			encOut = new ByteArrayOutputStream();
//			out = encOut;
//			if (armor) {
//				out = new ArmoredOutputStream(out);
//			}
//			bOut = new ByteArrayOutputStream();
//			PGPCompressedDataGenerator comData = new PGPCompressedDataGenerator(PGPCompressedDataGenerator.ZIP);
//			cos = comData.open(bOut);
//			PGPLiteralDataGenerator lData = new PGPLiteralDataGenerator();
//			pOut = lData.open(cos, PGPLiteralData.BINARY, fileName, clearData.length, new Date());
//            pOut.write(clearData);
//            lData.close();
//            comData.close();
// 
//            BcPGPDataEncryptorBuilder builder = new BcPGPDataEncryptorBuilder(PGPEncryptedData.CAST5);
//            builder.setSecureRandom(new SecureRandom());
//            builder.setWithIntegrityPacket(withIntegrityCheck);
// 
//            PGPEncryptedDataGenerator encryptedDataGenerator = new PGPEncryptedDataGenerator(builder);
//            PGPKeyEncryptionMethodGenerator method = new JcePublicKeyKeyEncryptionMethodGenerator(pgpPublicKey);
//            encryptedDataGenerator.addMethod(method);
//            byte[] bytes = bOut.toByteArray();
//            cOut = encryptedDataGenerator.open(out, bytes.length);
//            cOut.write(bytes);
//            encryptedDataGenerator.close();
//        } catch (IOException ioException) {
//            System.err.println("IOException in encrypt: " + ioException.getMessage());
//        } catch (PGPException pgpException) {
//            System.err.println("PGPException in encrypt: " + pgpException.getMessage());
//        } finally {
//            try {
//                if (null != cos) {
//                    cos.close();
//                }
//                if (null != cOut) {
//                    cOut.close();
//                }
//                if (null != out) {
//                    out.close();
//                }
//                if (null != encOut) {
//                    encOut.close();
//                }
// 
//                if (null != bOut) {
//                    bOut.close();
//                }
//                if (null != pOut) {
//                    pOut.close();
//                }
// 
//            } catch (IOException ioException) {
//                System.err.println("IOException in encrypt: " + ioException.getMessage());
//            }
//        }
//        return encOut.toByteArray();
//    }     private void encryptFile(OutputStream out, String fileName, boolean armor, boolean withIntegrityCheck) {
//        OutputStream cOut = null;
//        ByteArrayOutputStream bOut = null;
//        try {
//            if (armor) {
//                out = new ArmoredOutputStream(out);
//            }
//            bOut = new ByteArrayOutputStream();
//            PGPCompressedDataGenerator comData = new PGPCompressedDataGenerator(PGPCompressedData.ZIP);
//            PGPUtil.writeFileToLiteralData(comData.open(bOut), PGPLiteralData.BINARY, new File(fileName));
//            comData.close();
// 
//            BcPGPDataEncryptorBuilder dataEncryptor = new BcPGPDataEncryptorBuilder(PGPEncryptedData.TRIPLE_DES);
//            dataEncryptor.setSecureRandom(new SecureRandom());
//            dataEncryptor.setWithIntegrityPacket(withIntegrityCheck);
// 
//            PGPEncryptedDataGenerator encryptedDataGenerator = new PGPEncryptedDataGenerator(dataEncryptor);
//            encryptedDataGenerator.addMethod(new BcPublicKeyKeyEncryptionMethodGenerator(pgpPublicKey));
// 
//            byte[] bytes = bOut.toByteArray();
//            cOut = encryptedDataGenerator.open(out, bytes.length);
//            cOut.write(bytes);
//            encryptedDataGenerator.close();
//        } catch (IOException ioException) {
//            System.err.println("IOException in encryptFile: " + ioException.getMessage());
//        } catch (PGPException pgpException) {
//            System.err.println("PGPException in encryptFile: " + pgpException.getMessage());
//        } finally {
//            try {
//                if (null != cOut) {
//                    cOut.close();
//                }
//                if (null != out) {
//                    out.close();
//                }
//                if (null != bOut) {
//                    bOut.close();
//                }
//            } catch (IOException ioException) {
//                System.err.println("IOException in encryptFile: " + ioException.getMessage());
//            }
//        }
// 
//    }     private PGPPublicKey readPublicKey(InputStream in) throws IOException, PGPException {
//        in = PGPUtil.getDecoderStream(in);
//        PGPPublicKeyRingCollection pgpPub = new PGPPublicKeyRingCollection(in, fingerPrintCalculator);
//        Iterator rIt = pgpPub.getKeyRings();
//        while (rIt.hasNext()) {
//            PGPPublicKeyRing kRing = (PGPPublicKeyRing) rIt.next();
//            Iterator kIt = kRing.getPublicKeys();
//            while (kIt.hasNext()) {
//                PGPPublicKey k = (PGPPublicKey) kIt.next();
//                if (k.isEncryptionKey()) {
//                    return k;
//                }
//            }
//        }
//        throw new IllegalArgumentException("Can't find encryption key in key ring.");
//    }     public String encrypt(String clearData) {
//        String encryptedData = "";
//        try {
//            byte[] encryptedBytes = encrypt(clearData.getBytes(), null, true, true);
//            String encryptedText = new String(encryptedBytes);
//            List<string> encryptedTextLines = Arrays.asList(encryptedText.split("\n\n"));
//            String encryptedParagraph = encryptedTextLines.get(1);
//            List<string> encryptedLines = Arrays.asList(encryptedParagraph.split("\n"));
// 
//            for (int i = 0; i < encryptedLines.size() - 1; i++) {
//                encryptedData += encryptedLines.get(i);
//            }
//        } catch (Exception exception) {
//            System.err.println("Exception in encrypt: " + exception.getMessage());
//        }
//        return encryptedData;
//    }     public String decrypt(String encryptedData) {
//        byte[] decrypted = decrypt(encryptedData.getBytes());
//        return new String(decrypted);
//    }
//
//	public void encryptFile(String plainFilePath, String encryptFilePath) {
//		FileOutputStream out = null;
//		try {
//			out = new FileOutputStream(encryptFilePath);
//			encryptFile(out, plainFilePath, false, true);
//		} catch (IOException ioException) {
//			System.err.println("IOException in encryptFile: " + ioException.getMessage());
//		} finally {
//			try {
//				if (null != out) {
//					out.close();
//				}
//			} catch (IOException ioException) {
//				System.err.println("IOException in encryptFile: " + ioException.getMessage());
//            }
//        }
//    }
//
//	public void decryptFile(String encryptFilePath, String decryptFilePath) {
//		FileInputStream in = null;
//		FileOutputStream out = null;
//		try {
//			in = new FileInputStream(encryptFilePath);
//			out = new FileOutputStream(decryptFilePath);
//			decryptFile(in, out);
//		} catch (IOException ioException) {
//			System.err.println("IOException in decryptFile: " + ioException.getMessage());
//		} finally {
//			try {
//				if (null != in) {
//					in.close();
//				}
//				if (null != out) {
//					out.close();
//				}
//			} catch (IOException ioException) {
//				System.err.println("IOException in decryptFile: " + ioException.getMessage());
//            }
//        }
//    }
//}