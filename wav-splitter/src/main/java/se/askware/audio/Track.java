package se.askware.audio;

public class Track implements Comparable<Track> {

	private String name;
	private long streamStartPos;
	private long streamEndPos;
	private int trackId;
	

	public int getTrackId() {
		return trackId;
	}

	public void setTrackId(int trackId) {
		this.trackId = trackId;
	}
	
	public String getFileName(String type) {
		return String.format("%02d - %s.%s", getTrackId(),getName(), type);
	}

	public String getName() {
		if (name == null) {
			return "track " + getTrackId();
		}
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int compareTo(Track o) {
		return (int) (getStreamStartPos() - o.getStreamStartPos());
	}

	public long getStreamStartPos() {
		return streamStartPos;
	}

	public void setStreamStartPos(long streamStartPos) {
		this.streamStartPos = streamStartPos;
	}

	public long getStreamEndPos() {
		return streamEndPos;
	}

	public void setStreamEndPos(long streamEndPos) {
		this.streamEndPos = streamEndPos;
	}

}
