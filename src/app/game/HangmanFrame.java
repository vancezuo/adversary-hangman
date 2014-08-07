package app.game;

import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.net.URL;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import app.settings.SettingsDialog;
import engine.Dict;
import engine.Game;
import engine.Game.GameOverException;

/**
 * A Hangman Frame is the main frame for the Hangman game application. It
 * contains a menu, the hangman, the hangman word, and an on-screen keyboard.
 * 
 * @author Vance Zuo
 * Created: May 23, 2013
 *
 */
public class HangmanFrame extends JFrame {

	private static final String TITLE = "Adversary Hangman";
	private static final URL ICON = 
			HangmanFrame.class.getResource("/img/icon.png");
	private static final String LOOK_AND_FEEL = "Nimbus";	
	// Constants below denote colors of text for letters that were not solved,
	// those that were guessed correctly, and those that were guessed
	// incorrectly, respectively.
	private static final Color COLOR_MISSED = Color.GRAY;
	private static final Color COLOR_CORRECT = Color.decode("#99CC99");
	private static final Color COLOR_WRONG = Color.decode("#CC9999");

	// Instance Fields
	private ManPanel hangman;
	private WordPanel word;
	private KeyboardPanel alphabet;
	private OptionsBar menu;
	
	private SettingsDialog settings;
	private AboutDialog about;
	
	private Dict dict;
	private Game game; // The current game instance
	
	// Heigh and width parameters used to scale Man Panel graphics correctly.
	private int height;
	private int width;
	
