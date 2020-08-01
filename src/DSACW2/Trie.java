/*******************************************************************************
 File        : Trie.java

 Description : This class is made to mimic a trie data structure. It uses the
               TrieNode class to represent nodes in the trie. The root is set
               to a TrieNode. The class contains the following methods add,
               contains, outputBreadthFirstSearch, outputDepthFirstSearch,
               getSubTrie, getAllWords.

 Author      : Dovydas Novikovas

 Date        : Wednesday 15th July 2020

*******************************************************************************/
package DSACW2;

import java.util.*;

public class Trie
{
    private TrieNode root;

    public Trie()
    {
        root = new TrieNode();
    }

    public Trie(TrieNode node)
    {
        this.root = node;
    }

    public TrieNode getTrieNode()
    {
        return root;
    }


    /**
     * Traverses through each char in the string passed in and assigns a
     * new TrieNode for each char. The offspring of each char is set to the
     * next char in the string passed in. The method will return true if the
     * string was successfully added to the Trie, false otherwise.
     *
     * @param  key - string to add to the Trie
     * @return true if the string is added to the Trie; false otherwise
     * @throws IllegalArgumentException
     */
    public boolean add(String key) throws IllegalArgumentException
    {
        TrieNode temp = root;

        // loops through the string passed in on each char in the string
        for (int i = 0; i < key.length() ; i++)
        {
            TrieNode next = temp.getOffspring(key.charAt(i));

            // if offspring at char pos is not set
            if(next == null)
            {
                temp.setOffspring(key.charAt(i));
            }

            temp = temp.getOffspring(key.charAt(i));
            if(i == key.length()-1 && temp.getIsWord())
            {
                return false;
            }
        }
        // only leaf node's 'setIsWord' is set to true
        temp.setIsWord(true);
        return true;
    }

    /**
     * Traverses through the Trie and checks if the string passed in the
     * argument occurs in the Trie. If a complete word is found in the Trie
     * true is returned.
     *
     * @param  key - string to search for
     * @return true if Trie contains key passed in; false otherwise
     */
    public boolean contains(String key)
    {
        TrieNode temp = this.root;

        // checks every char in the string
        for (char c : key.toCharArray())
        {
            TrieNode next = temp.getOffspring(c);

            if(next == null)
            {
                return false;
            }
            temp = next;
        }
        return temp.getIsWord();
    }

    /**
     * Traverses the Trie in a 'Breadth First Search' traversal and returns a
     * string of the content in each node.
     *
     * @return string representation of the Trie after a "bfs" traversal
     */
    public String outputBreadthFirstSearch()
    {
        // checks if Trie is empty
        if(this.root == null)
        {
            return null;
        }

        // used to build the output string
        StringBuilder string = new StringBuilder();

        // used as separate data structure to hold temp visited nodes
        Queue<TrieNode> queue = new LinkedList<TrieNode>();

        queue.add(this.root);

        while(!(queue.isEmpty()))
        {
            TrieNode temp = queue.remove();

            // checks every node in the offspring
            for (TrieNode node : temp.getOffspringArray())
            {
                // if offspring node is not empty
                if(!(node == null))
                {
                    queue.add(node);
                    string.append(node.getPrefix());
                }
            }

        }
        return string.toString();
    }

    /**
     * Calls the private outputDepthFirstSearch method within Trie class to
     * retrieve a depth first search traversal of the Trie, passing the
     * method the parameters of this Trie and a new string builder to
     * out the return string.
     *
     * @return string representation of the Trie after a "dfs" traversal
     */
    public String outputDepthFirstSearch()
    {
        // calls private outputDepthFirstSearch method within Trie class
        return outputDepthFirstSearch(this.root, new StringBuilder());
    }

    /**
     * Traverses the Trie in a recursive 'Depth First Search' traversal and
     * returns string of the content in each node.
     *
     * @param  node - Trie node to begin traversal from
     * @param  sb   - String builder to be used to form the output string
     * @return string representation of the Trie after a "dfs" traversal
     */
    private String outputDepthFirstSearch(TrieNode node, StringBuilder sb)
    {
        // adds content in root
        sb.append(node.getPrefix());

        // for all the offspring that are not empty, call method recursively
        for (TrieNode n : node.getOffspringArray())
        {
            if(!(n == null))
            {
                // calls method recursively until leaf node is found
                outputDepthFirstSearch(n, sb);
            }
        }
        return sb.substring(1);
    }

    /**
     * Creates a new Trie with the prefix parameter as the root of the Trie
     * with the content, isWord and offspring matching the Trie the method
     * was called on.
     *
     * @param prefix - String to set as the root of the new Trie
     * @return Trie  - New Trie with the prefix's, isWord and offspring set
     */
    public Trie getSubTrie(String prefix)
    {
        if (root == null) {
            return null;
        }

        TrieNode temp = this.root;
        
        for (char c : prefix.toCharArray())
        {
            TrieNode next = temp.getOffspring(c);
            
            if (next == null)
            {
                return null;
            }
            temp = next;
        }
        Trie trie = new Trie(temp);
        trie.root.setPrefix('\0');
        return trie;
    }

    /**
     * Returns a list by making a function call to getAllWords passing in the
     * root node of this trie and an empty string.
     *
     * @return List - all the words currently stored in the trie
     */
    public List<String> getAllWords()
    {
        return getAllWords(this.root, "");
    }

    /**
     * The method generates a list and populates every element with a word
     * found in the tire. The method works recursively implementing a stack to
     * store the prefixes found to then later append them to the string builder.
     *
     * @param trieNode - TrieNode from which to begin the traversal from
     * @param str - string of all the characters traversed so far
     * @return List - each element stores a word found in the trie
     */
    private List<String> getAllWords(TrieNode trieNode, String str)
    {
        List<String> words = new LinkedList<>();
        StringBuilder stringBuilder = new StringBuilder();
        Stack<TrieNode> stack = new Stack<>();

        stringBuilder.append(str).append(trieNode.getPrefix());

        for (TrieNode node : trieNode.getOffspringArray())
        {
            if (node != null)
            {
                stack.push(trieNode);
            }
        }
        while (!stack.empty())
        {
            words.addAll(getAllWords(stack.pop(), stringBuilder.toString()));
        }
        if (trieNode.getIsWord())
        {
            words.add(stringBuilder.toString().trim());
        }
        return words;
    }



    /**
     * @return String output of all words in this trie
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


    // main used a test harness
    public static void main(String[] args) throws Exception
    {
        // testing to see if Trie can be instantiated correctly
        Trie t1 = new Trie();

        // testing if string can be added to Trie correctly
        t1.add("cheers");
        t1.add("cheese");
        t1.add("chat");
        t1.add("cat");
        t1.add("bat");

        // testing Trie contains method
        if(t1.contains("ch"))
        {
            System.out.println("trie contains word");
        }
        else
        {
            System.out.println("trie does not contain word");
        }

        // testing Trie contains method
        if(t1.contains("cheese"))
        {
            System.out.println("trie contains word");
        }
        else
        {
            System.out.println("trie does not contain word");
        }

        // testing the breadth first search method to see it prints correctly
        System.out.println("BFS Trie: " + t1.outputBreadthFirstSearch());

        // testing the depth first search method to see it prints correctly
        System.out.println("DFS Trie: " + t1.outputDepthFirstSearch() + "\n");

        // testing the subTrie method
        Trie t2 = t1.getSubTrie("ch");
        System.out.print("SubTrie testing: " + t2.outputDepthFirstSearch());

        System.out.println("\nAll words: " + t1.getAllWords());
    }

}
