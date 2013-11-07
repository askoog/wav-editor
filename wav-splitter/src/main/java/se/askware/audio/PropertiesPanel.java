package se.askware.audio;

import java.awt.Dimension;
import java.awt.Image;
import java.io.File;
import java.util.Properties;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import layout.SpringUtilities;

public class PropertiesPanel extends JPanel {

	private static final long serialVersionUID = -3891112857923103920L;

	private JTextField nameTextField;

	private JTextField artistTextField;

	private JTextField genreTextField;

	private JTextField baseDirTextField;

	private JTextField imagePathTextField;
	private JLabel imageLabel;

	private JTextField lameEncoderPathTextField;

	public PropertiesPanel() {
		super();
		nameTextField = new JTextField("");
		artistTextField = new JTextField("");
		genreTextField = new JTextField("");
		baseDirTextField = new JTextField("");
		baseDirTextField.setEditable(false);
		imagePathTextField = new JTextField("");
		imagePathTextField.setEditable(false);
		imageLabel = new JLabel("");
		imageLabel.setPreferredSize(new Dimension(200, 200));
		lameEncoderPathTextField = new JTextField("");
		lameEncoderPathTextField.setEditable(false);

		setLayout(new SpringLayout());

		add(new JLabel("Album name"));

		add(nameTextField);
		nameTextField.setColumns(45);
		add(new JPanel());

		add(new JLabel("Album artist"));
		add(artistTextField);
		add(new JPanel());

		add(new JLabel("Album genre"));
		add(genreTextField);
		add(new JPanel());

		add(new JLabel("Project basedir"));
		add(baseDirTextField);
		JButton fileButton = createSelectBaseDirButton();
		add(fileButton);

		add(new JLabel("Album icon"));
		add(imagePathTextField);
		add(createSelectIconButton());

		add(new JLabel());
		add(imageLabel);
		add(new JLabel());

		add(new JLabel("Path to lame encoder:"));
		add(lameEncoderPathTextField);
		add(createSelectLameEncoderButton());

		SpringUtilities.makeCompactGrid(this, 7, 3, // rows, cols
				5, 5, // initialX, initialY
				5, 5);// xPad, yPad

	}

	private JButton createSelectBaseDirButton() {
		return FileSelectHelper.getOpenDirButton(this,
				new FileSelectedCallback() {

					@Override
					public void fileSelected(File f) {
						baseDirTextField.setText(f.getAbsolutePath());
					}

				});
	}

	private JButton createSelectIconButton() {
		FileSelectedCallback callback = new FileSelectedCallback() {

			@Override
			public void fileSelected(File selectedFile) {
				if (selectedFile.length() > 128 * 1024) {
					JOptionPane.showMessageDialog(PropertiesPanel.this,
							"File must not be lager than 128 kb. Selected file size is "
									+ selectedFile.length() / 1024
									+ " kb. Please select another file.", "",
							JOptionPane.ERROR_MESSAGE);
				} else {
					String path = selectedFile.getAbsolutePath();
					imagePathTextField.setText(path);
					viewIcon(path);
				}
			}

		};
		return FileSelectHelper.getOpenFileButton(this, callback,
				"Image files", "jpg", "png", "gif");
	}

	private JButton createSelectLameEncoderButton() {
		return FileSelectHelper.getOpenFileButton(this,
				new FileSelectedCallback() {

					@Override
					public void fileSelected(File f) {
						lameEncoderPathTextField.setText(f.getAbsolutePath());
					}

				}, "Lame executable", "exe");
	}

	public void initProperties(Properties properties) {
		nameTextField.setText(properties.getProperty("album.name"));
		artistTextField.setText(properties.getProperty("album.artist"));
		genreTextField.setText(properties.getProperty("album.genre"));
		baseDirTextField.setText(properties.getProperty("project.basedir"));
		String iconPath = properties.getProperty("album.icon");
		imagePathTextField.setText(iconPath);
		viewIcon(iconPath);
		lameEncoderPathTextField.setText(properties
				.getProperty("lame.encoder.path"));
	}

	public void setProperties(Properties properties) {
		properties.setProperty("album.name", nameTextField.getText());
		properties.setProperty("album.artist", artistTextField.getText());
		properties.setProperty("album.genre", genreTextField.getText());
		properties.setProperty("project.basedir", baseDirTextField.getText());
		properties.setProperty("album.icon", imagePathTextField.getText());
		properties.setProperty("lame.encoder.path", lameEncoderPathTextField
				.getText());
	}

	private void viewIcon(String iconPath) {
		if (iconPath == null || !new File(iconPath).exists()) {
			imageLabel.setIcon(null);
		} else {
			ImageIcon icon = new ImageIcon(iconPath);
			Image scaledImage = icon.getImage().getScaledInstance(200, 200,
					Image.SCALE_SMOOTH);
			imageLabel.setIcon(new ImageIcon(scaledImage));
		}
	}

}
