package app.settings;

import app.misc.ComboBoxPanel;
import engine.Mode;

/**
 * A Mode Choice Panel is the part of the Settings Dialog for choosing the 
 * word choice Mode in a hangman game. It consists of a drop-down
 * JComboBox and a JLabel briefly describing the Mode.
 * 
 * @author Vance Zuo
 * Created: May 13, 2013
 * @see engine.Mode
 *
 */
public class ModeChooserPanel extends ComboBoxPanel<Mode> {
	// Constructors
	/**
	 * Creates a new Mode Choice Panel with given initial Mode.  
	 */
	public ModeChooserPanel(Mode initMode) {
		super("Word Choice Mode", Mode.getModeDescriptionMap(), initMode);
	}
	
	
	// Public methods
	public Mode getMode() {
		return super.getSelectedItem();
	}
	
}
