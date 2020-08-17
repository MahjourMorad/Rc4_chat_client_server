import com.sun.xml.internal.ws.api.model.MEP;
import sun.security.x509.IPAddressName;

import java.net.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import java.io.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.*;
import javax.swing.border.Border;

import java.awt.*;
import java.awt.event.*;

public class Client extends JFrame {
    private JTextField userText;
    private JTextArea chatWindow;
    private  JTextArea chatWindow2;
    private  JTextArea chatWindow3;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private JButton crypte;
    private JLabel params;
    private String message="";
    private String serverIP;
    private Socket connection;

    //constructor
    public Client(String host){
        super("Client (Emetteur) ");
        serverIP = host;
        
        
        
      // userText = new JTextField("dbei");
      
       
        chatWindow3 = new JTextArea("");
        chatWindow=new JTextArea();
        crypte = new JButton("Chiffrement et envoi");
        params = new JLabel("Paramètres: Clé");
        JLabel label = new JLabel();
        
        
        userText= new HintTextField("\n                                                                                                  Texte en clair   ");
        userText.setEditable(true);
        userText.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent event) {
                        try {
							sendMessage(event.getActionCommand());
						} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException
								| IllegalBlockSizeException | BadPaddingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                     //   userText.setText("");
                    }
                }
        );
        
        
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);        

   
       
        JFrame frame = new JFrame("Emetteur (Client)");
        label.setFont(new Font("Verdana", Font.PLAIN, 30));
        label.setPreferredSize(new Dimension(50, 50));
        label.setLocation(25, 100);
        label.setForeground(new Color(120, 90, 40));
        label.setBackground(new Color(100, 20, 70));
        Border border = BorderFactory.createLineBorder(Color.ORANGE);
        Border border1 = BorderFactory.createLineBorder(Color.BLACK);

        
        chatWindow.setBorder(border1);
        label.setBorder(border);
        userText.setSize(700, 300);
        userText.setLocation(250, 25);
        
        
        chatWindow.setSize(700,300);
        chatWindow.setLocation(250,350);
        
        chatWindow3.setSize(200,450);
        chatWindow3.setLocation(25,100);
        
        crypte.setSize(200,50);
        crypte.setLocation(25,600);
        frame.setBackground(Color.RED);
        
        params.setSize(200,100);
        params.setLocation(50,10);
        params.setFont(new Font("Verdana", Font.PLAIN, 20));
        params.setForeground(new Color(120, 90, 40));
        params.setBackground(new Color(100, 20, 70));
        Color myColor = Color.decode("#FFFFCC");
        Color color = Color.decode("#CCE5FF");

        crypte.setBackground(color);
        crypte.setOpaque(true);

        
        
        crypte.addActionListener(new ActionListener() { 
        	  public void actionPerformed(ActionEvent e) { 
        		  try {
					sendMessage(userText.getText());
				} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException
						| IllegalBlockSizeException | BadPaddingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
        	  } 
        	} );
        
        
        
        label.setBackground(myColor);
        label.setOpaque(true);
        frame.add(chatWindow3);
        frame.add(crypte);
        frame.add(params);
        frame.add(chatWindow);
        frame.add(userText);
        frame.add(label);
        frame.pack();
        frame.setSize(1000,710);
        frame.setVisible(true);

        
        
    }

    //setting up the server
    public void startRunning() throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException{
        try{
            connectToServer();
            setUpStreams();
            whileChatting();
        }catch (EOFException eofException){
            showMessage("\n Client déconnecté");
        }catch (IOException ioException){
            ioException.printStackTrace();
        }finally {
            closeConnnection();
        }
    }

    //connect to server
    private void connectToServer() throws IOException{
        connection = new Socket(InetAddress.getByName(serverIP),1234);
        showMessage("\n                                                                                                  Texte chiffré   ");
        //userText.setText("\n                                                                                                  Texte en clair   ");
        
    }

    //setting up the streams to send and receive messages
    private void setUpStreams() throws IOException{
        output = new ObjectOutputStream(connection.getOutputStream());
        output.flush();
        input = new ObjectInputStream(connection.getInputStream());
        
    }

    //while chatting with server
    private  void whileChatting() throws IOException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException{
        ableToType(true);
        do {
            try{
            	
            	RC4 rc = new RC4();
            	
            	Clepartagee cp = new Clepartagee().getInstance();
            	
            	String lastkey = cp.getChaine();
            	
            	byte[] decodedKey = Base64.getDecoder().decode(lastkey);
            	SecretKey originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "RC4");
            	
                message = (String) input.readObject();
                
                
                final byte [] msg_byte =message.getBytes();
                
                byte [] dec= rc.decrypter(msg_byte, originalKey);
                chatWindow3.append(new String(dec)+"\n");
                showMessage("\n"+ message);
                
                
                
                
                chatWindow3.setText("la clé est:  "+"\n"+cp.getChaine());
               
        

            }catch (ClassNotFoundException classNotFoundException){
                showMessage("\n message n'est reconnu \n");
            }

        }while (!message.equals("SERVER - END"));
    }

    //close the streams and sockets
    private  void closeConnnection(){
        showMessage("\n Fermeture du Chat, Serveur déconnecté \n");
        ableToType(false);
        try{
            input.close();
            output.close();
            connection.close();
        }catch (IOException ioExceptiom){
            ioExceptiom.printStackTrace();
        }
    }

    //sending messages
    private  void sendMessage(String message) throws NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException{
    	try{
         	RC4 rc = new RC4();
        	
         	Clepartagee cp = new Clepartagee().getInstance();

      
        	String lastkey = cp.getChaine();
        	
        	
        	
        	
        	byte[] decodedKey = Base64.getDecoder().decode(lastkey);
        	SecretKey originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "RC4");
        	
        	
        	
//        	 System.out.println("la clef En txt  : " +new String(rc.getKey().getEncoded()));
   //     	System.out.println(""+rc.getKey().getEncoded());
        	final byte [] msg_byte=message.getBytes();
//        	System.out.println("mesaage crypte "+msg_byte.toString());
      	
        	
        	
        	
            byte[] enc = rc.encrypter(msg_byte, originalKey);
            
       
//  	      System.out.println("En txt  : "+new String(enc));
        	
        	String messagecrypte = Arrays.toString(msg_byte);
        	
        	
//        	String encodedKey = Base64.getEncoder().encodeToString(cle.getEncoded());
        	
      //  	System.out.println("la clef encode est "+encodedKey);
        	
      //  	byte[] decodedKey = Base64.getDecoder().decode(encodedKey);
        	// rebuild key using SecretKeySpec
        
        	
//        	String originalKeych = Base64.getEncoder().encodeToString(cle.getEncoded());
//        	System.out.println("la clef decode est :"+originalKeych);
        	
        	
        	
        	
        	
            output.writeObject(new String(enc));
            output.flush();
            showMessage("\n Client - " + new String(enc));
            
            chatWindow2.append(" Client - " + new String(enc)+"\n");
           // chatWindow2.append("cle est "+ clefText.getText());
            

            
            
            
            
        }catch (IOException ioException){
            chatWindow.append("\n Désolée, envoi de message impossible \n");
        }
    }

    //change or update the chatwindow area
    private void showMessage(final String message){
        SwingUtilities.invokeLater(
                new Runnable() {
                    @Override
                    public void run() {
                        chatWindow.append(message);
                    }
                }
        );
    }

    private  void ableToType(boolean tof){
        SwingUtilities.invokeLater(
                new Runnable() {
                    @Override
                    public void run() {
                        userText.setEditable(tof);
                    }
                }
        );
    }


}
