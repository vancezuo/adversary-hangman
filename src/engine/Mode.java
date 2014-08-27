package engine;

import java.util.EnumMap;
import java.util.Map;

/**
 * Enumerates the different word choice modes in a Game. 
 * 
 * @author Vance Zuo
 * Created: May 12, 2013
 *
 */
public enum Mode {
	RANDOM ("Random", "Selects a word randomly."), 
	SCRABBLE ("Scrabble", "From a small random sample, selects the " +
			"'best' Scrabble word."),
	ADVERSARY ("Adversary", "Always selects a 'most difficult' word for " +
			"you to guess... ;)");
	
	// Instance Fields 
	// These provide some brief information about each mode
	private final String name;
	private final String detail;
	
	// Constructor
	private Mode(String n, String d) { 
		name = n; 
		detail = d; 
	}
	
	// Public methods
	/**
	 * Gets a short sentence describing the mode.
	 * @return A string descirbing the Mode
	 */
	public String getDescription() { 
		return detail; 
	}
	
	@Override 
	public String toString() { 
		return name; 
	}

	/**
	 * Creates and returns a Word object using a given Mode, Dict (dictionary), 
	 * and word length. 
	 * @param mode   The Mode used to determine the type of Word chosen.
	 * @param dict   The Dict containing set of words to choose from.
	 * @param length The length of the word to be chosen.
	 * @return A new Word object with given constructor parameters
	 * @see Word
	 */
	public static Word getWord(Mode mode, Dict dict, int length) {
		if (mode == null)
			throw new NullPointerException("Game mode must not be null.");
		switch (mode) {
		case RANDOM: 
			return new RandomWord(dict, length); 
		case ADVERSARY: 
			return new AdversaryWord(dict, length);
		case SCRABBLE: 
			return new ScrabbleWord(dict, length);
		}
		return null; // Should not be reachable
	}
	
	public static Map<Mode, String> getModeDescriptionMap() {
		Map<Mode, String> map = new EnumMap<Mode, String>(Mode.class);
		for (Mode e : Mode.values())
		    map.put(e, e.getDescription());
		return map;
	}
}