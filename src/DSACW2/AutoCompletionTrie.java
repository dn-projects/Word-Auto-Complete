/*******************************************************************************
 File        : AutoCompletionTrie.java

 Description : The class expands on Trie class. The class features additional
               variables and methods that are used in autoCompletion. The class
               introduces the new variable TrieWordCount, which is used to
               store the number of words in a Trie. The class also implements
               populateTrie that adds words from a list to the trie. A method
               returnCount returns the number of times a string occurs in a
               trie. getSubTrie has also been adapted to set the trie word
               count and set the root to null. Finally a toString method has
               been added to print all the words in the trie.

 Author      : Dovydas Novikovas

 Date        : Wednesday 15th July 2020
 *******************************************************************************/
package DSACW2;

import java.util.*;

public class AutoCompletionTrie
{
    // store count of each word to calculate probability
    private int TrieWordCount = 0;
    private AutoCompletionTrieNode root;

    public AutoCompletionTrie()
    {
        root = new AutoCompletionTrieNode();
    }

    /**
     * @return the count of total words in this AutoCompletionTrie
     */
    public int getTrieWordCount()
    {
        return TrieWordCount;
    }

    public AutoCompletionTrie(AutoCompletionTrieNode node)
    {
        this.root = node;
    }

    /**
     * Traverses through each char in the string passed in and assigns a
     * new TrieNode for each char. The offspring of each char is set to the
     * next char in the string passed in. The TrieWordCount is incremented
     * whether the word is already in the Trie or not. The method returns
     * true if the string was successfully added to the AutoCompletionTrie,
     * false otherwise.
     *
     * @param key - string to add to the AutoCompletionTrie
     * @return true if the string is added to the AutoCompletionTrie; false otherwise
     * @throws IllegalArgumentException
     */
    public boolean add(String key) throws IllegalArgumentException
    {
        AutoCompletionTrieNode temp = root;

        // loops through the string passed in on each char in the string
        for (int i = 0; i < key.length(); i++)
        {
            AutoCompletionTrieNode next = temp.getOffspring(key.charAt(i));

            // if offspring at char pos is not set
            if (next == null)
            {
                temp.setOffspring(key.charAt(i));
            }

            temp = temp.getOffspring(key.charAt(i));
            if (i == key.length() - 1 && temp.getIsWord())
            {
                TrieWordCount++;
                temp.incrementCount();
                return false;
            }
        }
        TrieWordCount++;
        // will only set leaf to true to indicate a complete word
        temp.setIsWord(true);
        temp.incrementCount();
        return true;
    }

    /**
     * Adds a list of words to the trie
     *
     * @param listOfWords words to be added
     */
    public void populateTrie(List<String> listOfWords)
    {
        for (String word : listOfWords)
        {
            add(word);
        }
    }

    /**
     * Method to return the count of how many times the given string is in
     * the file. Calculated using the AutoCompletionTrieNodes count value
     *
     * @param str String to look for
     * @return Count of string
     */
    public int returnCount(String str)
    {
        AutoCompletionTrieNode temp = this.root;

        // checks every char in the string
        for (char c : str.toCharArray())
        {
            AutoCompletionTrieNode next = temp.getOffspring(c);

            if (next == null)
            {
                return temp.getCount();
            }
            temp = next;
        }
        return temp.getCount();
    }


    /**
     * Creates a new Trie with the prefix parameter as the root of the
     * AutoCompletionTrie with the content, isWord and offspring matching the
     * AutoCompletionTrie the method was called on.
     *
     * @param prefix - String to set as the root of the new Trie
     * @return Trie  - New AutoCompletionTrie with the contents, isWord and offspring set
     */
    public AutoCompletionTrie getSubTrie(String prefix)
    {
        if (root == null)
        {
            return null;
        }

        int wordCount = 0;
        AutoCompletionTrieNode temp = this.root;

        for (char c : prefix.toCharArray())
        {
            if (temp.getIsWord())
            {
                wordCount += temp.getCount();
            }
            AutoCompletionTrieNode next = temp.getOffspring(c);

            if (next == null)
            {
                return null;
            }
            temp = next;
        }
        AutoCompletionTrie trie = new AutoCompletionTrie(temp);
        trie.TrieWordCount = wordCount;
        trie.root.setPrefix('\0');
        return trie;
    }

    /**
     * Generates a string that is made up of all the words in the Trie. Each
     * word is separated by a comma delimiter. The private method
     * 'getAllWords' is called in order to retrieve a list of words that can
     * be iterated through.
     *
     * @return string of all the words in the AutoCompletionTrie separated by a comma
     */
    public List<String> getAllWords()
    {
        return getAllWords(root, "");
    }

    /**
     * Iterates through the AutoCompletionTrie and generates a list of words that are found
     * within the Trie.
     *
     * @param node - AutoCompletionTrieTrieNode from which to begin the traversal from
     * @return List, with each element a word found in the AutoCompletionTrie
     */
    private List<String> getAllWords(AutoCompletionTrieNode node, String in)
    {
        List<String> allWords = new LinkedList<>();
        StringBuilder sb = new StringBuilder();
        Stack<AutoCompletionTrieNode> stack = new Stack<>();

        sb.append(in).append(node.getPrefix());

        for (AutoCompletionTrieNode trieNode : node.getOffspringArray())
        {
            if (trieNode != null)
            {
                stack.push(trieNode);
            }
        }

        while (!stack.empty())
        {
            allWords.addAll(getAllWords(stack.pop(), sb.toString()));
        }
        if (node.getIsWord())
        {
            allWords.add(sb.toString().trim());
        }
        return allWords;
    }

    /**
     * @return String output of all words in this AutoCompletionTrie
     */
    @Override
    public String toString()
    {
        StringBuilder stringBuilder = new StringBuilder();
        for (String word : getAllWords())
        {
            stringBuilder.append(word + "\n");
        }
        return stringBuilder.toString();
    }


    public static void main(String[] args) throws Exception
    {
        AutoCompletionTrie autoComplete = new AutoCompletionTrie();

        ArrayList<String> testQueries = DictionaryFinder.loadPrefixes("lotrQueries.csv");
        ArrayList<String> in = DictionaryFinder.readWordsFromCSV("lotr.csv");

        autoComplete.populateTrie(in);
        System.out.println("All words in autoComplete: " + autoComplete.getAllWords());
        System.out.println(autoComplete.getAllWords());
    }
}