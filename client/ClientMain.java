
 
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.swing.JFrame;

public class ClientMain {
    public static void main(String args[]) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException{
        Client clientObject = new Client("127.0.0.1");
        clientObject.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        clientObject.startRunning();
    }
}
