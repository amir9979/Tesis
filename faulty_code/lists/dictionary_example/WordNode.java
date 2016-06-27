package lists.dictionary_example;

/**
 * 
 * @author joako
 *	Class taken from: 'http://stackoverflow.com/questions/4530706/implementing-a-linked-list-java'
 */
public class WordNode {
	private String word;
	private int freq;
	private WordNode next;

	/**
	 * Constructor for objects of class WordNode
	 */
	public WordNode(String word, WordNode next )
	{
		this.word = word;
		this.next = next;
		freq = 1;

	}
	/**
	 * Constructor for objects of class WordNode
	 */
	public WordNode(String word)
	{
		this(word, null);
	}
	/**
	 * 
	 */
	public String getWord()
	{
		return word;
	}
	/**
	 * 
	 */
	public int getFreq(String word)
	{
		return freq;
	}
	/**
	 * 
	 */
	public WordNode getNext()
	{
		return next;
	}
	/**
	 * 
	 */
	public void setNext(WordNode n)
	{
		next = n;
	}
	/**
	 * 
	 */
	public void increment()
	{
		freq++;


	}
}
