/*******************************************************************************
 File        : AutoCompletion.java

 Description : AutoCompletion provides functionality that reads queries from
               lotrQueries and retrieves a list of results for the suggested
               words for each query. The class implements the method
               getTopThreeWordsAndProbabilities which retrieves the top three
               results for a query. The class also has the methods to print the
               results to a file and the console. Finally getLotrProbabilities
               is implemented to run the functionality of the class.

 Author      : Dovydas Novikovas

 Date        : Wednesday 15th July 2020
 *******************************************************************************/
package DSACW2;

import javafx.util.Pair;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.util.*;

public class AutoCompletion
{
    //Decimal format to round the probabilities to 4 d.p
    public static DecimalFormat decimalFormat = new DecimalFormat("#.####");

    /**
     * Method to get the top three suggested words from from an
     * AutoCompletionTrie that has the prefix given and their probability.
     * The list returned is sorted with the highest probable word first,
     * based on the AutoCompletionTrieNode count. If there are two equal
     * probabilities then the word with the least characters is put first.
     *
     * @param trie
     * @param query
     * @return A linkedlist of the top three words and their probabilities
     */
    public static List<Pair<String, Double>> getTopThreeWordsAndProbabilities
    (AutoCompletionTrie trie, String query)
    {
        //Linked list used due to being more efficient when adding elements
        // than an array list
        List<Pair<String, Double>> counts = new LinkedList<>();
        double allWordCounts = 0;


        for (String word : trie.getAllWords())
        {
            allWordCounts += trie.returnCount(word);
        }

        //for each word in the trie, add it to the list with its frequency
        // divided by the total of words in the AutoCompletionTrie to get the
        // probability.
        for (String word : trie.getAllWords())
        {
            counts.add(new Pair<>(query + word,
                    trie.returnCount(word) / allWordCounts));
        }

        //Sort the count list based on highest probability, and if the same
        // the shortest word is added
        counts.sort((o1, o2) ->
        {
            if (o1.getValue().equals(o2.getValue()))
            {
                return o1.getKey().length() - o2.getKey().length();
            }
            if (o2.getValue() > o1.getValue())
            {
                return 1;
            }
            return -1;
        });


        List<Pair<String, Double>> listToReturn = new LinkedList<>();
        //Only add the top three words to list to return
        for (Pair<String, Double> pair : counts)
        {
            if (listToReturn.size() < 3)
            {
                listToReturn.add(pair);
            }
        }
        return listToReturn;

    }

    /**
     * Method to print the top three probabilities list to the console in the
     * correct format with the probability rounded to 4 d.p
     *
     * @param list list to be printed
     */
    public static void printToConsoleFormatted(List<Pair<String, Double>> list)
    {
        for (Pair<String, Double> pair : list)
        {
            System.out.println(pair.getKey() + " (probability " +
                    AutoCompletion.decimalFormat.format(pair.getValue()) + ")");
        }
    }

    /**
     * Method to output top three probabilities to file in the desired format.
     * The prefix is outputted, the suggested word and then its probability
     *
     * @param list  list of top three probabilities to output to file
     * @param query the prefix that was search for in the AutoCompletionTrie
     * @throws FileNotFoundException
     */
    public static void outputToFile(List<Pair<String, Double>> list,
                                    String query, String fileName) throws FileNotFoundException
    {

        PrintStream stream = new PrintStream(new FileOutputStream(
                fileName, true));

        int i = 0;
        if (!list.isEmpty())
        {
            stream.append(query).append(",");
        }

        for (Pair<String, Double> word : list)
        {
            //exit if three words have been processed
            if (i == 3)
            {
                break;
            }
            stream.append(word.getKey()).append(",");
            stream.append(AutoCompletion.decimalFormat.format(word.getValue()));
            //append a comma for all but the last word in the list
            if (i != 2)
            {
                if (i + 1 < list.size())
                {
                    stream.append(",");
                }
            }
            i++;
            stream.flush();
        }
        stream.println();
        stream.close();
    }

    /**
     * Method reads in the lotrQueries.csv, separated them into an array
     * listing using the DictionaryFinder method.
     * @throws FileNotFoundException
     */
    public static void getLotrProbabilities() throws FileNotFoundException
    {
        AutoCompletionTrie autoComplete = new AutoCompletionTrie();
        //Get all prefixes
        ArrayList<String> lotrQueries = DictionaryFinder.loadPrefixes("lotrQueries.csv");
        //Get all words
        ArrayList<String> in = DictionaryFinder.readWordsFromCSV("lotr.csv");
        //Create Trie with words
        autoComplete.populateTrie(in);

        for (String query : lotrQueries)
        {
            AutoCompletionTrie subTrie = autoComplete.getSubTrie(query);

            if (subTrie != null)
            {
                List<Pair<String, Double>> list = getTopThreeWordsAndProbabilities(subTrie, query);
                printToConsoleFormatted(list);
                outputToFile( list, query,"lotrMatches.csv");
            }
        }
    }

    public static void main(String[] args) throws FileNotFoundException
    {
        getLotrProbabilities();
    }
}
