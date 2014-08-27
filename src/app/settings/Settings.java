package app.settings;

import engine.Dict;
import engine.Mode;

/** 
 * The Settings class contains parameters to apply on hangman games.
 * This includes a dictionary to draw words from, a mode for how words are
 * chosen, the length of the word, the lives the player has, and whether or
 * not the length should change between games.
 * 
 * @author Vance Zuo
 * Created: Aug 11, 2014
 *
 */
public class Settings {
	// Constants
	public static final Dict DEFAULT_DICT = new Dict();

	public static final Mode DEFAULT_MODE = Mode.ADVERSARY;

	public static final int DEFAULT_LENGTH = 4;
	public static final int DEFAULT_MIN_LENGTH = DEFAULT_DICT.getMinLength();
	public static final int DEFAULT_MAX_LENGTH = DEFAULT_DICT.getMaxLength();

	public static final int DEFAULT_LIVES = 7;
	public static final int DEFAULT_MIN_LIVES = 1;
	public static final int DEFAULT_MAX_LIVES = 25;


	// Instance Fields
	private Dict dictionary;
	private Mode mode;
	private int wordLength;
	private int lives;
	private boolean randomLength;

	private Settings newSettings; // Holds tentative changes


	// Constructors
	public Settings(Dict initDict, Mode initMode, int initWordLength,
			int initLives, boolean initRandomizeLength, boolean autocommit) {
		dictionary = initDict;
		mode = initMode;
		wordLength = initWordLength;
		lives = initLives;
		randomLength = initRandomizeLength;

		if (autocommit) {
			newSettings = this;
		} else {
			newSettings = new Settings(initDict, initMode, initWordLength,
					initLives, initRandomizeLength, true);
		}
	}

	public Settings(Dict initDict, Mode initMode, int initWordLength,
			int initLives, boolean initRandomizeLength) {
		this(initDict, initMode, initWordLength, initLives,
				initRandomizeLength, false);
	}

	public Settings() {
		this(DEFAULT_DICT, DEFAULT_MODE, DEFAULT_LENGTH, DEFAULT_LIVES, true);
	}


	// Public methods
	public Dict getDict() {
		return dictionary;
	}

	public Mode getMode() {
		return mode;
	}

	public int getWordLength() {
		return wordLength;
	}

	public int getLives() {
		return lives;
	}

	public boolean isRandomizingLength() {
		return randomLength;
	}

	public void setDictionary(Dict dict) {
		newSettings.dictionary = dict;
	}

	public void setMode(Mode mode) {
		newSettings.mode = mode;
	}

	public void setWordLength(int wordLength) {
		newSettings.wordLength = wordLength;
	}

	public void setLives(int lives) {
		newSettings.lives = lives;
	}

	public void setRandomizeLength(boolean randomizeLength) {
		newSettings.randomLength = randomizeLength;
	}

	public void commit() {
		dictionary = newSettings.dictionary;
		mode = newSettings.mode;
		wordLength = newSettings.wordLength;
		lives = newSettings.lives;
		randomLength = newSettings.randomLength;
	}

	public void revert() {
		newSettings.dictionary = dictionary;
		newSettings.mode = mode;
		newSettings.wordLength = wordLength;
		newSettings.lives = lives;
		newSettings.randomLength = randomLength;
	}

}
