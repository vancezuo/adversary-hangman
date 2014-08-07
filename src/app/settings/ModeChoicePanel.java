package app.settings;

import java.awt.Dimension;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.border.TitledBorder;

import engine.Mode;

import javax.swing.Box;

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
class ModeChoicePanel extends JPanel implements SettingsDialog.Restorable {

	private static final int H_GAP = 10; // Horizontal Spacing
	
	// Instance Fields
	private JComboBox<Mode> modeComboBox;
	private JLabel description;
	private JSeparator separator;
	private Component leftSpacing;
	private Component rightSpacing;
	
	private Mode prevChoice;

	// Constructors
	/**
	 * Creates a new Mode Choice Panel with given initial Mode.  
	 */
	public ModeChoicePanel(Mode initMode) {
		setBorder(new TitledBorder("Word Choice"));
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		modeComboBox = new JComboBox<Mode>();
		modeComboBox.setModel(new DefaultComboBoxModel<Mode>(Mode.values()));
		modeComboBox.setSelectedItem(initMode);
		modeComboBox.setMaximumSize(modeComboBox.getPreferredSize());
		// When a new Mode is selected, the description label is updated.
		modeComboBox.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent arg0) {
				description.setText(getMode().getDescription());
			}
		});
		add(modeComboBox);
		
		leftSpacing = Box.createHorizontalStrut(H_GAP);
		add(leftSpacing);

		description = new JLabel(getMode().getDescription());
		add(description);
		
		rightSpacing = Box.createHorizontalStrut(H_GAP);
		add(rightSpacing);
		
		// Separator fills in unused space following the description
		separator = new JSeparator();
		separator.setMaximumSize(new Dimension(
				Integer.MAX_VALUE, 
				separator.getPreferredSize().height) );
		add(separator);
		
		prevChoice = getMode();
	}
	
	public Mode getMode() {
		return (Mode) modeComboBox.getSelectedItem();
	}

	@Override
	public void save() {
		prevChoice = getMode();
	}

	@Override
	public void restore() {
		modeComboBox.setSelectedItem(prevChoice);
	}

}
