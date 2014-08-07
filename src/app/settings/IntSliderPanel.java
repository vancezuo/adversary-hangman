package app.settings;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.Component;
import javax.swing.Box;

/**
 * An Int Slider Panel is the part of the Settings Dialog for setting 
 * numerical parameters -- word length and lives. It consists of a 
 * JSlider, and a JLabel denoting the selected value.
 * 
 * @author Vance Zuo
 * Created: May 13, 2013
 *
 */
class IntSliderPanel extends JPanel implements SettingsDialog.Restorable {
	
	private static final int H_GAP = 5; // Horizontal Spacing

	// Instance Fields
	private JSlider slider;
	private JLabel value;
	
	private Component horizontalStrut;
	
	private int prevValue;

	
	// Constructors
	/**
	 * Creates a new Int Slider Panel with given title name, min/max values,
	 * and starting value. 
	 * @param min  The lowest selectable value
	 * @param max  The highest selectable value
	 * @param init The initially selected value
	 */
	public IntSliderPanel(String title, int min, int max, int init) {
		setBorder(new TitledBorder(title));
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		slider = new JSlider(min, max, init);
		slider.setMinorTickSpacing(1);
		slider.setPaintTicks(true);
		slider.setSnapToTicks(true);
		// Upon slide, label displaying selected value is updated
		slider.addChangeListener(new ChangeListener() {		
			@Override public void stateChanged(ChangeEvent e) {
				value.setText(String.valueOf(getValue()));
			}
		});
		add(slider);
		
		horizontalStrut = Box.createHorizontalStrut(H_GAP);
		add(horizontalStrut);
		
		value = new JLabel(String.valueOf(getValue()));
		add(value);
		
		prevValue = getValue();
	}
	
	// Public Methods
	/**
	 * Gets the selected (numerical) value of the Int Slider Panel.
	 * @return The current value of the panel's slider.
	 */
	public int getValue() {
		return slider.getValue();
	}
	
	/**
	 * Sets the selected (numerical) value of the Int Slider Panel to a 
	 * new value, if that value is within the minimum/maximum bounds of the 
	 * slider's selectable values.
	 */
	public void setValue(int n) {
		if (n >= slider.getMinimum() && n <= slider.getMaximum())
			slider.setValue(n);
	}
	
	/**
	 * Sets the minimum selectable value in the Int Slider Panel.
	 * @param newMin The new minimum value
	 */
	public void setMin(int newMin) {
		slider.setMinimum(newMin);
	}
	
	/**
	 * Sets the maximum selectable value in the Int Slider Panel.
	 * @param newMax The new maximum value
	 */

	public void setMax(int newMax) {
		slider.setMaximum(newMax);
	}

	@Override
	public void save() {
		prevValue = getValue();
	}

	@Override
	public void restore() {
		slider.setValue(prevValue);
	}

}
