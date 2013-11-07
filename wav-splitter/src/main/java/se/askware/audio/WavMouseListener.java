/**
 * 
 */
package se.askware.audio;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import se.askware.audio.SingleWaveformPanel.Mode;

final class WavMouseListener extends MouseAdapter {
	private final SingleWaveformPanel panel;
	private final WavFilePlayer player;

	WavMouseListener(SingleWaveformPanel panel, WavFilePlayer player) {
		this.panel = panel;
		this.player = player;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getClickCount() == 2) {
			panel.zoomIn(e.getX());

		} else {
			long position = panel.viewPositionToStreamPosition(e.getX());
			player.setPosition(position);
		}
		panel.requestFocus();
		panel.repaint();

	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (e.isControlDown()) {
			panel.createNewTrack(e.getX());
			panel.setMode(Mode.MOVE_END);
		}
		panel.repaint();

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		panel.setMode(Mode.NONE);
		panel.setCurrentEditedTrack(null);
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		panel.requestFocus();
	}
}