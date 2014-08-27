package app.game;

import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JMenuBar;
import javax.swing.JToolBar;

/**
 * The Hangman Menu is a menu bar of buttons for navigating the Hangman app:
 * creating a new game, accessing the settings, opening the about box,
 * and exiting.
 * 
 * @author Vance Zuo
 * Created: May 24, 2013
 *
 */
class HangmanMenu extends JMenuBar {
	// Constants
	// Action commands
	public static final String EXIT_CMD = "Exit";
	public static final String ABOUT_CMD = "About";
	public static final String SETTINGS_CMD = "Settings";
	public static final String NEW_CMD = "New Game";
	
	// Image urls
	private static final URL NEW_ICON = 
			HangmanMenu.class.getResource("/img/game_stick.png");
	private static final URL SETTINGS_ICON = 
			HangmanMenu.class.getResource("/img/gears.png");
	private static final URL INFO_ICON =
			HangmanMenu.class.getResource("/img/question.png");
	private static final URL EXIT_ICON = 
			HangmanMenu.class.getResource("/img/exit.png");
	
	
	// Instance Variables
	private JToolBar menu;
	private JButton newGame;
	private JButton settings;
	private JButton info;
	private JButton exit;
	
	
	// Private utility classes
	/**
	 * An Options Button is a button of the Options Bar, and in particular
	 * neither has focus nor a visible border. Its action command, note, is
	 * set to whatever its name is.
	 * 
	 * @author Vance Zuo
	 * Created: May 24, 2013
	 *
	 */
	private class OptionsButton extends JButton {
		public OptionsButton(String name, ImageIcon image) {
			super(name, image);
			setActionCommand(name);
			setBorderPainted(false);
			setFocusable(false);
		}
	}

	// Constructors
	/**
	 * Creates the Option Bar, which consists of a toolbar with buttons
	 * for each option.
	 */
	public HangmanMenu() {
		menu = new JToolBar();
		menu.setFloatable(false); // Prevents toolbar from being moved
		menu.setRollover(true); // Makes button borders appear on hover
		add(menu);
		
		newGame = new OptionsButton(NEW_CMD, new ImageIcon(NEW_ICON));
		menu.add(newGame);
		
		settings = new OptionsButton(SETTINGS_CMD, new ImageIcon(SETTINGS_ICON));
		menu.add(settings);
		
		info = new OptionsButton(ABOUT_CMD, new ImageIcon(INFO_ICON));
		menu.add(info);
			
		exit = new OptionsButton(EXIT_CMD, new ImageIcon(EXIT_ICON));
		menu.add(exit);
	}
	
	/**
	 * Adds an action listener to all of the buttons in the Options Bar.
	 * @param l Action Listener to add.
	 */
	public void addActionListener(ActionListener l) {
		newGame.addActionListener(l);
		settings.addActionListener(l);
		info.addActionListener(l);
		exit.addActionListener(l);
	}

}
