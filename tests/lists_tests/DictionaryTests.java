package lists_tests;
import static org.junit.Assert.*;
import lists.dictionary_example.Dictionary;

import org.junit.Test;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

@RunWith(Theories.class)
public class DictionaryTests {

	@Theory
	public void addTheory() {
		Dictionary d = new Dictionary("");
		d.add("a");
		d.add("a");
		d.add("b");
		assertTrue(!d.isEmpty());
		assertTrue(d.getSize() == 4);
	}
	
}
