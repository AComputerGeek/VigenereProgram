import edu.duke.*;
import java.util.*;

/**
* 
* @author: Amir Armion 
* 
* @version: V.01
* 
*/
public class Tester 
{

    public void test()
    {
        System.out.println("\n--------------- Caesar Cracker ---------------\n");
        CaesarCracker cc = new CaesarCracker('a');

        FileResource fr        = new FileResource();
        String       encrypted = fr.asString();
        int          key       = cc.getKey(encrypted);
        String       decrypted = cc.decrypt(encrypted);
        System.out.println("\n- The key is: " + key);
        System.out.println("\n- Decrypted Message is: " + decrypted);

        
        System.out.println("\n\n--------------- Vigenere Cipher ---------------\n");
        int[]          keyVC = {17, 14, 12, 4};        
        VigenereCipher vc    = new VigenereCipher(keyVC);
        FileResource   fr1   = new FileResource();

        for(String line: fr1.lines())
        {
            System.out.println("Plain Text: " + line);
            String encrypted1 = vc.encrypt(line);
            System.out.println("Encrypted: " + encrypted1);

            String decrypted1 = vc.decrypt(encrypted1);
            System.out.println("Deccrypted: " + decrypted1);
        }

        
        System.out.println("\n\n--------------- Vigenere Breaker ---------------\n");
        FileResource    fr2  = new FileResource();
        String          text = fr2.asString();
        VigenereBreaker vb   = new VigenereBreaker();
        int[]           keys = vb.tryKeyLength(text, 4, 'e');

        for(int i = 0; i < keys.length; i++)
        {
            System.out.println(keys[i]);
        }
    }
}
