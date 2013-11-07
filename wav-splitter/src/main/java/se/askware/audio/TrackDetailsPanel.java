package se.askware.audio;

import java.awt.BorderLayout;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class TrackDetailsPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4903833013642121393L;
	private Track track;
	private JTextField nameTextField;

	public TrackDetailsPanel(Track track) {
		this.track = track;

		setLayout(new BorderLayout());
		add(new JLabel("X"), BorderLayout.WEST);
		nameTextField = new JTextField(track.getName());
		nameTextField.addFocusListener(new TextFieldListener());
		add(nameTextField, BorderLayout.CENTER);
		JButton deleteButton = new JButton("X");
		add(deleteButton, BorderLayout.EAST);
	}

	private final class TextFieldListener extends FocusAdapter {
		@Override
		public void focusLost(FocusEvent e) {
			track.setName(nameTextField.getText());
		}
	}

}
