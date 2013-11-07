/**
 * 
 */
package se.askware.audio;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

final class WavKeyAdapter extends KeyAdapter {
	private PlayerController player;

	WavKeyAdapter(PlayerController player) {
		this.player = player;
	}

	public void keyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_SPACE:
			player.togglePlay();
			break;
		case KeyEvent.VK_RIGHT:
			if (e.isShiftDown()) {
				player.nextBoundary();
			} else if (e.isControlDown()) {
				player.fastForward();
			} else {
				player.forward();
			}
			break;
		case KeyEvent.VK_LEFT:
			if (e.isShiftDown()) {
				player.previousBoundary();
			} else if (e.isControlDown()) {
				player.fastRewind();
			} else {
				player.rewind();
			}
			break;
		case KeyEvent.VK_UP:
			player.zoomIn();
			break;
		case KeyEvent.VK_DOWN:
			player.zoomOut();
			break;
		}

	}
}