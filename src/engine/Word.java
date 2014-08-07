package engine;

/**
 * A Word object represents a word chosen for a hangman game. It contains 
 * methods for querying for letter existence and letter positions in the
 * word. 
 * <p>
 * Note a Word does not have to represent a single "word" at any
 * moment in time, but its return values to queries should always be
 * consistent with previous return values. 
 * 
 * @author Vance Zuo
 * Created: May 12, 2013
 *
 */
abstract class Word {
	
	// Constructors
	/**
	 * Creates a new Word object using a given Dict and word length to 
	 * choose a hangman word.
	 * @param dict       The Dict object to choose words from.
	 * @param wordLength The desired word length.
	 */
	public Word(Dict dict, int wordLength) {}
	
	// Public Methods
	/**
	 * Checks if the Word's word contains a given letter.
	 * @param letter The letter to check.
	 * @return True if the Word has the given letter, else false.
	 */
	public abstract boolean hasLetter(char letter);
	
	/**
	 * Obtains the positions of a given letter in the Word's word, if any.
	 * @param letter The letter to check.
	 * @return An int array containing string indexes corresponding to the 
	 *         letter's locations in the word, or null if none exists.
	 */
	public abstract int[] getLetterPositions(char letter);
	
	@Override
	public abstract String toString();
}
