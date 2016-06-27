package lists.dictionary_example;

/**
 * 
 * @author joako
 * Class taken from: 'http://stackoverflow.com/questions/4530706/implementing-a-linked-list-java'
 * 
 */

public class Dictionary {

	private WordNode Link;
	private int size;

	/**
	 * Constructor for objects of class Dictionary
	 */
	public Dictionary(String L)
	{
		Link = new WordNode(L);
		size = 1;
	}

	/**
	 * Return true if the list is empty, otherwise false
	 * 
	 * 
	 * @return 
	 */
	public boolean isEmpty()
	{
		return Link == null;

	}
	/**
	 * Return the length of the list
	 * 
	 * 
	 * @return 
	 */
	public int getSize()
	{
		return size;
	}
	/** 
	 * Add a word to the list if it isn't already present. Otherwise 
	 * increase the frequency of the occurrence of the word. 
	 * @param word The word to add to the dictionary. 
	 */
	public void add(String word)
	{

		Link.setNext(new WordNode(word, Link.getNext()) );
		size++;
	}
}
