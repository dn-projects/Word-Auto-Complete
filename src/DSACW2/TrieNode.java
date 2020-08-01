/*******************************************************************************
 File        : TrieNode.java

 Description : This class was implemented to represent a node in a Trie data
               structure. The class stores a prefix, a boolean isWord which
               is a flag to indicate if a node marks the end of word and an
               array off TrieNodes as the offspring of this TrieNode.

 Author      : Dovydas Novikovas

 Date        : Wednesday 15th July 2020

*******************************************************************************/
package DSACW2;

public class TrieNode
{
    private char prefix;
    private boolean isWord;
    private TrieNode[] offspring = new TrieNode[26];

    public TrieNode()
    {

    }

    public TrieNode(char prefix)
    {
        this.prefix = prefix;
    }

    public TrieNode(char prefix, boolean isWord)
    {
        this.prefix = prefix;
        this.isWord  = isWord;
    }

    public TrieNode[] getOffspringArray()
    {
        return this.offspring;
    }

    public void setPrefix(char c)
    {
        this.prefix = c;
    }

    public void setIsWord(boolean isWord)
    {
        this.isWord = isWord;
    }

    public char getPrefix()
    {
        return this.prefix;
    }

    public boolean getIsWord()
    {
        return this.isWord;
    }

    /**
     * Prints all the the information a TrieNode can store. (primarily used
     * for debugging)
     */
    public void printTrieNode()
    {
        System.out.println
        (
            "content = " + this.prefix + "\n" +
            "isWord = " + this.isWord
        );
        printOffspring();
    }

    /**
     * Loops over each element in a TrieNode's offspring array and prints
     * each element
     */
    public void printOffspring()
    {
        for (int i = 0; i <= 25 ; i++)
        {
            if(offspring[i] == null)
            {
                System.out.println("offspring at pos: " + i + " = null");
            }
            else
            {
                System.out.println
                (
                    "offspring at pos: " + i + " = " + offspring[i].getPrefix()
                );
            }
        }
    }

    /**
     * Check's a TrieNode's offspring at the ascii value of the parameter
     * 'x' if null will set the offspring at 'x' and return true
     * otherwise will print error message and return false
     *
     * @param  x - a letter to add to the offspring of the TrieNode
     * @return true if TrieNode offspring prefix has been set; false otherwise
     * @throws IllegalArgumentException
     * @author Dovydas Novikovas
     */
    public boolean setOffspring(char x) throws IllegalArgumentException
    {
        if(getOffspring(x) == null)
        {
            // set prefix of offspring at int pos of x
            this.offspring[((int) x - 97)] = new TrieNode(x);
            return true;
        }
        System.out.println("[" + x + "] already exists in offspring!");
        return false;
    }

    /**
     * Retrieves a TrieNode at an element position equivalent of the ascii
     * value of x in an array of offspring will print out error messages if
     * a NON lower case A-Z letter is passed in
     *
     * @param  x - a letter
     * @return TrieNode in an array of offspring
     * @throws IllegalArgumentException
     * @author Dovydas Novikovas
     */
    public TrieNode getOffspring(char x) throws IllegalArgumentException
    {
        // used to get ascii range between 0-25
        int intX = ((int) x - 97);

        // checking if lowercase letter
        if(intX >= 0 && intX <= 25)
        {
            return this.offspring[intX];
        }
        throw new IllegalArgumentException("Enter a lowercase A-Z character only!");
    }



    // main used a test harness
    public static void main(String[] args) throws Exception
    {
        TrieNode t1 = new TrieNode('a', false);

        TrieNode t2 = new TrieNode();

        //char space;

        // prints all the info in a TrieNode (content, isWord, offspring)
        t1.printTrieNode();
        System.out.println();

        // adds offspring to a TrieNode
        t1.setOffspring('c');
        t1.setOffspring('f');
        t1.setOffspring('t');
        t1.setOffspring('a');
        t1.setOffspring('a');
        t1.setOffspring('s');
        System.out.println();

        // testing if you can add invalid argument to TrieNode
        try
        {
            t1.setOffspring('4');
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
        System.out.println();

        // prints all the offspring of a TrieNode
        t1.printOffspring();
    }


}
