import java.util.*;
import java.io.*;
import edu.duke.*;

/**
* 
* @author: Amir Armion 
* 
* @version: V.01
* 
*/
public class VigenereBreaker 
{

    public String sliceString(String message, int whichSlice, int totalSlices) 
    {
        StringBuilder s = new StringBuilder();

        for(int i = whichSlice; i < message.length(); i += totalSlices)
        {
            s.append(message.charAt(i));
        }

        return s.toString();
    }

    public int[] tryKeyLength(String encrypted, int klength, char mostCommon) 
    {
        CaesarCracker cc   = new CaesarCracker(mostCommon);

        int[]         keys = new int[klength];

        for(int i = 0; i < keys.length; i++)
        {
            String slice = sliceString(encrypted, i, klength);
            int    key   = cc.getKey(slice);
            keys[i]      = key;
        }

        return keys;
    }

    public void breakVigenere() 
    {
        // For the third part of week 4
        HashMap<String, HashSet<String>> languages = new HashMap<>();
        String                           language;

        DirectoryResource dr = new DirectoryResource();

        for(File f: dr.selectedFiles())
        {
            language          = f.getName();
            
            FileResource    file       = new FileResource(f);
            HashSet<String> dictionary = readDictionary(file);

            languages.put(language, dictionary);
        }

        FileResource fr  = new FileResource();

        String encrypted = fr.asString();

        String decrypted = breakForAllLangs(encrypted, languages);

        System.out.println("\n- Decrypted Text: " + decrypted);

        // For the second part of week 4
        //FileResource fr1 = new FileResource();
        //String encrypted = fr1.asString();
        //FileResource fr2 = new FileResource("dictionaries/English.txt");
        //HashSet<String> dictionary = readDictionary(fr2);
        //String decrypted = breakForLanguage(encrypted, dictionary);
        //System.out.println("\n- Decrypted Text: " + decrypted);
        // This version is for first part of this lesson
        //FileResource fr  = new FileResource();
        //String encrypted = fr.asString();
        //System.out.println("\n- Encrypted Text: " + encrypted);

        //int[] keys = tryKeyLength(encrypted, 4, 'e');        
        //System.out.println("\n- All keys: ");

        //for(int i = 0; i < keys.length; i++)
        //{
        //    System.out.println(keys[i]);
        //}

        //VigenereCipher vc        = new VigenereCipher(keys);
        //String         decrypted = vc.decrypt(encrypted);
        //System.out.println("\n- Decrypted text: " + decrypted);
    }

    public HashSet<String> readDictionary(FileResource fr)
    {
        HashSet<String> dictionary = new HashSet<>();

        for(String line: fr.lines())
        {
            dictionary.add(line.toLowerCase());
        }

        return dictionary;
    }

    public int countWords(String message, HashSet<String> dictionary)
    {
        String[] words = message.split("\\W+");
        int      count = 0;

        for(int i = 0; i < words.length; i++)
        {
            if(dictionary.contains(words[i].toLowerCase()))
            {
                count++;
            }
        }

        return count;    
    }

    public String breakForLanguage(String encrypted, HashSet<String> dictionary)
    {
        char     mostCommonChar;
        int[]    keys;
        String   decrypted;
        int      count;
        int      max              = 0;
        int      theKeyLength;
        int[]    theKeys          = {0};
        String[] allWords         = null;
        int      validWords       = 0;
        String   decryptedMessage = null;

        mostCommonChar = mostCommonCharIn(dictionary);

        for(int keyLength = 1; keyLength <= 100; keyLength++)
        {
            keys              = tryKeyLength(encrypted, keyLength, mostCommonChar); 
            VigenereCipher vc = new VigenereCipher(keys);
            decrypted         = vc.decrypt(encrypted);
            count             = countWords(decrypted, dictionary);

            if(count > max)
            {
                max              = count;
                theKeyLength     = keyLength;
                theKeys          = keys;
                allWords         = encrypted.split("\\W+");
                validWords       = count;
                decryptedMessage = decrypted;
            }
        }

        // For the second part of week 4
        //System.out.println("\n- All words is: "       + allWords.length);
        //System.out.println("\n- All valid words is: " + validWords);
        //System.out.println("\n- The key length is: "  + theKeys.length);
        //System.out.println("\n- The keys are: ");

        //for(int i = 0; i < theKeys.length; i++)
        //{
        //    System.out.println(theKeys[i]);
        //}

        return decryptedMessage;
    }

    public char mostCommonCharIn(HashSet<String> dictionary)
    {
        String alphabet = "abcdefghijklmnopqrstuvwxyz";
        int[]  counter  = new int[26];

        for(String word: dictionary)
        {
            for(int i = 0; i < word.length(); i++)
            {
                char c               = word.charAt(i);
                int  indexOfChar     = alphabet.indexOf(c);

                if(indexOfChar != -1)
                {
                    counter[indexOfChar] = counter[indexOfChar] + 1;
                }
            }
        }

        int  maxVal     = 0;
        char commonChar = '\u0000';

        for(int i = 0; i < counter.length; i++)
        {
            if(counter[i] > maxVal)
            {
                maxVal     = counter[i];
                commonChar = alphabet.charAt(i);;
            }
        }

        return commonChar;        
    }

    public String breakForAllLangs(String encrypted, HashMap<String, HashSet<String>> languages)
    {
        String   lang              = null;
        String   finalDecryptedMsg = null;
        String   decrypted;
        int      maxCountWord      = 0;
        int      max               = 0;
        String[] allWords          = null;
        String[] finalAllWords     = null;
        int      validWords        = 0;
        int      finalValidWords   = 0;

        for(String language: languages.keySet())
        {
            decrypted   = breakForLanguage(encrypted, languages.get(language));
            int counter = countWords(decrypted,  languages.get(language));

            if(counter > max)
            {
                max                = counter;
                finalDecryptedMsg  = decrypted;
                allWords           = encrypted.split("\\W+");
                validWords         = counter;
                lang               = language;
            }
        }

        System.out.println("\n- Language is: "        + lang);
        System.out.println("\n- All words is: "       + allWords.length);
        System.out.println("\n- All valid words is: " + validWords);

        return finalDecryptedMsg;
    }
}

