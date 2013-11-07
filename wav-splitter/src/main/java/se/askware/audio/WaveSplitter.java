package se.askware.audio;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;

public class WaveSplitter {

	private static final int DEFAULT_HEIGHT = 500;
	private static final int DEFAULT_WIDTH = 800;
	private File propertiesFile;
	private Properties properties;
	private JFrame frame;
	private WavFilePlayer player;
	private File waveFile;
	private TableModel tableModel;
	private JPanel trackPanel;
	private AudioInfo audioInfo;
	private TracksHandler tracks;
	private SingleWaveformPanel waveDisplayPanel;
	private JTable trackTable;
	private PlayerController controller;

	public static void main(String[] args) throws Exception {
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		WaveSplitter splitter = new WaveSplitter();
		splitter.initUI();
	}

	private void initUI() {
		frame = new JFrame("Wave File Splitter");
		frame.getContentPane().setLayout(new BorderLayout());
		frame.setPreferredSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));

		JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER));
		ImageIcon icon = new ImageIcon(getClass().getResource("/page_blank.png"));
		System.out.println(icon.getIconWidth());
		JButton newButton = new JButton("New project", icon);
		newButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				properties = new Properties();
				loadProject();
			}

		});
		p.add(newButton);
		JButton openButton = new JButton("Existing project", new ImageIcon(
				getClass().getResource("/folder.png")));
		openButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					loadProperties();
					loadProject();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}

		});
		p.add(openButton);

		frame.getContentPane().add(p, BorderLayout.CENTER);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
		frame.validate();
		frame.repaint();

	}

	private void loadProject() {
		try {
			frame.getContentPane().removeAll();

			String waveFilePath = properties.getProperty("wavfile.path");
			if (waveFilePath != null) {
				waveFile = new File(waveFilePath);
			}
			if (waveFile == null || !waveFile.exists()) {
				JFileChooser chooser = new JFileChooser(new File("."));
				FileNameExtensionFilter filter = new FileNameExtensionFilter(
						"Wave File", "wav");
				chooser.setFileFilter(filter);
				int returnVal = chooser.showOpenDialog(frame);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					waveFile = chooser.getSelectedFile();
				}
			}

			audioInfo = new AudioInfo(waveFile, DEFAULT_WIDTH);
			tracks = new TracksHandler();

			tracks.loadTracks(properties);

			waveDisplayPanel = new SingleWaveformPanel(audioInfo, tracks);
			waveDisplayPanel.setMinimumSize(new Dimension(500, DEFAULT_HEIGHT));
			waveDisplayPanel.setPreferredSize(new Dimension(DEFAULT_WIDTH,
					DEFAULT_HEIGHT));

			player = new WavFilePlayer(waveFile);
			player.addPlayerPositionListener(waveDisplayPanel);
			// final JPanel detailsPanel = new JPanel(new BorderLayout());
			// detailsPanel.add(b, BorderLayout.SOUTH);
			// frame.getContentPane().add(detailsPanel, BorderLayout.SOUTH);

			controller = new PlayerController(player, tracks, waveDisplayPanel);
			KeyAdapter keyAdapter = new WavKeyAdapter(controller);

			initTrackPanel();
			// frame.getContentPane().add(new JScrollPane(new JTable(new
			// Object[][] {{"X", "Y"}}, new String[] {"A","B"})),
			// BorderLayout.SOUTH);

			waveDisplayPanel.addMouseListener(new WavMouseListener(
					waveDisplayPanel, player));

			waveDisplayPanel.addMouseMotionListener(new WavMouseMotionListener(
					waveDisplayPanel, tableModel));

			waveDisplayPanel.setFocusable(true);

			waveDisplayPanel.addKeyListener(keyAdapter);
			frame.addKeyListener(keyAdapter);
			frame.getContentPane().add(waveDisplayPanel, BorderLayout.CENTER);
			frame.getContentPane().add(trackPanel, BorderLayout.SOUTH);

			frame.pack();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void loadProperties() throws IOException, FileNotFoundException {
		JFileChooser chooser = new JFileChooser(new File("."));
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"Wave File Splitter Properties file", "properties");
		chooser.setFileFilter(filter);
		int returnVal = chooser.showOpenDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			propertiesFile = chooser.getSelectedFile();
		}
		// new File("wavesplit.properties");
		properties = new Properties();
		properties.load(new FileInputStream(propertiesFile));
	}

	private void initTrackPanel() {
		tableModel = new TracksTableModel(audioInfo, tracks);
		JButton createWavButton = new JButton();

		createWavButton.setIcon(new ImageIcon("accept.png"));
		createWavButton.setToolTipText("Create wave and mp3 files");
		createWavButton.addActionListener(new CreateWavAction(waveDisplayPanel,
				tracks, waveFile, properties));

		JButton saveButton = new JButton();
		saveButton.setIcon(new ImageIcon("save.png"));
		saveButton.setToolTipText("Save");

		saveButton.addActionListener(new SaveDataAction(properties,
				propertiesFile, tracks));

		JButton zoomInButton = new JButton(new ImageIcon("zoom_in.png"));
		zoomInButton.setToolTipText("Zoom in (up arrow)");
		zoomInButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				controller.zoomIn();
			}

		});

		JButton zoomOutButton = new JButton(new ImageIcon("zoom_out.png"));
		zoomOutButton.setToolTipText("Zoom out (down arrow)");
		zoomOutButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				controller.zoomOut();
			}

		});

		JButton deleteButton = new JButton(new ImageIcon("delete.png"));
		deleteButton.setToolTipText("Remove selected track");
		deleteButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (trackTable.getSelectedRow() >= 0) {
					tracks.removeTrack(tracks.getTrack(trackTable
							.getSelectedRow()));
					((AbstractTableModel) tableModel).fireTableDataChanged();
					waveDisplayPanel.repaint();
				}
			}

		});

		JButton propertiesButton = new JButton(new ImageIcon(
				"notebook_edit.png"));
		propertiesButton.setToolTipText("Edit project properties");
		propertiesButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				PropertiesPanel propsPanel = new PropertiesPanel();
				propsPanel.initProperties(properties);
				int result = JOptionPane.showOptionDialog(frame, propsPanel,
						"properties", JOptionPane.OK_CANCEL_OPTION,
						JOptionPane.PLAIN_MESSAGE, null, null, null);
				if (result == JOptionPane.OK_OPTION) {
					propsPanel.setProperties(properties);
				}
			}

		});

		trackPanel = new JPanel(new BorderLayout());
		trackTable = new JTable(tableModel);
		trackTable.getSelectionModel().setSelectionMode(
				ListSelectionModel.SINGLE_SELECTION);
		trackTable.getSelectionModel().addListSelectionListener(
				new ListSelectionListener() {

					@Override
					public void valueChanged(ListSelectionEvent e) {
						if (!e.getValueIsAdjusting()) {
							int selectedRow = trackTable.getSelectedRow();
							if (selectedRow >= 0) {
								Track track = tracks.getTrack(selectedRow);
								player.setPosition(track.getStreamStartPos());
							}
						}
					}

				});
		DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
		cellRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		trackTable.getColumnModel().getColumn(0).setPreferredWidth(10);
		trackTable.getColumnModel().getColumn(1).setPreferredWidth(200);
		trackTable.getColumnModel().getColumn(2).setPreferredWidth(40);
		trackTable.getColumnModel().getColumn(3).setPreferredWidth(40);
		trackTable.getColumnModel().getColumn(4).setPreferredWidth(40);
		trackTable.getColumnModel().getColumn(2).setCellRenderer(cellRenderer);
		trackTable.getColumnModel().getColumn(3).setCellRenderer(cellRenderer);
		trackTable.getColumnModel().getColumn(4).setCellRenderer(cellRenderer);
		JScrollPane scrollPane = new JScrollPane(trackTable);
		scrollPane.setPreferredSize(new Dimension(DEFAULT_WIDTH, 200));
		trackPanel.add(scrollPane, BorderLayout.CENTER);
		JPanel buttonPanel = new JPanel();
		buttonPanel.setAlignmentX(JComponent.RIGHT_ALIGNMENT);
		buttonPanel.add(saveButton);
		buttonPanel.add(propertiesButton);
		buttonPanel.add(deleteButton);
		buttonPanel.add(zoomInButton);
		buttonPanel.add(zoomOutButton);

		PlayerButtonPanel playerButtons = new PlayerButtonPanel(controller);
		buttonPanel.add(playerButtons);
		buttonPanel.add(createWavButton);

		JSlider volumeSlider = new JSlider(JSlider.VERTICAL);
		volumeSlider.setMaximum(100);
		volumeSlider.setMinimum(0);
		volumeSlider.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				JSlider slider = (JSlider) e.getSource();
				player.setVolumePercent(slider.getValue());
			}

		});
		volumeSlider.setPreferredSize(new Dimension(20, 50));
		buttonPanel.add(volumeSlider);

		trackPanel.add(buttonPanel, BorderLayout.SOUTH);
	}

	private void newProject() {
		JFileChooser chooser = new JFileChooser(new File("."));
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"Properties file", "properties");
		chooser.setFileFilter(filter);
		int returnVal = chooser.showSaveDialog(frame);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			propertiesFile = chooser.getSelectedFile();
			properties = new Properties();
		}
	}

}
