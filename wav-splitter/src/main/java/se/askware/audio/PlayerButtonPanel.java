package se.askware.audio;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

public class PlayerButtonPanel extends JPanel {

	private static final long serialVersionUID = -4179065980642432336L;

	public PlayerButtonPanel(PlayerController controller) {

		initGraphics(controller);

	}

	private void initGraphics(final PlayerController controller) {
		final JButton playButton = createImageButton("play.png");
		playButton.setToolTipText("Start playback (space bar)");
		playButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.togglePlay();
			}
		});
		controller.addPlayModeListener(new PlayerModeListener() {

			@Override
			public void playerStarted() {
				playButton.setIcon(new ImageIcon(getClass().getResource("/stop.png")));
			}

			@Override
			public void playerStopped() {
				playButton.setIcon(new ImageIcon(getClass().getResource("/play.png")));
			}

		});

		JButton forwardButton = createImageButton("fast_forward.png");
		forwardButton.setToolTipText("Fast forward (right arrow)");
		forwardButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				controller.fastForward();
			}

		});
		JButton rewindButton = createImageButton("rewind.png");
		rewindButton.setToolTipText("Fast backward (left arrow)");
		rewindButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				controller.fastRewind();
			}

		});

		JButton nextButton = createImageButton("skip_forward.png");
		nextButton
				.setToolTipText("Skip forward to track boundary (Shift + right arrow)");
		nextButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				controller.nextBoundary();
			}

		});
		JButton previousButton = createImageButton("skip_backward.png");
		previousButton
				.setToolTipText("Skip backward to track boundary (Shift + left arrow)");
		previousButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				controller.previousBoundary();

			}

		});

		setLayout(new FlowLayout());
		Dimension preferredSize = new Dimension(52, 52);
		previousButton.setPreferredSize(preferredSize);
		rewindButton.setPreferredSize(preferredSize);
		playButton.setPreferredSize(preferredSize);
		forwardButton.setPreferredSize(preferredSize);
		nextButton.setPreferredSize(preferredSize);

		add(previousButton);
		add(rewindButton);
		add(playButton);
		add(forwardButton);
		add(nextButton);
	}

	private JButton createImageButton(String iconFileName) {
		return new JButton(new ImageIcon(getClass().getResource("/" + iconFileName)));
	}

}
