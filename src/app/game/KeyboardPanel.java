package app.game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * The Keyboard Panel contains buttons for playing letters in a hangman
 * game, as well as white flag "surrender" button.
 * 
 * @author Vance Zuo
 * Created: May 23, 2013
 *
 */
class KeyboardPanel extends JPanel {
	
	// Two icon sizes, 32x32 and 48x48, to provide some scale flexibility
	private static final URL GIVEUP_ICON_32 = KeyboardPanel.class
			.getResource("/img/white_flag_32.png");
	private static final URL GIVEUP_ICON_48 = KeyboardPanel.class
			.getResource("/img/white_flag_48.png");
	// By default, arrayed on 3 rows by 9 column grid
	private static final int NUM_ROWS = 3;
	private static final int NUM_COLS = 9;
	private static final String LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static final int INIT_GAP = 20; // Margin gap size
	
	private static final String FONT = "Goudy Stout";
	private static final int INIT_SIZE = 16;
	private static final double INIT_SCALE = 1.0;

	// Instance Variables
	private JButton[] keys;
	// Scale variables are relative to initial width/height
	private double widthScale;
	private double heightScale;
	
	// Constructors
	/**
	 * Creates the Keyboard Panel. 
	 */
	public KeyboardPanel() {
		heightScale = INIT_SCALE;
		widthScale = INIT_SCALE;
		
		setLayout(new GridLayout(NUM_ROWS, NUM_COLS, 0, 0));
		setBorder(BorderFactory.createEmptyBorder(0, INIT_GAP, 0, INIT_GAP));
		
		keys = new JButton[LETTERS.length() + 1]; // Letters + Surrender buttons
		for (int i = 0; i < LETTERS.length(); i++) {
			String current = String.valueOf(LETTERS.charAt(i));
			keys[i] = new JButton(current);
			keys[i].setFont(new Font(FONT, Font.PLAIN, INIT_SIZE));
			initButton(keys[i], current);
		}
		
		// Special case: White flag button
		int last = LETTERS.length();
		keys[last] = new JButton(new ImageIcon(GIVEUP_ICON_32));
		initButton(keys[last], "Surrender");
	}

	/**
	 * Initializes a keyboard button's properties (action command, dimensions,
	 * etc.) and adds it to the panel.
	 * @param key           Button to be added to the Keyboard Panel.
	 * @param actionCommand String representing the key's action command name.
	 */
	private void initButton(JButton key, String actionCommand) {
		key.setActionCommand(actionCommand);
		key.setFocusable(false);
		int size = Math.max(key.getPreferredSize().width, 
	            			key.getPreferredSize().height);
		key.setPreferredSize(new Dimension(size, size));
		add(key);
	}
	
	// Public methods
	/**
	 * Sets the color of all of the buttons in the panel.
	 * @param color Color to change all buttons in the panel to.
	 */
	public void setAllColor(Color color) {
		for (JButton b : keys) {
			b.setForeground(color);
			b.setBackground(color);
		}
	}
	
	/**
	 * Changes the color of the button with a given character.
	 * @param ch    Character corresponding to the desired button.
	 * @param color Color to change the button to.
	 */
	public void setKeyColor(char ch, Color color) {
		for (JButton b : keys) {
			if (Character.toLowerCase(b.getText().charAt(0)) == ch) {
				b.setForeground(color);
				b.setBackground(color);
				return;
			}
		}
	}
	
	/**
	 * Enables/disables all of the buttons in the Keyboard Panel.
	 * @param isEnabled Whether the buttons should be enabled or not.
	 */
	public void setAllEnabled(boolean isEnabled) {
		for (JButton b : keys) {
			b.setEnabled(isEnabled);
		}
	}
	
	/**
	 * Enables/disables a button with a given character in the panel.
	 * @param ch        Character corresponding to the desired button.
	 * @param isEnabled Whether the button should be enabled or not.
	 */
	public void setKeyEnabled(char ch, boolean isEnabled) {
		for (int i = 0; i < LETTERS.length(); i++) {
			if (Character.toLowerCase(keys[i].getText().charAt(0)) == ch)
				keys[i].setEnabled(isEnabled);
		}
	}

	/**
	 * Adds an action listener to all of the buttons in the panel.
	 * @param l Action Listener to add.
	 */
	public void addPlayListener(ActionListener l) {
		for (JButton button : keys) {
			button.addActionListener(l);
		}
	}
	
	/**
	 * Updates the size of the button graphics based on a previous width
	 * and height change of the content pane this Keyboard Panel is 
	 * contained in. 
	 * @param widthChangeRatio  The ratio of the current content pane width to
	 *                          the width when this panel size was last updated.
	 * @param heightChangeRatio The ratio of the current content pane height to
	 *                          the width when this panel size was last updated.
	 */
	public void updateSize(double widthChangeRatio, double heightChangeRatio) {
		// Updates scale variables
		heightScale *= heightChangeRatio;
		widthScale *= widthChangeRatio;
		// Updates font sizes
		for (JButton button : keys) {
			float newSize = (float) (heightScale * INIT_SIZE);
			button.setFont(button.getFont().deriveFont(newSize));
		}		
		// Updates surrender icon image size
		JButton last = keys[keys.length - 1];
		URL pref = (heightScale > 2) ? GIVEUP_ICON_48 : GIVEUP_ICON_32;
		last.setIcon(new ImageIcon(pref));
		
		// Updates margin spacing size
		int sideMargin = (int) (widthScale * INIT_GAP);
		int bottomMargin = (int) (heightScale * INIT_GAP);
		setBorder(BorderFactory.createEmptyBorder(0, sideMargin, 
				                                  bottomMargin, sideMargin));
	}
}
