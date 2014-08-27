package app.game;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;

import javax.swing.Box;
import javax.swing.JPanel;

/**
 * The Word Panel displays the word in a hangman game with unsolved letters
 * hidden. The unsolved spaces are occupied with a blank underlined space.
 * 
 * @author Vance Zuo
 * Created: May 24, 2013
 *
 */
class HangmanWordPanel extends JPanel {
	// Constants
	private static final int H_GAP = 5; // Horizontal spacing
	private static final int V_GAP = 0; // Vertical spacing
	
	private static final String FONT = "Goudy Stout";
	private static final int INIT_SIZE = 24;
	private static final double INIT_SCALE = 1.0;
	
	
	// Instance fields
	private HangmanLetterLabel[] letters;
	private boolean[] changed; // For detecting which letters are solved
	
	private JPanel wordLine;
	private Component topGlue;
	private Component bottomGlue;
	private Component leftGlue;
	private Component rightGlue;
	
	private double heightScale;
	private final int initLetters; // Number of letters
	
	
	// Constructors
	/**
	 * Creates a new Word Panel with a given number of letters.
	 * @param numLetters The number of letters.
	 */
	public HangmanWordPanel(int numLetters) {
		heightScale = INIT_SCALE;
		initLetters = numLetters;
		setLayout(new GridLayout(0, 1, H_GAP, V_GAP));
		reset(numLetters);
	}
	
	
	// Public methods
	/**
	 * Checks if a given position in the word has been solved.
	 * @param index The index to check.
	 * @return True if the word at the index contains a non-blank letter,
	 *         meaning that it was solved, else false.
	 */
	public boolean isSet(int index) {
		return changed[index];
	}
	
	/**
	 * Resets the Word Panel (for a new game). The number of spaces may change
	 * depending on the number of letters in the new word. All of the spaces
	 * will become blank.
	 * @param numLetters The number of letters in the new game's word.
	 */
	public void reset(int numLetters) {
		removeAll();
		topGlue = Box.createVerticalGlue();
		add(topGlue);
		
		wordLine = new JPanel();
		wordLine.setLayout(new GridLayout(1, 0, H_GAP, V_GAP));
		add(wordLine);	
		{
			leftGlue = Box.createHorizontalGlue();
			wordLine.add(leftGlue);
			
			// The font size never exceeds INIT_SIZE, but if the number letters
			// increases beyond the initial, then the size is shrunk to 
			// ensure the word fits in the same width area.
			int size = INIT_SIZE;
			if (numLetters > initLetters)
				 size = initLetters * INIT_SIZE / numLetters;
			// Initializes letters and changed arrays
			letters = new HangmanLetterLabel[numLetters];
			changed = new boolean[numLetters];
			for (int i = 0; i < numLetters; i++) {
				letters[i] = new HangmanLetterLabel(heightScale, FONT, size);
				changed[i] = false;
				wordLine.add(letters[i]);
			}
			
			rightGlue = Box.createHorizontalGlue();
			wordLine.add(rightGlue);
		}
		
		bottomGlue = Box.createVerticalGlue();
		add(bottomGlue);
		revalidate(); // Updates display on GUI
	}
	
	/**
	 * Sets the letter at a given position in the word.
	 * @param index The index in the word to set.
	 * @param ch    The character to set to.
	 */
	public void set(int index, char ch) {
		set(index, ch, null);
	}

	/**
	 * Sets the letter at a given position in the word, with a given color
	 * @param index The index in the word to set.
	 * @param ch    The character to set to.
	 * @param c     The color of the character.
	 */
	public void set(int index, char ch, Color c) {
		letters[index].setLetter(ch, c);
		changed[index] = true;
	}

	/**
	 * Updates the size of the letter graphics based on a previous width
	 * and height change of the content pane this Man Panel is contained in. 
	 * @param widthChangeRatio  The ratio of the current content pane width to
	 *                          the width when this panel size was last updated.
	 * @param heightChangeRatio The ratio of the current content pane height to
	 *                          the width when this panel size was last updated.
	 */
	public void updateSize(double widthChangeRatio, double heightChangeRatio) {
		heightScale *= heightChangeRatio;
		for (HangmanLetterLabel letter : letters) {
			letter.updateSize(heightChangeRatio);
		}
	}
}
