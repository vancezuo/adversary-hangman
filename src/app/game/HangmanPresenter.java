package app.game;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.io.FileNotFoundException;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import app.misc.AboutDialog;
import app.settings.Settings;
import app.settings.SettingsDialog;
import engine.Dict;
import engine.Game;
import engine.Game.GameOverException;

/** 
 * The Hangman Presenter integrates the GUI of the hangman game with the 
 * backend settings and game models, following an MVP-style architecture.
 * The presenter contains all listeners that handle user input in the GUI,
 * editing the GUI or models accordingly. The GUI itself contains "blind" 
 * components that contain no hangman game or application logic. 
 * And the models contain game state information independent of presentation 
 * (i.e. a command-line interface would work just as well with the models).
 * 
 * The presenter itself can be run as an application.
 * 
 * @author Vance Zuo
 * Created: Aug 11, 2014
 *
 */
public class HangmanPresenter implements Runnable {
	// Constants
	// These denote colors of text for letters that were not solved,
	// those that were guessed correctly, and those that were guessed
	// incorrectly, respectively.
	private static final Color COLOR_MISSED = Color.GRAY;
	private static final Color COLOR_CORRECT = Color.decode("#99CC99");
	private static final Color COLOR_WRONG = Color.decode("#CC9999");

	
	// Instance Fields
	// Views
	private HangmanFrame view;
	private AboutDialog about;
	private SettingsDialog settingsView;

	// Models
	private Game game;
	private Settings settings;


