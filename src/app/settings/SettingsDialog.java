package app.settings;

import java.awt.BorderLayout;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import app.misc.ControlPanel;
import app.misc.IntSliderPanel;

/** 
 * A Settings Dialog provides the user the means to change different parameters
 * of a hangman game: the dictionary of words, the word choice mode, word
 * length, and lives.
 * 
 * @author Vance Zuo
 * Created: May 13, 2013
 *
 */
public class SettingsDialog extends JDialog {
	// Constants
	private static final String TITLE = "Settings";
	private static final int MARGIN = 5; // Spacing between edge and contents

	// Instance Fields
	private JPanel mainPanel;
	private DictChooserPanel dictChooser;	
	private ModeChooserPanel modeChooser;	
	private JPanel slidersPanel;
	private IntSliderPanel livesChooser;	
	private IntSliderPanel lengthChooser;
	private JPanel checkboxPanel;
	private JCheckBox randLengthCheckbox;
	
	private ControlPanel controlPanel; // OK/Cancel options

	/**
	 * Creates a new settings panel.
	 */
	public SettingsDialog(JFrame parent) {
		super(parent, TITLE);
		setLayout(new BorderLayout());
		setModal(true); // Denies access to parent while this window is open
		setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		setIconImage(parent.getIconImage());

		initMainPanel();
		initControlPanel();
		
		getRootPane().setDefaultButton(controlPanel.getDefaultButton());
		
		pack();
	}

	/**
	 * Initializes and adds the main panel, which contains everything other
	 * than the control panel.
	 */
	private void initMainPanel() {
		mainPanel = new JPanel();
		mainPanel.setBorder(new EmptyBorder(MARGIN, MARGIN, MARGIN, MARGIN));
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		getContentPane().add(mainPanel, BorderLayout.CENTER);
		
		initDictChooser();
		addVerticalGlue();
		initModeChooser();
		addVerticalGlue();
		initSliders();
		initRandLengthCheckbox(BorderLayout.WEST);
		addVerticalGlue();
	}

	/**
	 * Initializes and adds the word chooser mode combo box.
	 */
	private void initModeChooser() {
		modeChooser = new ModeChooserPanel(Settings.DEFAULT_MODE);
		mainPanel.add(modeChooser);
	}

	/**
	 * Initializes and adds the dictionary file chooser.
	 */
	private void initDictChooser() {
		dictChooser = new DictChooserPanel();
		mainPanel.add(dictChooser);
	}

	/**
	 * Initializes and adds the word length and lives sliders.
	 */
	private void initSliders() {
		slidersPanel = new JPanel();
		slidersPanel.setLayout(new BoxLayout(slidersPanel, BoxLayout.X_AXIS));
		mainPanel.add(slidersPanel);
		
		lengthChooser = new IntSliderPanel("Word Length",
				Settings.DEFAULT_MIN_LENGTH, Settings.DEFAULT_MAX_LENGTH,
				Settings.DEFAULT_LENGTH);
		slidersPanel.add(lengthChooser);

		livesChooser = new IntSliderPanel("Lives", 
				Settings.DEFAULT_MIN_LIVES,	Settings.DEFAULT_MAX_LIVES, 
				Settings.DEFAULT_LIVES);
		slidersPanel.add(livesChooser);
	}

	/**
	 * Initializes and adds the random length each game check box.
	 * This specifies whether each new hangman game should use a
	 * randomized or fixed word length.
	 * @param orientation the relative location of the checkbox (i.e. which
	 *                    direction the component should be 'justified') as
	 *                    a BorderLayout constant.
	 */
	private void initRandLengthCheckbox(String orientation) {
		checkboxPanel = new JPanel();
		checkboxPanel.setBorder(new EmptyBorder(0, MARGIN, 0, 0));
		checkboxPanel.setLayout(new BorderLayout());
		mainPanel.add(checkboxPanel);
		
		randLengthCheckbox = new JCheckBox("Random length each game?", true);
		checkboxPanel.add(randLengthCheckbox, orientation);
	}

	/**
	 * Adds vertical glue (for creating spacing between components).
	 */
	private void addVerticalGlue() {
		mainPanel.add(Box.createVerticalGlue());
	}

	/**
	 * Initializes and adds the ok/cancel control panel.
	 */
	private void initControlPanel() {
		controlPanel = new ControlPanel();
		getContentPane().add(controlPanel, BorderLayout.SOUTH);
	}
	
	
	// Public methods
	/**
	 * Gets the Settings Dialog's dictionary chooser.
	 * @return this dialog's Dict Chooser instance
	 */
	public DictChooserPanel getDictChooser() {
		return dictChooser;
	}
	
	/**
	 * Gets the Settings Dialog's mode chooser.
	 * @return this dialog's Mode Chooser Panel instance
	 */
	public ModeChooserPanel getModeChooser() {
		return modeChooser;
	}

	/**
	 * Gets the Settings Dialog's livers chooser.
	 * @return this dialog's Int Slider Panel instance for hangman lives
	 */
	public IntSliderPanel getLivesChooser() {
		return livesChooser;
	}

	/**
	 * Gets the Settings Dialog's length chooser.
	 * @return this dialog's Int Slider Panel instance for hangman word length
	 */
	public IntSliderPanel getLengthChooser() {
		return lengthChooser;
	}

	/**
	 * Gets the Settings Dialog's word length randomizer flag.
	 * @return this dialog's length randomize checkbox instance
	 */
	public JCheckBox getRandomLengthCheckbox() {
		return randLengthCheckbox;
	}

	/**
	 * Gets the Settings Dialog's control panel.
	 * @return this dialog's Control Panel instance
	 */
	public ControlPanel getControlPanel() {
		return controlPanel;
	}	
}
