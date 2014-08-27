package app.game;

import java.awt.EventQueue;

/**
 * The Hangman class contains the program's main method, simply creating
 * a Hangman Presenter and running it.
 * 
 * @author Vance Zuo
 * Created: May 13, 2013
 *
 */
public class Hangman {

	/**
	 * Launches the application.
	 */
	public static void main(String[] args) {
		// Fixes rendering issues on Windows
		System.setProperty("sun.java2d.noddraw", "true");		
		EventQueue.invokeLater(new HangmanPresenter());
	}

}
