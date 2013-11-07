/**
 * 
 */
package se.askware.audio;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Properties;

final class CreateWavAction implements ActionListener {
	private final SingleWaveformPanel panel;
	private final TracksHandler tracks;
	private final File file;
	private final Properties properties;

	CreateWavAction(SingleWaveformPanel panel,
			TracksHandler tracks, File file, Properties properties) {
		this.panel = panel;
		this.tracks = tracks;
		this.file = file;
		this.properties = properties;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			File baseDir = new File(properties
					.getProperty("project.basedir"));
			if (!baseDir.exists()) {
				baseDir.mkdirs();
			}
			File wavDir = new File(baseDir, "wav");
			File mp3Dir = new File(baseDir, "mp3");
			wavDir.mkdirs();
			mp3Dir.mkdirs();
			LameEncoder encoder = new LameEncoder(new File(
					"lame/lame.exe"));
			for (Track track : tracks) {
				File wavFile = new File(wavDir, track
						.getFileName("wav"));
				File mp3File = new File(mp3Dir, track
						.getFileName("mp3"));
				new WavCreator()
						.createWavFile(file, track, wavFile);
				encoder.encode(wavFile, mp3File, track, properties);
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		panel.requestFocus();
	}
}