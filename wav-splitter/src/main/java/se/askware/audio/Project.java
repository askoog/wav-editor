package se.askware.audio;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.google.common.base.Optional;

public class Project {

	private enum Key {
		WAV_FILE_PATH("wavfile.path"), NUM_TRACKS("num.tracks"), ALBUM_ARTIST("album.artist"), ALBUM_NAME("album.name"), ALBUM_GENRE(
				"album.genre"), ALBUM_ICON("album.icon"), PROJECT_BASEDIR("project.basedir"), LAME_ENCODER_PATH(
				"lame.encoder.path");

		private String value;

		Key(String value) {
			this.value = value;
		}
	}

	private final Properties properties = new Properties();

	public Project() {
	}

	public Optional<File> getWavFile() {
		return getOptionalFile(getWavFilePath());
	}

	private Optional<File> getOptionalFile(Optional<String> filePath) {
		if (filePath.isPresent()) {
			return Optional.of(new File(filePath.get()));
		} else {
			return Optional.absent();
		}
	}

	private Optional<String> getWavFilePath() {
		return getProperty(Key.WAV_FILE_PATH);
	}

	private Optional<String> getProperty(Key key) {
		return Optional.fromNullable(properties.getProperty(key.value));
	}

	private void setProperty(Key key, String value) {
		properties.setProperty(key.value, value);
	}

	public void load(File propertiesFile) {
		try (InputStream in = new FileInputStream(propertiesFile)) {
			properties.load(in);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public int getNumTracks() {
		return Integer.parseInt(getProperty(Key.NUM_TRACKS).or("0"));
	}

	public void setNumTracks(int numTracks) {
		setProperty(Key.NUM_TRACKS, String.valueOf(numTracks));
	}

	public String getAlbumArtist() {
		return getProperty(Key.ALBUM_ARTIST).or("");
	}

	public void setAlbumArtist(String albumArtist) {
		setProperty(Key.ALBUM_ARTIST, albumArtist);
	}

	public String getAlbumName() {
		return getProperty(Key.ALBUM_NAME).or("");
	}

	public void setAlbumName(String albumName) {
		setProperty(Key.ALBUM_NAME, albumName);
	}

	public String getAlbumGenre() {
		return getProperty(Key.ALBUM_GENRE).or("");
	}

	public void setAlbumGenre(String albumGenre) {
		setProperty(Key.ALBUM_GENRE, albumGenre);
	}

	public Optional<File> getAlbumIcon() {
		return getOptionalFile(getProperty(Key.ALBUM_ICON));
	}

	public void setAlbumIconPath(String albumIconPath) {
		setProperty(Key.ALBUM_ICON, albumIconPath);
	}

	public File getProjectBasedir() {
		return getOptionalFile(getProperty(Key.PROJECT_BASEDIR)).or(new File(""));
	}

	public void setProjectBasedir(String baseDir) {
		setProperty(Key.PROJECT_BASEDIR, baseDir);
	}

	public void setLameEncoderPath(String path) {
		setProperty(Key.LAME_ENCODER_PATH, path);
	}

	public String getLameEncoderPath() {
		return getProperty(Key.LAME_ENCODER_PATH).or("");
	}

	// TODO: Refactor
	public TracksHandler loadTracks() {
		TracksHandler tracks = new TracksHandler();
		String numTracksString = properties.getProperty("num.tracks");
		if (numTracksString != null) {
			int numTracks = Integer.parseInt(numTracksString);
			for (int i = 1; i <= numTracks; i++) {
				long streamStartPos = Long.parseLong(properties.getProperty("track." + i + ".start"));
				long streamStartEnd = Long.parseLong(properties.getProperty("track." + i + ".end"));
				String name = properties.getProperty("track." + i + ".name");
				Track t = new Track();
				t.setStreamStartPos(streamStartPos);
				t.setStreamEndPos(streamStartEnd);
				t.setName(name);
				t.setTrackId(i);
				tracks.addTrack(t);
			}
		}
		return tracks;
	}

	public Properties getProperties() {
		// TODO Temporary during refactoring
		return properties;
	}

}
