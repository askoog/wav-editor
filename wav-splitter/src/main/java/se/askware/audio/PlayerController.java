package se.askware.audio;

import java.awt.Cursor;

public class PlayerController {

	Cursor waitCursor = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR);
	Cursor normalCursor = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);

	WavFilePlayer player;
	TracksHandler tracks;
	SingleWaveformPanel wavDisplayPanel;

	public PlayerController(WavFilePlayer player, TracksHandler tracks,
			SingleWaveformPanel wavPanel) {
		super();
		this.player = player;
		this.tracks = tracks;
		this.wavDisplayPanel = wavPanel;
	}

	public void forward() {
		player.forward(false);
	}

	public void fastForward() {
		player.forward(true);
	}

	public void rewind() {
		player.rewind(false);
	}

	public void fastRewind() {
		player.rewind(true);
	}

	public void nextBoundary() {
		long newPos = tracks.getNextTrackBoundary(player.getCurrentPosition());
		player.setPosition(newPos);
		wavDisplayPanel.center(newPos);
	}

	public void previousBoundary() {
		long newPos = tracks.getPreviousTrackBoundary(player
				.getCurrentPosition());
		player.setPosition(newPos);
		wavDisplayPanel.center(newPos);
	}

	public void togglePlay() {
		player.toggle();
	}

	public void zoomIn() {
		wavDisplayPanel.setCursor(waitCursor);
		wavDisplayPanel.zoomIn(wavDisplayPanel
				.streamPositionToViewPosition(player.getCurrentPosition()));
		wavDisplayPanel.setCursor(normalCursor);
	}

	public void zoomOut() {
		wavDisplayPanel.setCursor(waitCursor);
		wavDisplayPanel.zoomOut();
		wavDisplayPanel.setCursor(normalCursor);
	}

	public void addPlayModeListener(PlayerModeListener playerModeListener) {
		player.addPlayModeListener(playerModeListener);
	}

}
