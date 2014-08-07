package engine;

/**
 * A Game object simulates a hangman game. Each instance represents one
 * play-through. Each Game can be customized with a different Dict
 * (dictionary), Mode, word length, and lives. Its methods allow the user
 * to check or progress a Game's state.
 * 
 * @author Vance Zuo
 * Created: May 12, 2013
 *
 */
public class Game {
	
	/** Exception representing the end of a game. */
	public class GameOverException extends Exception {}

	// Instance Fields
	private Dict dict;
	private int length;
	private int lives;
	
	private Word word;	
	private String usedLetters;
	private char[] solved; // Represents the word in partially-solved state
	
	// Constructors
	/**
	 * Creates a new Game object with given starting conditions.
	 * @param dictionary Dict representing possible words to choose from.
	 * @param wordMode   Mode representing how the hangman word will be chosen.
	 * @param wordLength The number of characters in the word.
	 * @param maxLives   The number of failed tries the user is allowed.
	 */
	public Game(Dict dictionary, Mode wordMode, int wordLength, int maxLives) {
		setDict(dictionary);	
		setLength(wordLength);
		setLives(maxLives);
		setWord(wordMode);
		usedLetters = "";
		solved = new char[length];
	}
	
	// Public methods
	/**
	 * Checks if a given letter has been guessed/played.
	 * @param letter The letter to check. Must be an alphabetical char.
	 * @return True if letter has been played, else false.
	 */
	public boolean hasUsed(char letter) {
		if (!Character.isLetter(letter))
			throw new IllegalArgumentException("Argument must be a letter.");
		letter = Character.toLowerCase(letter); // Case-insensitive
		return usedLetters.indexOf(letter) != -1;
	}
	
	/**
	 * Checks if the hangman word has been completely solved.
	 * @return True if every letter in the word has been found, else false.
	 */
	public boolean isSolved() {
		for (char ch : solved) {
			if (ch == '\0') // Means unsolved letter
				return false;
		}
		return true;
	}
	
	/**
	 * Checks if the game is over; that is, if any more moves are possible.
	 * A game is over when the word has been solved or there are no 
	 * lives left.
	 * @return True if game is over, else false.
	 */
	public boolean isGameOver() {
		if (lives <= 0)
			return true;
		return isSolved();
	}
	
	/**
	 * Gets the number of lives the player has left.
	 * @return Number of lives left
	 */
	public int getLivesLeft() {
		return lives;
	}
	
	/**
	 * Gets letters that the player has guessed/played.
	 * @return An array of characters corresponding to letters that have 
	 *         been played.
	 */
	public char[] getUsedLetters() {
		return usedLetters.toCharArray();
	}
	
	/**
	 * Gets the hangman word with only the solved letters revealed.
	 * @return An array with the solved letters at their corresponding indices, 
	 *         and '\0' at indices that have not been solved.
	 */
	public char[] getSolvedPart() {
		return solved;
	}	
	
	/**
	 * Gets the solution word to the hangman game.
	 * @return An array of letters corresponding the correct word in this
	 *         hangman game.
	 */
	public char[] getAnswer() {
		return word.toString().toCharArray();
	}
	
	/**
	 * Changes the state of the Game by attempting to "play" a given letter.
	 * If the letter appears in the hangman word, then the partial solution is
	 * updated accordingly. If the letter isn't in the word OR has already
	 * been played, then the number of lives is decremented.
	 * @param letter The letter to play. Must be an alphabetical char.
	 * @return True if the letter is found in the word and successfully played,
	 *         else false (an unsuccessful move, meaning a loss of one life).
	 * @throws GameOverException if isGameOver() returns true.
	 */
	public boolean playLetter(char letter) throws GameOverException {
		if (!Character.isLetter(letter))
			throw new IllegalArgumentException("Argument must be a letter.");
		if (isGameOver())
			throw new GameOverException();
		letter = Character.toLowerCase(letter);
		boolean used = hasUsed(letter);
		if (!used)
			usedLetters += letter;
		if (!word.hasLetter(letter) || used) { // Unsuccessful case
			lives--;
			return false;
		}
		for (int i : word.getLetterPositions(letter)) {
			solved[i] = letter;
		}
		return true;
	}
	
	/**
	 * Ends the Game by "surrendering". The number of lives
	 * is set to zero, which causes further moves to be disallowed.
	 * @return True
	 * @throws GameOverException if isGameOver() already returns true.
	 */
	public boolean giveUp() throws GameOverException {
		if (isGameOver())
			throw new GameOverException();
		lives = 0;
		return true;
	}
	
	// Private methods
	/**
	 * Sets the hangman game's dictionary.
	 * @param dictionary The Dict
	 */
	private void setDict(Dict dictionary) {
		if (dictionary == null)
			throw new NullPointerException("Game dict must not be null.");
		dict = dictionary;
	}	
	
	/**
	 * Sets the hangman word's length.
	 * @param wordLength The word length
	 */
	private void setLength(int wordLength) {
		if (!dict.hasLength(wordLength))
			throw new IllegalArgumentException("Game dict has no words of " +
					                           "length " + length);
		length = wordLength;
	}
	
	/**
	 * Sets the hangman game player's number of lives.
	 * @param newLives The lives
	 */
	private void setLives(int newLives) {
		if (newLives < 1)
			throw new IllegalArgumentException("Game tries must be positive.");
		lives = newLives;
	}
	
	/**
	 * Sets the hangman game's word choice mode.
	 * @param mode The Mode
	 */
	private void setWord(Mode mode) {
		word = Mode.getWord(mode, dict, length);
	}	
	
//	/**
//	 * The main method. Used for testing purposes.
//	 *
//	 * @param args
//	 * @throws FileNotFoundException
//	 */
//	public static void main(String[] args) throws FileNotFoundException {
//		Dict d = new Dict();
//		Game.Mode m = Game.Mode.ADVERSARY;
//		int len = 5;
//		int lives = 10;
//	
//		Game g = new Game(d, m, len, lives);
//		
//		System.out.println(g);
//	}
}
