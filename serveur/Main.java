import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.swing.*;


public class Main {
    public static void main(String args[]) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException{
        Server serverObject = new Server();
        
    	Clepartagee cp = new Clepartagee().getInstance();
        System.out.println("ma clef"+cp.getChaine());
        serverObject.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        serverObject.startRunning();
    }
}
