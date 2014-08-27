package app.misc;

import java.awt.FlowLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * A Control Panel is the part of a settings dialog used for confirming or 
 * canceling settings changes. 
 * 
 * @author Vance Zuo
 * Created: May 13, 2013
 *
 */
public class ControlPanel extends JPanel {
	// Instance Fields	
	private JButton okButton; // Also starts a new game
	private JButton cancelButton;
	
	// Originally there was an option to either run the new settings just once,
	// or "save" them for future games. This was not included as it
	// was ultimately determined to be confusing and unnecessary.
	// private JCheckBox saveCheckBox;

	// Constructors
	/**
	 * Creates a new Control Panel with "New Game" and "Cancel" buttons.
	 */
	public ControlPanel(String okString, String cancelString) {
		setLayout(new FlowLayout(FlowLayout.RIGHT));

		// saveCheckBox = new JCheckBox("Remember");
		// saveCheckBox.setSelected(true);
		// add(saveCheckBox);

		okButton = new JButton(okString);
		add(okButton);

		cancelButton = new JButton(cancelString);
		add(cancelButton);
	}
	
	public ControlPanel() {
		this("OK", "Cancel");
	}
	
	// Public methods
	/**
	 * Adds an action listener to the okay button in the Control Panel.
	 * @param l The ActionListener object.
	 */
	public void addOkActionListener(ActionListener l) {
		okButton.addActionListener(l);
	}
	
	/**
	 * Adds an action listener to the cancel button in the Control Panel.
	 * @param l The ActionListener object.
	 */
	public void addCancelActionListener(ActionListener l) {
		cancelButton.addActionListener(l);
	}

	/**
	 * Gets the default button; that is, the one that should be pressed when
	 * the enter key is hit and the Control Panel is active.
	 * @return
	 */
	public JButton getDefaultButton() {
		return okButton;
	}
	
	/**
	 * Gets whether the user has indicated that the new settings should be 
	 * retained beyond the next game. 
	 * @return True
	 */
	public boolean isSaveChecked() {
		return true;
		// return saveCheckBox.isSelected();
	}
}