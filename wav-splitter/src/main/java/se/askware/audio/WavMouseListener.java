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
		if (e.getButton() != MouseEvent.BUTTON2) {
			panel.zoomOut();
		} else if (e.getClickCount() == 2) {
			panel.zoomIn(e.getX());

		} else {
			long position = panel.viewPositionToStreamPosition(e.getX());
			player.setPosition(position);
		}
		panel.requestFocus();
		panel.repaint();
		System.out.println("CLick");
		panel.setMode(Mode.NONE);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (e.isControlDown()) {
			panel.createNewTrack(e.getX());
			panel.setMode(Mode.MOVE_END);
		} else {
			System.out.println(e.isConsumed());
			panel.setMode(Mode.ZOOM);
			System.out.println("Zoom");
			panel.setZoomStartPos(e.getX());
		}
		panel.setMarkerPos(0);
		panel.repaint();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		System.out.println("release");

		if (panel.getMode() == Mode.ZOOM) {
			panel.setZoomEndPos(e.getX());
			panel.zoomSelection();
		}
		panel.setMode(Mode.NONE);
		panel.setCurrentEditedTrack(null);
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		panel.requestFocus();
	}

	@Override
	public void mouseExited(MouseEvent e) {
		panel.setMarkerPos(0);
		panel.repaint();
	}
}