	/**
	 * The Play Listener listens for Action Event commands corresponding to
	 * different hangman game choices, such as trying to play a letter.
	 * It then updates the Game and graphics accordingly.
	 * 
	 * @author Vance Zuo
	 * Created: May 23, 2013
	 *
	 */
	private class PlayListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent a) {
			String command = a.getActionCommand();
			try {
				if (command.equals("Surrender")) {
					game.giveUp();
					hangman.displayLast();
					alphabet.setAllEnabled(false);
					updateSolve();
					return;
				}
				char letter = Character.toLowerCase(command.charAt(0));
				boolean success = game.playLetter(letter);
				alphabet.setKeyEnabled(letter, false);
				if (success) {
					updateWord();
					alphabet.setKeyColor(letter, COLOR_CORRECT);
				} else {
					hangman.displayNext();
					alphabet.setKeyColor(letter, COLOR_WRONG);
				}
				if (game.isGameOver()) {
					alphabet.setAllEnabled(false);
					if (!game.isSolved()) {
						updateSolve();
					}
				}
			} catch (GameOverException e) {
				String error = "Game over. Stop trying to play on.";
				JOptionPane.showMessageDialog(HangmanFrame.this, error,
						"Error", JOptionPane.ERROR_MESSAGE);
				System.err.println("Error: " + error);
				e.printStackTrace();
			}
		}

		private void updateWord() {
			for (int i = 0; i < game.getSolvedPart().length; i++) {
				char current = game.getSolvedPart()[i];
				if (current != '\0')
					word.set(i, current);
			}
		}
		
		private void updateSolve() {
			for (int i = 0; i < game.getAnswer().length; i++) {
				if (!word.isSet(i))
					word.set(i, game.getAnswer()[i], COLOR_MISSED);
			}
		}
	}
	
	/**
	 * The Menu Listener listens for Action Events commands corresponding
	 * to different menu options: creating a new game, changing settings,
	 * exiting, etc.
	 * 
	 * @author Vance Zuo
	 * Created: May 23, 2013
	 *
	 */
	private class MenuListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			String command = arg0.getActionCommand();
			if (command.equals("New Game")) {
				resetGame();
			} else if (command.equals("Settings")){
				settings.setLocationRelativeTo(HangmanFrame.this);
				settings.setVisible(true);
			} else if (command.equals("About")) {
				about.setLocationRelativeTo(HangmanFrame.this);
				about.setVisible(true);
			} else if (command.equals("Exit")) {
				dispose();
			}
		}
	}
	
	// Constuctors
	/**
	 * Creates the application.
	 */
	public HangmanFrame() {
		initLookAndFeel();
		initGuiComponents();
		initGuiProperties();
		initGame();
		height = getContentHeight();
		width = getContentWidth();
	}
	
	// Public Methods
	/**
	 * Starts a new hangman game, updating the engine and GUI components
	 * accordingly.
	 */
	public void resetGame() {
		settings.updateWordLength();
		hangman.reset(settings.getLives());
		word.reset(settings.getWordLength());
		alphabet.setAllEnabled(true);
		alphabet.setAllColor(UIManager.getColor(JButton.class));
		initGame();
	}	
	
	// Private Methods
	/**
	 * Initializes the "engine" of the game: the Dict of words and
	 * a new Game instance.
	 */
	private void initGame() {
		dict = settings.getDict();
		game = new Game(dict, settings.getMode(), 
				settings.getWordLength(), settings.getLives());
	}
	
	/**
	 * Sets various properties of the Hangman Frame like its size, location,
	 * and close operation.
	 */
	private void initGuiProperties() {	
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		pack();
		setMinimumSize(getSize());
	}

	/**
	 * Initialize the contents of the Hangman Frame.
	 */
	private void initGuiComponents() {	
		setIconImage(Toolkit.getDefaultToolkit().getImage(ICON));
		setTitle("Adversary Hangman");
		
		settings = new SettingsDialog(this);
		about = new AboutDialog(this);
		
		hangman = new ManPanel(settings.getLives());
		
		word = new WordPanel(settings.getWordLength());
		
		alphabet = new KeyboardPanel();
		alphabet.addPlayListener(new PlayListener());
		
		menu = new OptionsBar();
		menu.addButtonListener(new MenuListener());
		setJMenuBar(menu);
		
		GroupLayout layout = new GroupLayout(getContentPane());
		layout.setHorizontalGroup(
			layout.createParallelGroup(Alignment.LEADING)
				.addGroup(layout.createSequentialGroup()
					.addComponent(hangman)
					.addComponent(word) )
				.addComponent(alphabet)
		);
		layout.setVerticalGroup(
			layout.createParallelGroup(Alignment.LEADING)
				.addGroup(layout.createSequentialGroup()
					.addGroup(layout.createParallelGroup(Alignment.LEADING)
						.addComponent(hangman)
						.addComponent(word) )
					.addComponent(alphabet) )
		);
		
		// Does't work for scaling hangman image
		// layout.linkSize(SwingConstants.VERTICAL, hangman, alphabet, word);
		
		getContentPane().setLayout(layout);
		// When the widow is resized, this listener records the dimensions
		// and updates the image sizes the the frame's compenents accordingly.
		getContentPane().addComponentListener(new ComponentAdapter() {
			@Override public void componentResized(ComponentEvent e) {
				super.componentResized(e);
				int newWidth = getContentWidth();
				int newHeight = getContentHeight();
				double widthRatio = (double) newWidth / width;
				double heightRatio = (double) newHeight / height;
				height = newHeight;
				width = newWidth;
				alphabet.updateSize(widthRatio, heightRatio);
				hangman.updateSize(widthRatio, heightRatio);
				word.updateSize(widthRatio, heightRatio);
			}
		});
	}
	
	/**
	 * Sets the Look and Feel for the GUI of this application. 
	 */
	private void initLookAndFeel() {
        if (LOOK_AND_FEEL == null)
        	return;
        
        String lookAndFeel = null; 
        if (LOOK_AND_FEEL.equals("System")) { // "System" being a custom option
        	lookAndFeel = UIManager.getSystemLookAndFeelClassName();
        } else {
        	for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
        		if (LOOK_AND_FEEL.equals(info.getName())) {
        			lookAndFeel = info.getClassName();
        			break;
        		}
        	}
        }
        if (lookAndFeel == null)
        	return;

        try {        	
        	UIManager.setLookAndFeel(lookAndFeel);          
        } catch (Exception e) {
        	System.err.printf("Look and feel (%s) not found. ", lookAndFeel);
        	System.err.println("Using the default look and feel.");
        	e.printStackTrace();
        } 
    }
	
	/**
	 * Gets the height of the content panel of the frame.
	 * @return the content panel's height in pixels.
	 */
	private int getContentHeight() {
		return getContentPane().getHeight();
	}
	
	/**
	 * Gets the width of the content panel of the frame.
	 * @return the content panel's width in pixels.
	 */
	private int getContentWidth() {
		return getContentPane().getWidth();
	}
}
