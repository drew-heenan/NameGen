
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Drew Heenan
 */
public class NameGenerator {
    
    public static final char[] VOWELS = new char[]{'a', 'e', 'i', 'o', 'u', 'y'};
    public static final String[] COMMON_CONSONANT_COMBOS = new String[]{"dr", "ch", "st", "fr", "cl", "th"};
    
    private Random rand;
    
    private Map<Character, Map<Character, Double>> frequencies;
    
    private int minChars, maxChars;
    
    public NameGenerator(String referencesPath, int minChars, int maxChars) {
        this.rand = new Random();
        this.minChars = minChars;
        this.maxChars = maxChars;
        NameLoader nl = new NameLoader(referencesPath);
        this.frequencies = nl.getFrequencies();
    }
    
    public String makeName(String firstLetters) {
        String name = firstLetters;
        
        int maxLength = minChars + rand.nextInt(maxChars - minChars + 1);
        
        //Continue adding characters until size conditions are met.
        while(name.length() < maxLength) {
            double p = rand.nextDouble()*rand.nextInt(15);
            double cumulativeProb = 0.0;
            char lastC = name.charAt(name.length()-1);
            for(char newC : frequencies.get(lastC).keySet()) {
                cumulativeProb += frequencies.get(lastC).get(newC);
                
                //Select a new letter based on the preceding two letters.
                if(p < cumulativeProb) {
                    if(name.length() >= 2) {
                        boolean a = isVowel(name.charAt(name.length()-2)),
                                b = isVowel(lastC), 
                                c = isVowel(newC);
                        //Ensure that three consonants or vowels are not placed together.
                        if((a && b && c) || (!a && !b && !c)) {
                            continue;
                        }
                    }
                    name += "" + newC;
                    break;
                }
            }
        }
        
        //Return the name with the first character in uppercase.
        return name.substring(0,1).toUpperCase() + name.substring(1);
    }
    
    public String makeName() {
        char a, b;
        
        //Select a reasonable first two letters for the name.
        do {
            a = (char)frequencies.keySet().toArray()[rand.nextInt(frequencies.size())];
            b = (char)frequencies.get(a).keySet().toArray()[rand.nextInt(frequencies.get(a).size())];
            
            //Continue as long as the selected letters are two disallowed consonants.
        } while(!((isVowel(a) && isVowel(b)) || (isVowel(a) ^ isVowel(b)) || isCommonConsonantCombo("" + a + b)));
        
        return this.makeName("" + a + b);
    }
    
    private boolean isCommonConsonantCombo(String s) {
        for(String cc : COMMON_CONSONANT_COMBOS)
            if(cc.equals(s))
                return true;
        return false;
    }
    
    public boolean isVowel(char c) {
        for(char v : VOWELS)
            if(v == c)
                return true;
        return false;
    }
}
