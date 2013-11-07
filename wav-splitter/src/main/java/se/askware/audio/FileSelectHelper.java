package se.askware.audio;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class FileSelectHelper {

	public static void showOpenFileDialog(Component parent,
			FileSelectedCallback callback, String description,
			String... acceptedTypes) {
		JFileChooser chooser = getFileChooser(description, acceptedTypes);
		if (chooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
			callback.fileSelected(chooser.getSelectedFile());
		}
	}

	public static void showSaveFileDialog(Component parent,
			FileSelectedCallback callback, String description,
			String... acceptedTypes) {
		JFileChooser chooser = getFileChooser(description, acceptedTypes);
		if (chooser.showSaveDialog(parent) == JFileChooser.APPROVE_OPTION) {
			callback.fileSelected(chooser.getSelectedFile());
		}
	}

	public static void showOpenDirDialog(Component parent,
			FileSelectedCallback callback) {
		JFileChooser chooser = new JFileChooser(new File("."));
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		if (chooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
			callback.fileSelected(chooser.getSelectedFile());
		}
	}

	private static JFileChooser getFileChooser(String description,
			String... acceptedTypes) {
		JFileChooser chooser = new JFileChooser(new File("."));
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				description, acceptedTypes);
		chooser.setFileFilter(filter);
		return chooser;
	}

	public static JButton getOpenFileButton(final Component parent,
			final FileSelectedCallback callback, final String description,
			final String... acceptedTypes) {
		JButton button = new JButton("...");
		button.setPreferredSize(new Dimension(30, 10));
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showOpenFileDialog(parent, callback, description, acceptedTypes);
			}
		});
		return button;
	}

	public static JButton getOpenDirButton(final Component parent,
			final FileSelectedCallback callback) {
		JButton button = new JButton("...");
		button.setPreferredSize(new Dimension(30, 10));
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showOpenDirDialog(parent, callback);
			}
		});
		return button;
	}
}
