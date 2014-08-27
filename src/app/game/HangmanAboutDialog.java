package app.game;

import java.net.URL;

import javax.swing.JFrame;

import app.misc.AboutDialog;

/**
 * The Hangman About Dialog displays information about the Hangman program.
 * 
 * @author Vance Zuo
 * Created: May 13, 2013
 *
 */
class HangmanAboutDialog extends AboutDialog {

	// Constants
	private static final URL INFO =
			HangmanAboutDialog.class.getResource("/info/about.html");
	private static final int MARGIN = 10;
	private static final int WIDTH = 560;
	private static final int HEIGHT = 370;

	// Constructors
	/**
	 * Creates the About Dialog.
	 * @param parent The parent frame of this dialog. Should be a Hangman Frame.
	 */
	public HangmanAboutDialog(JFrame parent) {
		super(parent, WIDTH, HEIGHT, INFO);
	}

}
