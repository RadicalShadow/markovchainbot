import java.util.*;
import java.io.File;
import java.util.concurrent.ThreadLocalRandom;

class Main {
    public static void main(String[] args) {
        final int PREFIX_SIZE = 2;
        HashMap<String, ArrayList<String>> prefixSuffix = new HashMap<String, ArrayList<String>>();
        String frankenstien = readText("C:\\Users\\emily\\IdeaProjects\\markov_chains\\src\\inputs\\Frankenstein.txt");
        String holmes = readText("C:\\Users\\emily\\IdeaProjects\\markov_chains\\src\\inputs\\study-in-scarlet.txt");
        String trumpSpeech = readText("C:\\Users\\emily\\IdeaProjects\\markov_chains\\src\\inputs\\TrumpSpeech.txt");
        String communism = readText("C:\\Users\\emily\\IdeaProjects\\markov_chains\\src\\inputs\\CommunistManifesto.txt");
        String alice = readText("C:\\Users\\emily\\IdeaProjects\\markov_chains\\src\\inputs\\AliceInWonderland.txt");
        System.out.print(toString(useProbability(makeProbability(makeAChain(alice, prefixSuffix, PREFIX_SIZE)), makeAChain(alice, prefixSuffix, PREFIX_SIZE))));
    }

    public static HashMap<String, ArrayList<String>> makeAChain(String fileText, HashMap<String, ArrayList<String>> prefixSuffix, int PREFIX_SIZE) {
        String[] textArray = fileText.split(" ");
        String prefix = "";
        String[] convertToString;
        for (int i = 0; i < textArray.length - PREFIX_SIZE - 1; i++) {
            prefix = "";
            convertToString = Arrays.copyOfRange(textArray, i, i + PREFIX_SIZE - 1);
            for (int j = 0; j < PREFIX_SIZE && j < convertToString.length; j++) {
                prefix += convertToString[j] + " ";
            }
            if (!prefixSuffix.containsKey(prefix)) {
                ArrayList<String> suffix = new ArrayList<String>();
                suffix.add(textArray[i + PREFIX_SIZE]);
                prefixSuffix.put(prefix, suffix);
            } else {
                prefixSuffix.get(prefix).add(textArray[i + PREFIX_SIZE]);
            }
        }
        return prefixSuffix;
    }

    public static String toString(ArrayList<String> toPrint) {
        String output = "";
        for (int i = 0; i < toPrint.size(); i++) {
            output = output + toPrint.get(i) + " ";
        }
        return output;
    }

    public static HashMap<String, Integer> makeProbability(HashMap<String, ArrayList<String>> prefixSuffix) {
        HashMap<String, Integer> wordProbs = new HashMap<String, Integer>();
        Iterator iterator = wordProbs.entrySet().iterator();
        int occurance = 1;
        ArrayList<String> tempList;
        while (iterator.hasNext()) {
            HashMap.Entry suffix = (HashMap.Entry) iterator.next();
            tempList = (ArrayList<String>) suffix.getValue();
            for (int i = 0; i < tempList.size(); i++) {
                if (wordProbs.containsKey(tempList.get(i)))
                    occurance++;
                wordProbs.put(tempList.get(i), occurance);
                iterator.remove();
            }
        }
        Iterator denSet = wordProbs.entrySet().iterator();
        int numerator = 0;
        int denominator = 0;
        while (denSet.hasNext()) {
            HashMap.Entry suffix = (HashMap.Entry) iterator.next();
            denominator += wordProbs.get(suffix.getValue());
            denSet.remove();
        }
        Iterator numSet = wordProbs.entrySet().iterator();
        while (numSet.hasNext()) {
            HashMap.Entry suffix = (HashMap.Entry) iterator.next();
            numerator = wordProbs.get(suffix.getValue());
            wordProbs.replace((String) suffix.getKey(), ((numerator / denominator) / 100));
            numSet.remove();
        }
        return wordProbs;
    }

    public static <K, V> K getKey(HashMap<K, V> map, V value) {
        for (K key : map.keySet()) {
            if (value.equals(map.get(key))) {
                return key;
            }
        }
        return null;
    }

    public static ArrayList<String> useProbability(HashMap<String, Integer> probability, HashMap<String, ArrayList<String>> prefixSuffix) {
        ArrayList<String> result = new ArrayList<String>();
        ArrayList<String> tempList = new ArrayList<String>();
        Iterator iterator = prefixSuffix.entrySet().iterator();
        HashMap<ArrayList<String>, String> suffixPrefix = new HashMap<ArrayList<String>, String>();
        for (HashMap.Entry<String, ArrayList<String>> entry : prefixSuffix.entrySet()) {
            suffixPrefix.put(entry.getValue(), entry.getKey());
        }
        while (iterator.hasNext()) {
            HashMap.Entry suffix = (HashMap.Entry) iterator.next();
            tempList = (ArrayList<String>) suffix.getValue();
            for (int i = 0; i < tempList.size(); i++) {
                int j = ThreadLocalRandom.current().nextInt(0, tempList.size());
                result.add(suffixPrefix.get(getKey(suffixPrefix, tempList.get(j))));
                result.add(tempList.get(j));
            }
        }
        //Check for duplicated words or null values.
        for (int x = 0; x < result.size(); x++) {
            if (result.contains(null)) {
                try {
                    result.removeAll(null);
                    x--;
                    if(x == -1){
                        x = 0;
                    }
                } catch (Exception e) {
                    result.removeAll(Collections.singleton(null));
                }
            }
            if (result.get(x).equals(" ")) {
                try {
                    result.remove(x);
                    x--;
                    if(x == -1){
                        x = 0;
                    }
                } catch (NullPointerException npe) {
                    result.removeAll(Collections.singleton(null));
                }
            }
            if (x < result.size() - 1 && result.get(x).equals(result.get(x + 1))) {
                try {
                    result.remove(x);
                    x--;
                    if(x == -1){
                        x = 0;
                    }
                } catch (NullPointerException npe) {
                    result.removeAll(Collections.singleton(null));
                }
            }
            if (x < result.size() - 2 && result.get(x).equals(result.get(x + 2))) {
                try {
                    result.remove(x);
                    x--;
                    if(x == -1){
                        x = 0;
                    }
                } catch (NullPointerException npe) {
                    result.removeAll(Collections.singleton(null));
                }
            }
        }
        return result;
    }


    public static String readText(String fileName) {
        try {
            File file = new File(fileName);
            Scanner scanner = new Scanner(file);
            String current = "";
            while (scanner.hasNextLine()) {
                current += scanner.nextLine() + " ";
            }
            scanner.close();
            return current;
        } catch (Exception e) {
            return (e + " please check your input or code.");
        }
    }
}