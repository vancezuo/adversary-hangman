package engine;

import java.util.ArrayList;

/**
 * A Random Word object is a Word, meaning that it represents a hangman word. 
 * This word is randomly selected from its Dict.
 * 
 * @author Vance Zuo
 * Created: May 12, 2013
 *
 */
class RandomWord extends Word {
	
	// Instance Fields
	private String word;
	
	// Constructors
	/**
	 * Creates a new Random Word object using a given Dict and word length.
	 * The RandomWord represents one word from the Dict.
	 * @param dict       The Dict of possible words to choose from.
	 * @param wordLength The desired word length.
	 */
	public RandomWord(Dict dict, int wordLength) {
		super(dict, wordLength);
		word = dict.getRandom(wordLength);
	}
	
	// Public Methods
	@Override
	public boolean hasLetter(char letter) {
		return word.indexOf(letter) != -1;
	}

	@Override
	public int[] getLetterPositions(char letter) {
		letter = Character.toLowerCase(letter);
		int first = word.indexOf(letter);
		if (first == -1) // Letter not in word
			return null;
		// ArrayList used initially as the number of occurances is not known
		ArrayList<Integer> list = new ArrayList<Integer>();
		for (int i = first; i >= 0; i = word.indexOf(letter, i + 1)) {
		    list.add(i);
		}
		// Convert ArrayList to array
		int[] array = new int[list.size()];
		for (int i = 0; i < array.length; i++) {
			array[i] = list.get(i).intValue();
		}
		return array;
	}
	
	@Override
	public String toString() {
		return word;
	}
}
