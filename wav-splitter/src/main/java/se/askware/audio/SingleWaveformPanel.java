package se.askware.audio;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JPanel;

public class SingleWaveformPanel extends JPanel implements
		PlayerPositionListener, ComponentListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5247859527018719223L;
	protected static final Color BACKGROUND_COLOR = Color.white;
	protected static final Color REFERENCE_LINE_COLOR = Color.black;
	protected static final Color[] WAVEFORM_COLOR = new Color[] {
			Color.DARK_GRAY, Color.LIGHT_GRAY };
	private static final Color PLAY_MARKER_COLOR = Color.CYAN;

	private AudioInfo audio;

	private long playerPosition;

	enum Mode {
		MOVE_START, MOVE_END, NONE
	}

	private Mode mode;
	private TracksHandler tracks;
	private Track currentEditedTrack = null;
	private Track currentPlayedTrack = null;

	public SingleWaveformPanel(AudioInfo audio, TracksHandler tracks) {
		this.audio = audio;
		this.tracks = tracks;
		setBackground(BACKGROUND_COLOR);
		addComponentListener(this);
	}

	protected void paintComponent(Graphics g) {
		System.out.println("r");
		super.paintComponent(g);

		int lineHeight = getHeight() / 2;
		g.setColor(REFERENCE_LINE_COLOR);
		g.drawLine(0, lineHeight, (int) getWidth(), lineHeight);

		for (int i = 0; i < audio.getNumberOfChannels(); i++) {
			drawWaveform(g, audio.getAudio(i), WAVEFORM_COLOR[i]);
		}

		g.setColor(PLAY_MARKER_COLOR);
		int pPos = streamPositionToViewPosition(playerPosition);
		g.drawLine(pPos, 0, pPos, getHeight());
		// System.out.println(pPos);

		for (Track track : tracks) {
			Color trackColor = Color.GREEN;

			if (track == currentPlayedTrack) {
				trackColor = Color.BLUE;
			} else if (track == currentEditedTrack) {
				trackColor = Color.RED;
			}
			int x1 = streamPositionToViewPosition(track.getStreamStartPos());
			int x2 = streamPositionToViewPosition(track.getStreamEndPos());

			g.setColor(new Color(trackColor.getRed(), trackColor.getGreen(),
					trackColor.getBlue(), 20));
			g.fillRect(x1, 0, x2 - x1, getHeight());
			g.setColor(trackColor);
			g.drawLine(x1, 0, x1, getHeight());
			g.drawLine(x2, 0, x2, getHeight());
		}

	}

	protected void drawWaveform(Graphics g, int[] samples, Color color) {
		if (samples == null) {
			return;
		}

		int oldX = 0;
		int oldY = (int) (getHeight() / 2);
		int xIndex = 0;

		int increment = 1;//helper.getIncrement(helper.getXScaleFactor(getWidth(
		// )));
		g.setColor(color);

		int t = 0;

		for (t = 0; t < increment; t += increment) {
			g.drawLine(oldX, oldY, xIndex, oldY);
			xIndex++;
			oldX = xIndex;
		}

		for (; t < samples.length; t += increment) {
			double scaleFactor = audio.getYScaleFactor(getHeight());
			double scaledSample = samples[t] * scaleFactor;
			int y = (int) ((getHeight() / 2) - (scaledSample));
			g.drawLine(oldX, oldY, xIndex, y);

			oldX = xIndex;
			oldY = y;
			xIndex++;
		}
	}

	public Track createNewTrack(int viewPosition) {
		Track track = new Track();
		track.setStreamStartPos(viewPositionToStreamPosition(viewPosition));
		track.setStreamEndPos(viewPositionToStreamPosition(viewPosition));
		tracks.addTrack(track);
		setCurrentEditedTrack(track);
		return track;

	}

	public Mode getMode() {
		return mode;
	}

	public void setMode(Mode mode) {
		this.mode = mode;
	}

	public Track getCurrentTrack() {
		return currentEditedTrack;
	}

	public void setCurrentEditedTrack(Track currentTrack) {
		this.currentEditedTrack = currentTrack;
	}

	public Iterable<Track> getTracks() {
		return tracks;
	}

	public long viewPositionToStreamPosition(int viewXPosition) {
		long span = audio.getEndPos() - audio.getStartPos();
		return (long) (span * (viewXPosition / (getWidth() * 1.0)) + audio
				.getStartPos());
	}

	public int streamPositionToViewPosition(long streamPosition) {
		long span = audio.getEndPos() - audio.getStartPos();
		double xPos = ((streamPosition - audio.getStartPos()) / (span * 1.0))
				* getWidth();
		return (int) xPos;
	}

	public void zoomIn() {
		zoomIn(getWidth() / 2);
	}

	public void zoomIn(int centerXPosition) {
		long span = audio.getEndPos() - audio.getStartPos();
		long context = span / 4;
		long center = viewPositionToStreamPosition(centerXPosition);
		audio.setStartPos(center - context);
		audio.setEndPos(center + context);
		long time = System.currentTimeMillis();
		new ReaderThread().start();
		//audio.createSampleArrayCollection(getWidth());
		System.out.println((System.currentTimeMillis() - time) + "ms");
		repaint();
	}

	public void zoomOut() {
		long span = audio.getEndPos() - audio.getStartPos();
		long skip = span / 2;
		audio.setStartPos(audio.getStartPos() - skip);
		audio.setEndPos(audio.getEndPos() + skip);
		new ReaderThread().start();
		//audio.createSampleArrayCollection(getWidth());
		repaint();
	}

	private class ReaderThread extends Thread{
		@Override
		public void run() {
			audio.createSampleArrayCollection(getWidth());
		}
	}
	
	@Override
	public void positionChanged(long newPosition) {
		playerPosition = newPosition;

		if (currentPlayedTrack != null) {
			if (playerPosition < currentPlayedTrack.getStreamStartPos()
					|| playerPosition > currentPlayedTrack.getStreamEndPos()) {
				currentPlayedTrack = null;
			}
		} else {
			for (Track track : tracks) {
				if (playerPosition >= track.getStreamStartPos()
						&& playerPosition <= track.getStreamEndPos()) {
					currentPlayedTrack = track;
					break;
				}
			}
		}

		repaint();
	}

	public void center(long center) {
		long span = audio.getEndPos() - audio.getStartPos();
		long context = span / 2;
		audio.setStartPos(center - context);
		audio.setEndPos(center + context);
		audio.createSampleArrayCollection(getWidth());
		repaint();
	}

	@Override
	public void componentHidden(ComponentEvent e) {
	}

	@Override
	public void componentMoved(ComponentEvent e) {
	}

	@Override
	public void componentResized(ComponentEvent e) {
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		audio.createSampleArrayCollection(getWidth());
		repaint();
		setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}

	@Override
	public void componentShown(ComponentEvent e) {
	}

}
