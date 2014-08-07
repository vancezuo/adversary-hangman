package engine;

import java.util.LinkedList;
import java.util.Queue;

/**
 * An Adversary Word object is a Word, meaning that it represents a hangman
 * word. 
 * <p>
 * This word is not chosen when an Adversary Word is created; instead
 * the Adversary Word maintains a list of possible words from its Dict,
 * and when it is queried for letter existence or position, it eliminates
 * words that are inconsistent with its answer. It always answers in
 * a way as to keep its possible word list as large as possible.
 * <p>
 * The Adversary Word attempts to force the most guesses possible, but 
 * this is not necessarily optimal at forcing a player to lose in hangman 
 * since lives are only lost after <i>incorrect</i> guesses.
 * 
 * @author Vance Zuo
 * Created: May 12, 2013
 *
 */
class AdversaryWord extends Word {
	
	// Instance Fields
	private Queue<String> possibleWords;
	private int length;
	private String usedLetters;
	private char[] partialWord; // AdversaryWord must give answers consistent
	                            // with this partialWord
	private Word backup; 

	// Constructors
	/**
	 * Creates a new Adversary Word object using a given Dict and word length.
	 * @param dict       The Dict of possible words to choose from.
	 * @param wordLength The desired word length.
	 */
	public AdversaryWord(Dict dict, int wordLength) {
		super(dict, wordLength);
		possibleWords = new LinkedList<String>();
		for (String word : dict.getWordList(wordLength)) {
			// For simplicity, the AdversaryWord ignores words that contain
			// repeated letters. For small dictionaries or very long words,
			// it may ignore all the possible words; in this case, Adversary 
			// word reverts to a "backup" word chooser.
			if (isUnique(word))
				possibleWords.add(word);
		}
		length = wordLength;
		usedLetters = "";
		partialWord = new char[wordLength];
		backup = possibleWords.isEmpty() ? new RandomWord(dict, wordLength) 
										 : null; // null -> no backup needed
	}

	// Public Methods
	@Override
	public boolean hasLetter(char letter) {
		if (backup != null)
			return backup.hasLetter(letter);
		if (!hasUsed(letter))
			processLetter(letter); // Updates usedLetters and partialWord
		return getIndexOf(partialWord, letter) != null;
	}


	@Override
	public int[] getLetterPositions(char letter) {
		if (backup != null)
			return backup.getLetterPositions(letter);
		if (!hasUsed(letter))
			processLetter(letter); // Updates usedLetters and partialWord
		return getIndexOf(partialWord, letter);
	}

	@Override
	public String toString() {
		if (backup != null)
			return backup.toString();
		// If forced to show its "word", the Adversary simply picks the first
		// one in its list of possible word. So this method can return 
		// a different result if called in a later state.
		return possibleWords.peek();
	}	
	
	// Private methods
	/**
	 * Checks if a letter has been guessed/queried for yet.
	 * @param letter The letter as a char.
	 * @return True if the letter has been processed previously (queried), 
	 *         else false.
	 */
	private boolean hasUsed(char letter) {
		return usedLetters.indexOf(letter) != -1;
	}

	/**
	 * Adds a letter to the list of used letters, updating the AdversaryWord's
	 * instance fields accordingly.
	 * @param letter The letter as a char.
	 */
	private void processLetter(char letter) {
		usedLetters += letter;
		
		// There are several different "realities" that the Adversary Word
		// can choose from: letter not in word, letter at first index,
		// letter at second index, etc... The AdversaryWord chooses the
		// reality containing the largest number of possible words.
		// This greedy approach is probably not optimal for maximizing lives
		// lost player, but it works pretty well in practice.
		Queue<String>[] realities = new LinkedList[length + 1];
		for (int i = 0; i < realities.length; i++) {
			realities[i] = new LinkedList<String>();
		}
		while (!possibleWords.isEmpty()) {
			String current = possibleWords.remove();
			int letterPos = current.indexOf(letter);
			letterPos++;
			realities[letterPos].add(current);
		}
		
		// Find the possible words list with the greatest number of words,
		// preferring the word list corresponding to the letter not being
		// in the hangman word for ties.
		int maxIndex = 0;
		for (int i = 1; i < realities.length; i++) {
			if (realities[i].size() > realities[maxIndex].size())
				maxIndex = i;
		}
		
		possibleWords = realities[maxIndex];
		
		// 0-th index means not in word, the rest n-th index means the n-th
		// letter of the word, so the partial word needs to be updated 
		// at the (n-1)-th index.
		if (maxIndex != 0)
			partialWord[maxIndex - 1] = letter;
	}
	
	/**
	 * Returns the first index of a given character array that contains the
	 * same as the given character.
	 * @param word   The char array.
	 * @param letter The letter as a char. 
	 * @return Null if the letter is not in the word, else an array of the 
	 *         int index where the letter appears.
	 */
	private int[] getIndexOf(char[] word, char letter) {
		for (int i = 0; i < word.length; i++) {
			// The AdversaryWord does not include words with repeated 
			// characters in its possible words list, so the array should 
			// have only one element.
			if (word[i] == letter)
				return new int[] {i};
		}
		return null;
	}
	
	/**
	 * Checks if a string has any repeated characters. A "unique" string
	 * has no repeats.
	 * @param s The string.
	 * @return True if the string contains unique characters only, else false.
	 */
	private boolean isUnique(String s) {
		s = s.toLowerCase();
		for (int i = 0; i < s.length(); i++) {
			for (int j = i + 1; j < s.length(); j++) {
				if (s.charAt(i) == s.charAt(j))
					return false;
			}
		}
		return true;
	}
}
