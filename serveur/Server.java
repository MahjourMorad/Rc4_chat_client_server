
import java.io.*;
import  java.net.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import  java.awt.*;
import  java.awt.event.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.*;
import javax.swing.border.Border;

public class Server extends JFrame {

    private JTextField clefText;
    private  JTextArea chatWindow;
    private  JTextArea chatWindow2;
    private  ObjectInputStream input;
    private  ObjectOutputStream output;
    private  ServerSocket server;
    private  Socket connection;
    private JButton decrypte;
    private JLabel params;
    private JLabel cryp;

    
    public static String getMessagenoncrypte() {
		return messagenoncrypte;
	}




	public static void setMessagenoncrypte(String messagenoncrypte) {
		Server.messagenoncrypte = messagenoncrypte;
	}


	private static String messagenoncrypte;
    private static String clepartage = "";





	public static String getClepartage() {
		return clepartage;
	}




	public static void setClepartage(String clepartage) {
		Server.clepartage = clepartage;
	}




	//constructor creating the gui for the server
    public  Server() throws NoSuchAlgorithmException{
      
		super("Serveur (Récepteur)");
		
	 	
        decrypte = new JButton("Déchiffrement");
        chatWindow = new JTextArea();
        chatWindow2= new JTextArea();
        clefText= new HintTextField("                 Copiez la clé ici");
        JLabel label = new JLabel();
        params = new JLabel("Paramètres: Clé");
        cryp = new JLabel("Messages reçus cryptés");


        clefText.setEditable(true);
        chatWindow.setEditable(false);

       
        
        clefText.addActionListener(
                new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent event) {
                        try {
							sendMessage(event.getActionCommand());
							decrypterMessage(event.getActionCommand());
						} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException
								| IllegalBlockSizeException | BadPaddingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (ClassNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                        clefText.setText("");
                    }

				
                }
        );
        
        
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);        
        JFrame frame = new JFrame("Récepteur (Serveur)");
        label.setFont(new Font("Verdana", Font.PLAIN, 30));
        label.setPreferredSize(new Dimension(50, 50));
        label.setLocation(25, 100);
        label.setForeground(new Color(120, 90, 40));
        label.setBackground(new Color(100, 20, 70));
        Border border = BorderFactory.createLineBorder(Color.ORANGE);
        Border border1 = BorderFactory.createLineBorder(Color.BLACK);


        chatWindow.setSize(700, 300);
        chatWindow.setLocation(250, 25);
        chatWindow.setBorder(border1);
        
        chatWindow2.setBorder(border1);
        chatWindow2.setSize(700,300);
        chatWindow2.setLocation(250,350);
        
        
        clefText.setSize(200,450);
        clefText.setLocation(25,100);
        
        params.setSize(200,100);
        params.setLocation(50,10);
        params.setFont(new Font("Verdana", Font.PLAIN, 20));
        params.setForeground(new Color(120, 90, 40));
        params.setBackground(new Color(100, 20, 70));
        
        
        decrypte.setSize(200,50);
        decrypte.setLocation(25,600);
        
        
        decrypte.addActionListener(new ActionListener() { 
      	  public void actionPerformed(ActionEvent e) { 
      		  try {
					decrypterMessage(clefText.getText());
				} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException
						| IllegalBlockSizeException | BadPaddingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
      	  } 
      	} );
        
        Color myColor = Color.decode("#FFFFCC");
        Color color = Color.decode("#CCE5FF");
        
        decrypte.setBackground(color);
        decrypte.setOpaque(true);
        
        cryp.setSize(10,10);
        cryp.setLocation(50,0);
        cryp.setFont(new Font("Verdana", Font.PLAIN, 20));

        
        
        
        label.setBackground(myColor);
        label.setOpaque(true);
        frame.add(chatWindow);
        frame.add(decrypte);
        frame.add(chatWindow2);
        frame.add(clefText);
        frame.add(params);
        frame.add(cryp);
        frame.add(label);
        frame.pack();
        frame.setSize(1000,710);
        frame.setVisible(true);
      

    }
    
    
 

    //setting the server to run
    public void startRunning() throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException{
        try{
                server= new ServerSocket(1234,100);

                while(true){
                    try{
                        waitForConnection();
                        setUpStreams();
                        whileChatting();
                    }catch(EOFException eofException){
                        showMessage("\n Connexion fermé par le serveur");
                    }finally {
                            endProcess();
                    }

                }

        }catch (IOException ioException){
            ioException.printStackTrace();
        }
    }

    //wait for connection and then display connection information
    public void waitForConnection() throws IOException{
 
        connection=server.accept();
        chatWindow.append("\n                                                                                                  Texte chiffré   ");
        chatWindow2.append("\n                                                                                                  Texte en clair   ");
        
    }

    //setting up the streams to send and receive streams
    private void setUpStreams() throws IOException{
        output = new ObjectOutputStream(connection.getOutputStream());
        output.flush();
        input = new ObjectInputStream(connection.getInputStream());
        //showMessage("Me ");
    }

    //during the chat conversation
    public  void whileChatting() throws IOException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException{
        String message= " Vous êtes maintenant connecté \n";
        sendMessage(message);
        setAbleToType(true);

        do {
            try{
                message= (String) input.readObject();
                setMessagenoncrypte(message);
               
                showMessage("\n"+message);
            }catch (ClassNotFoundException classNotFoundException){
                showMessage("\nImpossible d'envoyer le message");
            }

        }while (!message.equals("CLIENT - END"));

    }

    //close the socket and connection after chatting
    private void endProcess(){
        showMessage("\n Fermetture de la connexion\n");
        setAbleToType(false);

        try{
            input.close();
            output.close();
            connection.close();

        }catch (IOException ioException){
            ioException.printStackTrace();
        }
    }

    //send a message to the client
    private  void sendMessage(String message) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException{
        
    	 try{
             output.writeObject("Serveur - "+message);
             output.flush();

         }catch (IOException ioException){
             chatWindow.append("\n Quelque chose ne va pas\n");
         }
    	
    	
    }

    //updates the chat window to show message
    private void showMessage(final String text){
            SwingUtilities.invokeLater(
                    new Runnable() {
                        @Override
                        public void run() {
                            chatWindow.append(text);
                        }
                    }
            );
    }
    
    
	private void decrypterMessage(String actionCommand) throws ClassNotFoundException, IOException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
		// TODO Auto-generated method stub
		

		 chatWindow2.append("\n ");
	//	 chatWindow2.append(messagenoncrypte);
		 
		 RC4 rc = new RC4();
     	
    
     	
     	String lastkey = actionCommand;
     	
     	byte[] decodedKey = Base64.getDecoder().decode(lastkey);
     	SecretKey originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "RC4");
     	
     
         
         
         final byte [] msg_byte =messagenoncrypte.getBytes();
         
         byte [] dec= rc.decrypter(msg_byte, originalKey);
         
         
         chatWindow2.append(new String(dec)+"\n");
		 
		 
		 
		
	}
    

    //let the user type the message
    private  void setAbleToType(final  boolean tof){
            SwingUtilities.invokeLater(
                    new Runnable() {
                        @Override
                        public void run() {
                            clefText.setEditable(tof);
                        }
                    }
            );
    }
}
