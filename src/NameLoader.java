
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Drew Heenan
 */
public class NameLoader {
    
    private File nameFile;
    private List<String> referenceNames;
    private Map<Character, Map<Character, Double>> frequenciesMap;
    
    public NameLoader(String filePath) {
        this.nameFile = new File(filePath);
        try {
            loadNames();
        } catch(FileNotFoundException e) {
            e.printStackTrace();
            System.err.println("Failed to find specified file.");
        }
        calculateFrequencies();
        
    }
    
    public Map<Character, Map<Character, Double>> getFrequencies() {
        return frequenciesMap;
    }
    
    private void loadNames() throws FileNotFoundException {
        this.referenceNames = new ArrayList();
        BufferedReader reader = new BufferedReader(new FileReader(nameFile));
        
        //Starting with Ada, of course.
        String ln = "Ada";
        while(ln != null) {
            this.referenceNames.add(ln.toLowerCase());
            try {
                ln = reader.readLine();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    private void calculateFrequencies() {
        this.frequenciesMap = new HashMap();
        
        //Counts letter followups.
        for(String name : this.referenceNames) {
            for(int i = 0; i < name.length()-1; i++) {
                char a = name.charAt(i);
                char b = name.charAt(i+1);
                if(!frequenciesMap.containsKey(a)) {
                    frequenciesMap.put(a, new HashMap());
                    frequenciesMap.get(a).put(b, 1.0);
                } else if(!frequenciesMap.get(a).containsKey(b)) {
                    frequenciesMap.get(a).put(b, 1.0);
                } else {
                    Map<Character, Double> hsh = frequenciesMap.get(a);
                    hsh.put(b, hsh.get(b)+1);
                }
            }
        }
        
        //Normalize frequencies for each set of letters for each initial character.
        for(Map<Character, Double> hsh : frequenciesMap.values()) {
            double total = 0.0;
            for(Double d : hsh.values())
                total += d;
            for(Character c : hsh.keySet())
                hsh.put(c, hsh.get(c)/total);
        }
        
    }
    
}
