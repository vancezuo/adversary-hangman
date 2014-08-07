package app.game;

import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JMenuBar;
import javax.swing.JToolBar;

/**
 * The Options Bar is a menu of buttons for navigating the Hangman program:
 * creating a new game, accessing the settings, opening the about box,
 * and exiting.
 * 
 * @author Vance Zuo
 * Created: May 24, 2013
 *
 */
class OptionsBar extends JMenuBar {

	private static final URL NEW_ICON = 
			OptionsBar.class.getResource("/img/game_stick.png");
	private static final URL SETTINGS_ICON = 
			OptionsBar.class.getResource("/img/gears.png");
	private static final URL INFO_ICON =
			OptionsBar.class.getResource("/img/question.png");
	private static final URL EXIT_ICON = 
			OptionsBar.class.getResource("/img/exit.png");
	
	// Instance Variables
	private JToolBar menu;
	private JButton newGame;
	private JButton settings;
	private JButton info;
	private JButton exit;
	
	/**
	 * An Options Button is a button of the Options Bar, and in particular
	 * neither has focus nor a visible border. 
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
	public OptionsBar() {
		menu = new JToolBar();
		menu.setFloatable(false); // Prevents toolbar from being moved
		menu.setRollover(true); // Makes button borders appear on hover
		add(menu);
		
		newGame = new OptionsButton("New Game", new ImageIcon(NEW_ICON));
		menu.add(newGame);
		
		settings = new OptionsButton("Settings", new ImageIcon(SETTINGS_ICON));
		menu.add(settings);
		
		info = new OptionsButton("About", new ImageIcon(INFO_ICON));
		menu.add(info);
			
		exit = new OptionsButton("Exit", new ImageIcon(EXIT_ICON));
		menu.add(exit);
	}
	
	/**
	 * Adds an action listener to all of the buttons in the Options Bar.
	 * @param l Action Listener to add.
	 */
	public void addButtonListener(ActionListener l) {
		newGame.addActionListener(l);
		settings.addActionListener(l);
		info.addActionListener(l);
		exit.addActionListener(l);
	}

}
