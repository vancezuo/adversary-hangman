package app.settings;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import app.game.HangmanFrame;
import engine.Dict;
import engine.Mode;

/**
 * A Settings Dialog provides the user the means to change different parameters
 * of a hangman game: the dictionary of words, the word choice mode, word
 * length, and lives. Upon confirmation of changes, it starts a new game
 * with the new settings on its parent Hangman Frame.
 * 
 * @author Vance Zuo
 * Created: May 13, 2013
 * @see app.game.HangmanFrame
 *
 */
public class SettingsDialog extends JDialog {
	
	/**
	 * Classes implementing Restorable should be able to save their state
	 * before changes to them are made. The previously saved state can 
	 * then be restored, or the changes can be saved again. 
	 * <p>
	 * For objects in the Settings Dialog,
	 * this allows settings changes to be saved if the user confirms them,
	 * or the original settings to be retrieved if the user cancels the
	 * changes.
	 * 
	 * @author Vance Zuo
	 * Created: May 13, 2013
	 *
	 */
	public interface Restorable {
		/**
		 * Saves the state of the Restorable object, overwriting the previous
		 * saved state.
		 */
		void save();
		
		/**
		 * Restores the Restorable object to its previously saved state.
		 */
		void restore();
	}
	
	private static final String TITLE = "Settings";
	private static final int MARGIN = 5; // Spacing between edge and contents
	
	private static final Dict DEFAULT_DICT = new Dict();
	private static final Mode DEFAULT_MODE = Mode.ADVERSARY;
	private static final int DEFAULT_LENGTH = 4;
	private static final int DEFAULT_LIVES = 7;
	
	// Min/max values for sliders
	private static final int DEFAULT_MIN_LENGTH = DEFAULT_DICT.getMinLength();
	private static final int DEFAULT_MAX_LENGTH = DEFAULT_DICT.getMaxLength();
	private static final int DEFAULT_MIN_LIVES = 1;
	private static final int DEFAULT_MAX_LIVES = 25;
	
	// Instance Fields
	private HangmanFrame parentFrame;
	
	private JPanel contentPanel;
	
	private DictChoicePanel dictChooser;	
	private ModeChoicePanel modeChooser;	
	private JPanel slidersPanel;
	private IntSliderPanel livesChooser;	
	private IntSliderPanel lengthChooser;
	private JPanel checkboxPanel;
	private JCheckBox randomLengthCheckbox;
	private ControlPanel control; // OK/Cancel options
	
	private Component topGlue;
	private Component middleGlue;
	private Component bottomGlue;
	
	private boolean prevRandomLengthChoice;
	
