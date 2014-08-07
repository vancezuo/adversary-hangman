package app.game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

/**
 * A Letter Label is a label containing a single character, used to
 * denote individual letters of a hangman word in a Word Panel. The
 * label takes up a fixed amount of space, with a bold underline at 
 * the bottom.
 * 
 * @author Vance Zuo
 * Created: May 23, 2013
 * @see app.game.WordPanel
 *
 */
class LetterLabel extends JLabel {	
	
	private static final Color UNDERLINE_COLOR = Color.BLACK;
	private static final int UNDERLINE_THICKNESS = 5;
	// Widest letter constant is used to determine size of the letter label
	// such that any letter will fit. Of alphabetical characters 'W' would
	// almost certainly be the widest.
	private static final String WIDEST_LETTER = "W";

	// Instance Variables
	private Font baseFont;
	private Border underline;
	private double scale; // For determining font size
	private final int baseSize; // Base font size
	
	// Constructors
	/**
	 * Creates a new Letter Label with no character (a blank label).
	 * @param initScale The initial font size scale multiplier.
	 * @param font      The name of the font of the Letter Label.
	 * @param size      The initial font size.
	 */
	public LetterLabel(double initScale, String font, int size) {
		this(' ', initScale, font, size);
	}
	
	/**
	 * Creates a new Letter Label with a given starting character.
	 * @param ch        The initial character displayed.
	 * @param initScale The initial font size scale multiplier.
	 * @param fontName  The name of the font of the Letter Label.
	 * @param size      The initial font size.
	 */
	public LetterLabel(char ch, double initScale, String fontName, int size) {		
		super(String.valueOf(ch));
	
		baseFont = new Font(fontName, Font.PLAIN, (int) (initScale * size));
		underline = BorderFactory.createMatteBorder(0, 0, UNDERLINE_THICKNESS, 
				                                    0, UNDERLINE_COLOR);
		scale = initScale;
		baseSize = size;
		
		setFont(baseFont);
		setHorizontalAlignment(SwingConstants.CENTER);
		setVerticalAlignment(SwingConstants.BOTTOM);
		setBorder(underline);
		// Ensures consistent width between letter labels
		setPreferredSize(new Dimension(
				getFontMetrics(baseFont).stringWidth(WIDEST_LETTER),
				getFontMetrics(baseFont).getHeight())
		);
//		setMaximumSize(getPreferredSize());

//		setBorder(BorderFactory.createLineBorder(null, 1)); // Debug
	}
	
	// Public Methods
	/**
	 * Sets the letter of the label.
	 * @param ch The new character to be displayed.
	 */
	public void setLetter(char ch) {
		setLetter(ch, null);
	}
	
	/**
	 * Sets the letter of the label, with a given color.
	 * @param ch The new character to be displayed.
	 * @param c  Color of the new character.
	 */
	public void setLetter(char ch, Color c) {
		if (c != null)
			setForeground(c);
		setText(String.valueOf(ch));
	}

	/**
	 * Updates the font size of the Letter Label based on a previous
	 * height change of the content pane this label is contained within.
	 * @param heightChangeRatio The ratio of the current content pane height to
	 *                          the pane height when the label was last updated.
	 */
	public void updateSize(double heightChangeRatio) {
		scale *= heightChangeRatio;
		float newSize = (float) (scale * baseSize);
		setFont(getFont().deriveFont(newSize));
		setPreferredSize(new Dimension(
				getFontMetrics(getFont()).stringWidth(WIDEST_LETTER),
				getFontMetrics(getFont()).getHeight())
		);
//		setMaximumSize(getPreferredSize());
//		setMinimumSize(getPreferredSize());
	}
}