	// Private utility classes
	/**
	 * The Play Listener listens for Action Event commands corresponding to
	 * different hangman game choices, such as trying to play a letter. It then
	 * updates the Game and graphics accordingly.
	 * 
	 * @author Vance Zuo 
	 * Created: May 23, 2013
	 *
	 */
	private class GamePlayListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent a) {
			String command = a.getActionCommand();
			try {
				if (command.equals(HangmanKeyboardPanel.SURRENDER_CMD)) {
					game.giveUp();
					view.getImagePanel().displayLast();
					view.getAlphabetPanel().setAllEnabled(false);
					updateSolve();
					return;
				}
				char letter = Character.toLowerCase(command.charAt(0));
				boolean success = game.playLetter(letter);
				view.getAlphabetPanel().setKeyEnabled(letter, false);
				if (success) {
					updateWordPanel();
					view.getAlphabetPanel().setKeyColor(letter, COLOR_CORRECT);
				} else {
					view.getImagePanel().displayNext();
					view.getAlphabetPanel().setKeyColor(letter, COLOR_WRONG);
				}
				if (game.isGameOver()) {
					view.getAlphabetPanel().setAllEnabled(false);
					if (!game.isSolved()) {
						updateSolve();
					}
				}
			} catch (GameOverException e) {
				String error = "Game over. Stop trying to play on.";
				JOptionPane.showMessageDialog(view, error, "Error",
						JOptionPane.ERROR_MESSAGE);
				System.err.println("Error: " + error);
				e.printStackTrace();
			}
		}

		private void updateWordPanel() {
			for (int i = 0; i < game.getSolvedPart().length; i++) {
				char current = game.getSolvedPart()[i];
				if (current != '\0')
					view.getWordPanel().set(i, current);
			}
		}

		private void updateSolve() {
			for (int i = 0; i < game.getAnswer().length; i++) {
				if (!view.getWordPanel().isSet(i))
					view.getWordPanel().set(i, game.getAnswer()[i],
							COLOR_MISSED);
			}
		}
	}

	/**
	 * The Menu Listener listens for Action Events commands corresponding to
	 * different menu options: creating a new game, changing settings, exiting,
	 * etc.
	 * 
	 * @author Vance Zuo 
	 * Created: May 23, 2013
	 *
	 */
	private class AppMenuListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			String command = arg0.getActionCommand();
			if (command.equals(HangmanMenu.NEW_CMD)) {
				if (settings.isRandomizingLength()) {
					int newLength = settings.getDict().getRandomLength();
					settings.setWordLength(newLength);
					settings.commit();
					settingsView.getLengthChooser().setValue(newLength);
				}
				resetGame();
			} else if (command.equals(HangmanMenu.SETTINGS_CMD)) {
				settingsView.setLocationRelativeTo(view);
				settingsView.setVisible(true);
			} else if (command.equals(HangmanMenu.ABOUT_CMD)) {
				about.setLocationRelativeTo(view);
				about.setVisible(true);
			} else if (command.equals(HangmanMenu.EXIT_CMD)) {
				view.dispose();
			}
		}
	}
	
	/** 
	 * The Dict Change Listener listens for property changes in the 
	 * dictionary chooser of the settings view. It rejects the change if
	 * the new dictionary cannot be loaded successfully, else updates
	 * the model with the new dictioary
	 * 
	 * @author Vance Zuo
	 * Created: Aug 8, 2014
	 *
	 */
	private class DictChangeListener implements VetoableChangeListener {
		@Override
		public void vetoableChange(PropertyChangeEvent evt)
				throws PropertyVetoException {
			try {
				Dict dict = settingsView.getDictChooser().getDict();
				settings.setDictionary(dict);
				settingsView.getLengthChooser().setMin(dict.getMinLength());
				settingsView.getLengthChooser().setMax(dict.getMaxLength());
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				veto(evt, "File not found.");
			} catch (Exception e) {
				e.printStackTrace();
				veto(evt, "Failed to successfully load file.");
			}
		}

		private void veto(PropertyChangeEvent evt, String msg)
				throws PropertyVetoException {
			JOptionPane.showMessageDialog(view, msg, "Error",
					JOptionPane.ERROR_MESSAGE);
			throw new PropertyVetoException(msg, evt);
		}
	}
	
	/** 
	 * The Mode Change Listener changes the model according to changes to 
	 * the game mode by the user in the settings view.
	 * 
	 * @author Vance Zuo
	 * Created: Aug 11, 2014
	 *
	 */
	private class ModeChangeListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			settings.setMode(settingsView.getModeChooser().getMode());
		}
	}
	
	/** 
	 * The Word Length Listener changes the settings model according to changes 
	 * to the hangman word length by the user in the settigns view.
	 * 
	 * @author Vance Zuo
	 * Created: Aug 11, 2014
	 *
	 */
	private class WordLengthListener implements ChangeListener {
		@Override
		public void stateChanged(ChangeEvent e) {
			settings.setWordLength(settingsView.getLengthChooser().getValue());
		}		
	}
	
	/** 
	 * The Lives Listener changes the settings model according to changes 
	 * to the game lives by the user in the settigns view.
	 * 
	 * @author Vance Zuo
	 * Created: Aug 11, 2014
	 *
	 */
	private class LivesListener implements ChangeListener {
		@Override
		public void stateChanged(ChangeEvent e) {
			settings.setLives(settingsView.getLivesChooser().getValue());
		}		
	}
	
	/** 
	 * The Randomize Length Listener changes the settings model according
	 * to changes to the randomize length flag by the user in the settings
	 * view.
	 * 
	 * @author Vance Zuo
	 * Created: Aug 11, 2014
	 *
	 */
	private class RandomizeLengthListener implements ChangeListener {
		@Override
		public void stateChanged(ChangeEvent e) {
			settings.setRandomizeLength(
					settingsView.getRandomLengthCheckbox().isSelected());
		}		
	}

	/** 
	 * The Settings Confirm Listener commits (saves) changes to the settings
	 * model that were made since the last commit. It also hides the 
	 * settings view and resets the current hangman game.
	 * 
	 * @author Vance Zuo
	 * Created: Aug 11, 2014
	 *
	 */
	private class SettingsConfirmListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {			
			// Validate word length
			int desiredLength = settings.getWordLength();
			if (!settings.getDict().hasLength(desiredLength)) {
				String error = "Wait! No words of length " + desiredLength
						+ " found in the current dictionary.";
				JOptionPane.showMessageDialog(settingsView, error, "Error",
						JOptionPane.ERROR_MESSAGE);
				System.err.println("Error: " + error);
				return;
			}
			settings.commit();
			settingsView.setVisible(false);
			resetGame();
		}
	}

	/** 
	 * The Settings Cancel Listener reverts (undos) changes to the settings
	 * model that occured since the last commit, and hides the settings view.
	 * 
	 * @author Vance Zuo
	 * Created: Aug 11, 2014
	 *
	 */
	private class SettingsCancelListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			settings.revert();
			settingsView.setVisible(false);
		}
	}

	/** 
	 * The Settings Window Listener listens for a window closing event
	 * (in the settings view) and alerts the user that the settings model
	 * will be reset to its last saved state.
	 * 
	 * @author Vance Zuo
	 * Created: Aug 11, 2014
	 *
	 */
	private class SettingsWindowListener extends WindowAdapter {
		@Override
		public void windowClosing(WindowEvent e) {
			String msg = "Changes will not be saved.";
			JOptionPane.showMessageDialog(settingsView, msg);
			settings.revert();
		}
	}

	
	// Constructors
	/**
	 * Initializes the Hangman Presenter instance.
	 */
	public HangmanPresenter() {
		initView();	
		initModel();		
	}

	/**
	 * Initializes the viewable elements of the hangman app.
	 * This includes the main GUI and settings dialog GUI.
	 */
	private void initView() {
		view = new HangmanFrame(Settings.DEFAULT_LIVES, Settings.DEFAULT_LENGTH);
		view.getMenu().addActionListener(new AppMenuListener());
		view.getAlphabetPanel().addActionListener(new GamePlayListener());
		
		about = new HangmanAboutDialog(view);		
		
		settingsView = new SettingsDialog(view);
		// Add listeners for settings dialog
		settingsView.addWindowListener(new SettingsWindowListener());
		settingsView.getDictChooser().addVetoableChangeListener(
				new DictChangeListener());
		settingsView.getModeChooser().addActionListener(
				new ModeChangeListener());
		settingsView.getLengthChooser().addChangeListener(
				new WordLengthListener());
		settingsView.getLivesChooser().addChangeListener(new LivesListener());
		settingsView.getRandomLengthCheckbox().addChangeListener(
				new RandomizeLengthListener());
		settingsView.getControlPanel().addOkActionListener(
				new SettingsConfirmListener());
		settingsView.getControlPanel().addCancelActionListener(
				new SettingsCancelListener());
	}
	
	/**
	 * Initializes the backend elements of the hangman app.
	 * This includes the settings and game instances.
	 */
	private void initModel() {
		settings = new Settings();
		resetModel();
	}
	
	// Public methods
	/**
	 * Starts a new hangman game, updating the engine and GUI components
	 * accordingly.
	 */
	public void resetGame() {
		resetView();		
		resetModel();
	}

	/**
	 * Resets the view to the beginning of a hangman game.
	 */
	private void resetView() {
		view.getImagePanel().reset(settings.getLives());
		view.getWordPanel().reset(settings.getWordLength());
		view.getAlphabetPanel().setAllEnabled(true);
		view.getAlphabetPanel().setAllColor(UIManager.getColor(JButton.class));
	}

	/**
	 * Resets the model to the beginning of a hangman game.
	 */
	private void resetModel() {
		game = new Game(settings.getDict(), settings.getMode(), 
				settings.getWordLength(), settings.getLives());
	}
	
	/**
	 * Runs the hangman app.
	 */
	@Override
	public void run() {
		view.setVisible(true);
	}
}
