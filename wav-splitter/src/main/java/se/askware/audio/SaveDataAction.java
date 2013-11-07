/**
 * 
 */
package se.askware.audio;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

final class SaveDataAction implements ActionListener {
	private final Properties properties;
	private File propertiesFile;
	private final TracksHandler tracks;

	SaveDataAction(Properties properties, File propertiesFile,
			TracksHandler tracks) {
		this.properties = properties;
		this.propertiesFile = propertiesFile;
		this.tracks = tracks;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (propertiesFile == null) {
			FileSelectedCallback callback = new FileSelectedCallback() {

				@Override
				public void fileSelected(File f) {
					propertiesFile = f;
				}

			};
			FileSelectHelper.showSaveFileDialog(null, callback, "Save file",
					".properties");
		}
		properties.setProperty("num.tracks", "" + tracks.getNumTracks());
		for (Track t : tracks) {
			if (t.getName() != null) {
				properties.setProperty("track." + t.getTrackId() + ".name", t
						.getName());
			}
			properties.setProperty("track." + t.getTrackId() + ".start", ""
					+ t.getStreamStartPos());
			properties.setProperty("track." + t.getTrackId() + ".end", ""
					+ t.getStreamEndPos());
		}
		try {
			properties.store(new FileOutputStream(propertiesFile), "");
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}