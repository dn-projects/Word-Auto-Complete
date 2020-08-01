package DSACW2;

import java.io.*;
import java.util.*;

/**
 *
 * @author ajb
 */
public class DictionaryFinder
{
    TreeMap<String, Integer> dictionary;

    public DictionaryFinder(ArrayList<String> words)
    {
        formDictionary(words);
    }

    /**
     * Reads all the words in a comma separated text document into an Array
     *
     * @param file - name of a file
     */
    public static ArrayList<String> readWordsFromCSV(String file) throws FileNotFoundException
    {
        Scanner sc = new Scanner(new File(file));
        sc.useDelimiter(" |,");
        ArrayList<String> words = new ArrayList<>();
        String str;

        while(sc.hasNext())
        {
            str=sc.next();
            str=str.trim();
            str=str.toLowerCase();
            words.add(str);
        }
        return words;
    }

    /**
     * Reads a file where the file contains words with every word written on a
     * new line. The method parses the file and for every word found adds it to
     * an element in arrayList and then the method returns the arrayList.
     *
     * @param fileName - name of a file
     * @return arrayList - populated with a word in each element
     */
    static ArrayList<String> loadPrefixes(String fileName)
    {
        ArrayList<String> list = new ArrayList<>();
        try
        {
            File file = new File(fileName);
            Scanner scanner = new Scanner(file);
            String str;
            while (scanner.hasNext())
            {
                str = scanner.next();
                str = str.trim();
                str = str.toLowerCase();
                list.add(str);
            }
            scanner.close();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        return list;
    }

    public static void saveCollectionToFile(Collection<?> c,String file) throws IOException
    {
        FileWriter fileWriter = new FileWriter(file);
        PrintWriter printWriter = new PrintWriter(fileWriter);

        for(Object w : c)
        {
            printWriter.println(w.toString());
        }
        printWriter.close();
    }

    /**
     * Generates a dictionary out of an ArrayList of words with the words
     * being mapped as the keys and the frequency of each word being mapped as
     * the values and sorts the words in alphabetical order
     *
     * @param  words - an ArrayList<String> of words
     * @author Dovydas Novikovas
     */
    public void formDictionary(ArrayList<String> words)
    {
        // TreeMap used to order words alphabetically
        dictionary = new TreeMap<>();

        for (String word : words)
        {
            if (dictionary.containsKey(word))
            {
                dictionary.replace(word, dictionary.get(word)+1);
            }
            else
            {
                dictionary.put(word,1);
            }
        }
    }

    /**
     * Writes out a stream of Keys and Values in a TreeMap to a file called
     * "Dictionary.csv" with each key/value pair being separated on a new line
     *
     * @throws FileNotFoundException
     * @author Dovydas Novikovas
     */
    public void saveToFile() throws FileNotFoundException
    {
        PrintStream writeOut = new PrintStream("Dictionary.csv");

        // toString Overridden with StringBuilder to format output neatly
        writeOut.print(this.toString());

        writeOut.close();
    }

    /**
     * Overrides the toString in class DictionaryFinder to format the ArrayList
     * to return a toString representation of a StringBuilder used to output
     * a key and a value separated with a comma delimiter in a TreeMap
     *
     * @return a string representation of a StringBuilder used to format output
     * @author Dovydas Novikovas
     */
    @Override
    public String toString()
    {
        StringBuilder stringBuilder = new StringBuilder();

        for (Object key : dictionary.keySet())
        {
            // using string builder to format output as: "Word, Word Frequency"
            stringBuilder.append(key).append(",").append(dictionary.get(key));
            stringBuilder.append("\n");
        }

        return stringBuilder.toString();
    }


    // main used a test harness
    public static void main(String[] args) throws Exception
    {

        DictionaryFinder df = new DictionaryFinder(readWordsFromCSV("/Users/david/ /dsacoursework2/testDocument.csv"));

        // testing formDictionary method is working correctly
        df.formDictionary(readWordsFromCSV("/Users/david/ /dsacoursework2/testDocument.csv"));

        // printing out the dictionary to test if it was generated correctly
        System.out.println(df);

        // testing if a file is successfully created when method is called
        df.saveToFile();
    }

}