	/**
	 * The Action Listener for the Control Panel (which consists of the 
	 * confirm/cancel buttons). If the user confirms the changes and the
	 * changes are valid, then the listener saves the new settings and starts 
	 * a new game on the parent Hangman Frame. Else the previous settings
	 * are retained.
	 * 
	 * @author Vance Zuo
	 * Created: May 13, 2013
	 *
	 */
	private class ControlListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			if (arg0.getActionCommand().equals("OK")) {
				int desiredLength = lengthChooser.getValue();
				if (!dictChooser.getDictionary().hasLength(desiredLength)) {
					String error = "No words of length " + desiredLength + 
							" found in the current dictionary.";
					JOptionPane.showMessageDialog(SettingsDialog.this, error,
							"Error", JOptionPane.ERROR_MESSAGE);
					System.err.println("Error: " + error);
					return;
				}
				if (control.isSaveChecked()) 
					saveSettings();
				parentFrame.resetGame();
			} else {
				restoreSettings();
			}
			SettingsDialog.this.setVisible(false);
		}	
	}

	/**
	 * Creates a new Settings Dialog with a given parent Hangman Frame to
	 * apply its settings to.
	 * 
	 * @param parent The parent Hangman Frame
	 */
	public SettingsDialog(HangmanFrame parent) {
		super(parent, TITLE);
		setModal(true); // Denies access to parent while this window is open
		setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		setIconImage(parent.getIconImage());
		
		// If user presses the X exit button, treat it as canceling changes
		// and alert the user of such.
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				String msg = "Changes will not be saved.";
				JOptionPane.showMessageDialog(SettingsDialog.this, msg);
				restoreSettings();
			}
		});
		
		parentFrame = parent;
		
		getContentPane().setLayout(new BorderLayout());
		
		// Separate content panel to combine Border and Box Layout functionality
		contentPanel = new JPanel();
		contentPanel.setBorder(new EmptyBorder(MARGIN, MARGIN, MARGIN, MARGIN));
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		{
			dictChooser = new DictChoicePanel(DEFAULT_DICT);
			// Listener for Dict Choice Panel property change event updates
			// the word length chooser limits accordingly.
			dictChooser.addPropertyChangeListener("currentDict", 
					new PropertyChangeListener () {
						@Override
						public void propertyChange(PropertyChangeEvent arg0) {
							Dict dict = dictChooser.getDictionary();
							lengthChooser.setMin(dict.getMinLength());
							lengthChooser.setMax(dict.getMaxLength());
						}			
			});
			contentPanel.add(dictChooser);
			
			topGlue = Box.createVerticalGlue();
			contentPanel.add(topGlue);
			
			modeChooser = new ModeChoicePanel(DEFAULT_MODE);
			contentPanel.add(modeChooser);
			
			middleGlue = Box.createVerticalGlue();
			contentPanel.add(middleGlue);
			
			slidersPanel = new JPanel();
			slidersPanel.setLayout(
					new BoxLayout(slidersPanel, BoxLayout.X_AXIS));
			contentPanel.add(slidersPanel);
			{
				lengthChooser = new IntSliderPanel("Word Length", 
						DEFAULT_MIN_LENGTH, DEFAULT_MAX_LENGTH, DEFAULT_LENGTH);
				slidersPanel.add(lengthChooser);
				
				livesChooser = new IntSliderPanel("Lives", 
						DEFAULT_MIN_LIVES, DEFAULT_MAX_LIVES, DEFAULT_LIVES);
				slidersPanel.add(livesChooser);
			}
			
			checkboxPanel = new JPanel();
			checkboxPanel.setBorder(new EmptyBorder(0, MARGIN, 0, 0));
			checkboxPanel.setLayout(new BorderLayout());
			contentPanel.add(checkboxPanel);
			{
				String text = "Random length each game?";
				randomLengthCheckbox = new JCheckBox(text, true);
				checkboxPanel.add(randomLengthCheckbox, BorderLayout.WEST);

				prevRandomLengthChoice = true;
			}

			bottomGlue = Box.createVerticalGlue();
			contentPanel.add(bottomGlue);
		}
					
		control = new ControlPanel();
		control.addButtonListener(new ControlListener());
		getContentPane().add(control, BorderLayout.SOUTH);
			
		// Default button = confirm changes
		getRootPane().setDefaultButton(control.getDefaultButton());
		
		pack();
	}
	
	// Public Methods
	/**
	 * Gets the Settings Dialog's currently chosen Dict of 
	 * possible hangman words.
	 * @return The current Dict.
	 */
	public Dict getDict() {
		return dictChooser.getDictionary();
	}
	
	/**
	 * Gets the Settings Dialog's currently chosen Mode for selecting 
	 * hangman words from the current Dict.
	 * @return The current Mode.
	 */
	public Mode getMode() {
		return modeChooser.getMode();
	}
	
	/**
	 * Gets the Settings Dialog's currently chosen hangman word length.
	 * @return The current word length.
	 */
	public int getWordLength() {
		return lengthChooser.getValue();
	}
	
	/**
	 * Gets the Settings Dialog's currently chosen number of lives.
	 * @return The number of lives.
	 */
	public int getLives() {
		return livesChooser.getValue();
	}
	
	/**
	 * If the Settings Dialog is set to randomize word length, then changes 
	 * the hangman word length to a new, random value. Otherwise, does nothing.
	 */
	public void updateWordLength() {
		if (randomLengthCheckbox.isSelected()) {
			lengthChooser.setValue(getDict().getRandomLength());
			lengthChooser.save();
		}
	}

	// Private Methods
	/**
	 * Permanently saves the currently selected settings.
	 */
	private void saveSettings() {
		dictChooser.save();
		modeChooser.save();
		lengthChooser.save();
		livesChooser.save();
		prevRandomLengthChoice = randomLengthCheckbox.isSelected();
	}

	/**
	 * Restores the previous settings, ignoring any changes made.
	 */
	private void restoreSettings() {
		dictChooser.restore();
		modeChooser.restore();
		lengthChooser.restore();
		livesChooser.restore();
		randomLengthCheckbox.setSelected(prevRandomLengthChoice);
	}
}
