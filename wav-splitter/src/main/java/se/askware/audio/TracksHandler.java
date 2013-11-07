package se.askware.audio;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

public class TracksHandler implements Iterable<Track> {

	private List<Track> tracks = new ArrayList<Track>();

	private Track currentTrack;

	@Override
	public Iterator<Track> iterator() {
		return tracks.iterator();
	}

	public void addTrack(Track track) {
		tracks.add(track);
		updateTrackIds();
	}

	public void removeTrack(Track track) {
		tracks.remove(track);
		updateTrackIds();
	}

	private void updateTrackIds() {
		Collections.sort(tracks);
		for (int i = 0; i < tracks.size(); i++) {
			tracks.get(i).setTrackId(i + 1);
		}
	}

	public long getNextTrackBoundary(long currentPosition) {
		long best = Long.MAX_VALUE;
		long bestDiff = Long.MAX_VALUE;
		for (Track track : tracks) {
			long diff = track.getStreamEndPos() - currentPosition;
			if (diff > 0 && diff < bestDiff) {
				best = track.getStreamEndPos();
				bestDiff = diff;
			}
			diff = track.getStreamStartPos() - currentPosition;
			if (diff > 0 && diff < bestDiff) {
				best = track.getStreamStartPos();
				bestDiff = diff;
			}
		}
		return best;
	}

	public long getPreviousTrackBoundary(long currentPosition) {
		long best = 0;
		long bestDiff = Long.MAX_VALUE;
		for (Track track : tracks) {
			long diff = currentPosition - track.getStreamEndPos();
			if (diff > 0 && diff < bestDiff) {
				best = track.getStreamEndPos();
				bestDiff = diff;
			}
			diff = currentPosition - track.getStreamStartPos();
			if (diff > 0 && diff < bestDiff) {
				best = track.getStreamStartPos();
				bestDiff = diff;
			}
		}
		return best;
	}

	public int getNumTracks() {
		return tracks.size();
	}

	public Track getTrack(int rowIndex) {
		Collections.sort(tracks);
		return tracks.get(rowIndex);
	}

	public void loadTracks(Properties properties) {
		String numTracksString = properties.getProperty("num.tracks");
		if (numTracksString != null) {
			int numTracks = Integer.parseInt(numTracksString);
			for (int i = 1; i <= numTracks; i++) {
				long streamStartPos = Long.parseLong(properties
						.getProperty("track." + i + ".start"));
				long streamStartEnd = Long.parseLong(properties
						.getProperty("track." + i + ".end"));
				String name = properties.getProperty("track." + i + ".name");
				Track t = new Track();
				t.setStreamStartPos(streamStartPos);
				t.setStreamEndPos(streamStartEnd);
				t.setName(name);
				t.setTrackId(i);
				addTrack(t);
			}
		}
	}

	public Track getCurrentTrack() {
		return currentTrack;
	}

	public void setCurrentTrack(Track currentTrack) {
		this.currentTrack = currentTrack;
	}

}
