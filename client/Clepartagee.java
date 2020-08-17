import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.SecretKey;

public  class Clepartagee {
	

	private static Clepartagee compteur = null; 
	
	
	
	public static Clepartagee getInstance() throws NoSuchAlgorithmException 
    { 
        if (compteur == null) 
        	compteur = new Clepartagee(); 
  
        return compteur; 
    } 
	
	public String getChaine() {
		return chaine;
	}

	public void setChaine(String chaine) {
		this.chaine = chaine;
	}

	public Clepartagee() throws NoSuchAlgorithmException {
		

		super();
		
		RC4 rc = new RC4();
    	System.out.println(compteur);
		SecretKey cle = rc.getKey();
		
		String encodedKey = Base64.getEncoder().encodeToString(cle.getEncoded());
		setChaine(encodedKey);
		
		
	}
	public  String chaine = "haloo";

}
