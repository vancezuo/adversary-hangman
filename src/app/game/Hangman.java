package app.game;

import java.awt.EventQueue;

import javax.swing.JFrame;

/**
 * The Hangman class contains the program's main method, simply creating
 * a Hangman Frame and displaying it.
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
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					JFrame window = new HangmanFrame();
					window.setVisible(true);					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}
