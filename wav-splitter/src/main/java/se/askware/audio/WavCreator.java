package se.askware.audio;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.sound.sampled.AudioFileFormat.Type;

public class WavCreator {

	public void createWavFile(File srcFile, long startPos, long endPos,
			File outFile) throws FileNotFoundException,
			UnsupportedAudioFileException, IOException {

		AudioInputStream audioInputStream = AudioSystem
				.getAudioInputStream(new BufferedInputStream(new FileInputStream(srcFile)));
		skip(audioInputStream, startPos);

		AudioInputStream audioInputStream2 = new AudioInputStream(
				audioInputStream, audioInputStream.getFormat(), (endPos
						- startPos)
						/ audioInputStream.getFormat().getFrameSize());
		AudioSystem.write(audioInputStream2, Type.WAVE, outFile);

	}

	private long skip(AudioInputStream audioInputStream, long bytesToSkip)
			throws IOException {
		long skipped = 0;
		int frameSize = audioInputStream.getFormat().getFrameSize();
		while (bytesToSkip - skipped > frameSize
				&& audioInputStream.available() > frameSize) {
			skipped += audioInputStream.skip(bytesToSkip - skipped);
		}
		return skipped;
	}

	public void createWavFile(File file, Track track, File outFile)
			throws FileNotFoundException, UnsupportedAudioFileException,
			IOException {
		createWavFile(file, track.getStreamStartPos(), track.getStreamEndPos(),
				outFile);
	}
}
