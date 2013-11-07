package se.askware.audio;

import java.awt.Dimension;
import java.awt.Image;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import layout.SpringUtilities;

import com.google.common.base.Optional;

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
		return FileSelectHelper.getOpenDirButton(this, new FileSelectedCallback() {

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
							"File must not be lager than 128 kb. Selected file size is " + selectedFile.length() / 1024
									+ " kb. Please select another file.", "", JOptionPane.ERROR_MESSAGE);
				} else {
					String path = selectedFile.getAbsolutePath();
					imagePathTextField.setText(path);
					viewIcon(Optional.of(selectedFile));
				}
			}

		};
		return FileSelectHelper.getOpenFileButton(this, callback, "Image files", "jpg", "png", "gif");
	}

	private JButton createSelectLameEncoderButton() {
		return FileSelectHelper.getOpenFileButton(this, new FileSelectedCallback() {

			@Override
			public void fileSelected(File f) {
				lameEncoderPathTextField.setText(f.getAbsolutePath());
			}

		}, "Lame executable", "exe");
	}

	public void initProperties(Project project) {
		nameTextField.setText(project.getAlbumName());
		artistTextField.setText(project.getAlbumArtist());
		genreTextField.setText(project.getAlbumGenre());
		baseDirTextField.setText(project.getProjectBasedir().getAbsolutePath());
		Optional<File> iconPath = project.getAlbumIcon();
		imagePathTextField.setText(iconPath.or(new File("")).getAbsolutePath());
		viewIcon(iconPath);
		lameEncoderPathTextField.setText(project.getLameEncoderPath());
	}

	public void setProperties(Project project) {
		project.setAlbumName(nameTextField.getText());
		project.setAlbumArtist(artistTextField.getText());
		project.setAlbumGenre(genreTextField.getText());
		project.setProjectBasedir(baseDirTextField.getText());
		project.setAlbumIconPath(imagePathTextField.getText());
		project.setLameEncoderPath(lameEncoderPathTextField.getText());
	}

	private void viewIcon(Optional<File> iconFile) {
		if (iconFile.isPresent() && iconFile.get().exists()) {
			ImageIcon icon = new ImageIcon(iconFile.get().getAbsolutePath());
			Image scaledImage = icon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
			imageLabel.setIcon(new ImageIcon(scaledImage));
		} else {
			imageLabel.setIcon(null);
		}
	}

}
