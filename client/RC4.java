

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.xml.bind.DatatypeConverter;

public class RC4 {

 	static String Alg="RC4";
	static String Type="RC4";
	static int    Taille=64;
	
	
	public SecretKey getKey() throws NoSuchAlgorithmException {
		KeyGenerator keyGen;
		
		   keyGen = KeyGenerator.getInstance(Alg);
		      keyGen.init(Taille);
		SecretKey cle = keyGen.generateKey();
		return cle;
		
	}

	public static byte[] encrypter(final byte[] donnees, SecretKey cle)
		      throws NoSuchAlgorithmException, NoSuchPaddingException,
		      InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		      Cipher cipher = Cipher.getInstance(Type);
		      cipher.init(Cipher.ENCRYPT_MODE, cle);
		      return cipher.doFinal(donnees);
		  }

		  public static byte[]  decrypter(final byte[] donnees, SecretKey cle)
		      throws NoSuchAlgorithmException, NoSuchPaddingException,
		      InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		      Cipher cipher = Cipher.getInstance(Type);
		      cipher.init(Cipher.DECRYPT_MODE, cle);
		      return cipher.doFinal(donnees);
		  }
		  
	public  String Msg(String msg) throws NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
		

		final byte [] msg_byte =msg.getBytes();
		KeyGenerator keyGen;
		
		   keyGen = KeyGenerator.getInstance(Alg);
		      keyGen.init(Taille);
		SecretKey cle = keyGen.generateKey();
		

		System.out.println("Le message en clair :");
		System.out.println("En txt  : "  + msg);
		System.out.println("En Byte : " + Arrays.toString(msg_byte));
	    System.out.println("En Hex  : " + DatatypeConverter.printHexBinary(msg.getBytes()));
	    System.out.println("\n------------------------------------------------------------------------------------------");
	      
		
	    try {
	   
	      
	      System.out.println("La clé de chiffrement :");
	      System.out.println("En txt  : " +new String(cle.getEncoded()));
	      System.out.println("En byte : " +Arrays.toString(cle.getEncoded()));
	      System.out.println("En Hex  : " +DatatypeConverter.printHexBinary(cle.getEncoded()));
	      System.out.println("\n------------------------------------------------------------------------------------------");
	      
	      System.out.println("\nRésultat du chiffrement :");
	      byte[] enc = encrypter(msg_byte, cle);
	      System.out.println("En txt  : "+new String(enc));
	      System.out.println("En byte : "+Arrays.toString(enc));
	      System.out.println("En Hex  : "+DatatypeConverter.printHexBinary(enc));
	      System.out.println("\n------------------------------------------------------------------------------------------");
 
	      System.out.println("\nRésultat du déchiffrement :");
	      byte [] dec = decrypter(enc, cle);
	      System.out.println("En txt  : "+new String(dec));
	      System.out.println("En byte : "+Arrays.toString(dec));
	      System.out.println("En Hex  : "+DatatypeConverter.printHexBinary(dec));
	      System.out.println("\n------------------------------------------------------------------------------------------");
	      
	    } catch (Exception e) {
	      e.printStackTrace();
	    }
		return (DatatypeConverter.printHexBinary(encrypter(msg_byte, cle)));
		

	}
	
	
	public RC4() {
	
		
	}
}
