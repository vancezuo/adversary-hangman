package engine;

/**
 * A Scrabble Word object is a Word, meaning that it represents a hangman word. 
 * This word is chosen randomly, but weighted towards words with high
 * Scrabble(tm) scores -- in other words, with higher chance of rare letters
 * appearing. Specifically, a sample of words are randomly selected from its
 * Dict, and only the highest scoring one retained.
 * 
 * @author Vance Zuo
 * Created: May 12, 2013
 *
 */
public class ScrabbleWord extends Word {
	
	private static final int SAMPLE_SIZE = 10;
	private static final int[] LETTER_VALUES = { 1, 3, 3, 2, 1, 4, 2, 
		4, 1, 8, 5, 1, 3, 1, 1, 3, 10, 1, 1, 1, 1, 4, 4, 8, 4, 10
	}; // a = 0, z = 25
	
	// Instance Fields
	private Word chosen;
	
	// Constructors
	/**
	 * Creates a new Scrabble Word object using a given Dict and word length.
	 * The ScrabbleWord represents one word from the Dict.
	 * @param dict       The Dict of possible words to choose from.
	 * @param wordLength The desired word length.
	 */
	public ScrabbleWord(Dict dict, int wordLength) {
		super(dict, wordLength);
		chosen = new RandomWord(dict, wordLength);
		// Technically there is a chance the same word(s) will be randomly
		// chosen multiple times, but for large enough lists this should 
		// not be a real worry.
		for (int i = 1; i < SAMPLE_SIZE; i++) {
			Word candidate = new RandomWord(dict, wordLength);
			if (getValue(candidate.toString()) > getValue(chosen.toString()))
				chosen = candidate;
		}
	}

	@Override
	public boolean hasLetter(char letter) {
		return chosen.hasLetter(letter);
	}

	@Override
	public int[] getLetterPositions(char letter) {
		return chosen.getLetterPositions(letter);
	}

	@Override
	public String toString() {
		return chosen.toString();
	}
	
	/**
	 * Calculates the Scrabble(tm) value of a word, without special effects
	 * such as double letter score.
	 * @param word A String representing the word. Assumed to consist only
	 *             of alphabetic characters.
	 * @return The score value of the word if it contains only alphabetic 
	 *         characters, else an undefined return value.
	 */
	public static int getValue(String word) { 
		word = word.toUpperCase();
		int totalValue = 0;
		for (char c : word.toCharArray()) {
			int offset = c - 'A'; // Calculates look-up index
			totalValue += LETTER_VALUES[offset];
		}
		return totalValue; 
	}

}
