/**
 * 
 */
package se.askware.audio;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import se.askware.audio.SingleWaveformPanel.Mode;

final class WavMouseMotionListener implements MouseMotionListener {
	private final SingleWaveformPanel panel;
	private final TableModel tableModel;

	WavMouseMotionListener(SingleWaveformPanel panel, TableModel tableModel) {
		this.panel = panel;
		this.tableModel = tableModel;
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		Track track = panel.getCurrentTrack();
		if (panel.getMode() == Mode.MOVE_START) {
			track.setStreamStartPos(panel
					.viewPositionToStreamPosition(e.getX()));
			if (track.getStreamEndPos() <= track.getStreamStartPos()) {
				panel.setMode(Mode.MOVE_END);
			}
		} else if (panel.getMode() == Mode.MOVE_END) {
			track.setStreamEndPos(panel.viewPositionToStreamPosition(e.getX()));
			if (track.getStreamEndPos() <= track.getStreamStartPos()) {
				panel.setMode(Mode.MOVE_START);
			}
		} else {
			panel.setZoomEndPos(e.getX());			
		}
		panel.repaint();
		((AbstractTableModel) tableModel).fireTableDataChanged();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		panel.setMarkerPos(e.getX());

		panel.setCursor(panel.getToolkit().createCustomCursor(
	            new BufferedImage(3, 3, BufferedImage.TYPE_INT_ARGB), new Point(0, 0),
	            "null"));
		panel.setCursor(Cursor.getDefaultCursor());
		panel.setMode(Mode.NONE);

		for (Track track : panel.getTracks()) {

			Cursor resizeCursor = Cursor
					.getPredefinedCursor(Cursor.E_RESIZE_CURSOR);
			if (e.getX() == panel.streamPositionToViewPosition(track
					.getStreamStartPos())) {
				panel.setCursor(resizeCursor);
				panel.setMode(Mode.MOVE_START);
				panel.setCurrentEditedTrack(track);
				break;
			} else if (e.getX() == panel.streamPositionToViewPosition(track
					.getStreamEndPos())) {
				panel.setCursor(resizeCursor);
				panel.setMode(Mode.MOVE_END);
				panel.setCurrentEditedTrack(track);
				break;
			} else {
//				panel.setCursor(null);
			}
		}
		panel.repaint();
	}
}