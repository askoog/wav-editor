package se.askware.audio;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * Created by IntelliJ IDEA. User: Jonathan Simon Date: Mar 6, 2005 Time:
 * 8:48:28 PM To change this template use File | Settings | File Templates.
 */
public class AudioInfo {
	private static final int NUM_BITS_PER_BYTE = 8;

	private AudioInputStream audioInputStream;
	private int[][] samplesContainer;

	// cached values
	protected int sampleMax = 0;
	protected int sampleMin = 0;
	protected double biggestSample;
	protected int numberOfChannels = 0;
	
	private long startPos;
	private long endPos;

	private File wavFile;

	private long streamLength;

	public AudioInfo(File wavFile, int initialNumSamples)
			throws FileNotFoundException, UnsupportedAudioFileException,
			IOException {
		this.wavFile = wavFile;
		initAudioStream();

		startPos = 0;
		endPos = audioInputStream.getFormat().getFrameSize()
				* audioInputStream.getFrameLength();
		streamLength = endPos;
		createSampleArrayCollection(0, endPos, initialNumSamples);
		// createSampleArrayCollection();
	}

	private void initAudioStream() throws UnsupportedAudioFileException,
			IOException, FileNotFoundException {
		if (audioInputStream != null) {
			audioInputStream.close();
		}
		this.audioInputStream = AudioSystem
				.getAudioInputStream(new BufferedInputStream(new FileInputStream(wavFile)));
		initNumberOfChannels();
	}

	public int getNumberOfChannels() {
		return numberOfChannels;
	}

	private void initNumberOfChannels() {
		int numBytesPerSample = audioInputStream.getFormat()
				.getSampleSizeInBits()
				/ NUM_BITS_PER_BYTE;
		numberOfChannels = audioInputStream.getFormat().getFrameSize() / numBytesPerSample;
	}

	public void createSampleArrayCollection(int numSamples) {
		createSampleArrayCollection(getStartPos(), getEndPos(), numSamples);
	}

	public void createSampleArrayCollection(long startPos, long endPos,
			int numSamples) {
		sampleMax = 0;
		sampleMin = 0;
		biggestSample = 0;
		try {
			initAudioStream();
			long skipped = skip(startPos);
			System.out.println(startPos + " " + skipped);
			long streamLength = endPos - startPos;

			int sampleSize = getNumberOfChannels() * 2;
			byte[] samples = new byte[numSamples * sampleSize];
			int offset = 0;

			int read = 0;
			int totalRead = 0;
			while (read >= 0 && offset < samples.length) {
				int chunkSize = (int) (streamLength / numSamples);
				byte[] chunk = new byte[sampleSize];
				read = audioInputStream.read(chunk);
				read += skip(chunkSize - sampleSize);
				// System.out.println(" " + chunkSize + " " + read);
				System.arraycopy(chunk, 0, samples, offset, sampleSize);
				streamLength -= read;
				numSamples--;
				offset += sampleSize;
				totalRead += read;
				samplesContainer = getSampleArray(samples);
			}
			System.out.println(totalRead);
			System.out.println(endPos);
			// long rewind = audioInputStream.skip(-totalRead);
			// System.out.println(rewind);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private long skip(long bytesToSkip) throws IOException {
		long skipped = 0;
		int frameSize = audioInputStream.getFormat().getFrameSize();
		while (bytesToSkip - skipped > frameSize
				&& audioInputStream.available() > frameSize) {
			skipped += audioInputStream.skip(bytesToSkip - skipped);
		}
		return skipped;
	}

	protected int[][] getSampleArray(byte[] eightBitByteArray) {
		int[][] toReturn = new int[getNumberOfChannels()][eightBitByteArray.length
				/ (2 * getNumberOfChannels())];
		int index = 0;

		// loop through the byte[]
		for (int t = 0; t < eightBitByteArray.length;) {
			// for each iteration, loop through the channels
			for (int a = 0; a < getNumberOfChannels(); a++) {
				// do the byte to sample conversion
				// see AmplitudeEditor for more info
				int low = (int) eightBitByteArray[t];
				t++;
				int high = (int) eightBitByteArray[t];
				t++;
				int sample = (high << 8) + (low & 0x00ff);

				if (sample < sampleMin) {
					sampleMin = sample;
				} else if (sample > sampleMax) {
					sampleMax = sample;
				}
				// set the value.
				toReturn[a][index] = sample;
			}
			index++;
		}
		biggestSample = Math.max(sampleMax, Math.abs(sampleMin));

		return toReturn;
	}

	// public double getXScaleFactor(int panelWidth) {
	// return (panelWidth / ((double) samplesContainer[0].length));
	// }
	//
	public double getYScaleFactor(int panelHeight) {
		return (panelHeight / (biggestSample * 2 * 1.2));
	}

	public int[] getAudio(int channel) {
		return samplesContainer[channel];
	}

	// protected int getIncrement(double xScale) {
	// try {
	// int increment = (int) (samplesContainer[0].length /
	// (samplesContainer[0].length * xScale));
	// return increment;
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// return -1;
	// }

	public long getStartPos() {
		return startPos;
	}

	public long getEndPos() {
		return endPos;
	}

	public void setStartPos(long startPos) {
		if (startPos < 0) {
			this.startPos = 0;
		} else if (startPos > streamLength) {
			this.startPos = streamLength;
		} else {
			this.startPos = startPos;
		}
	}

	public void setEndPos(long endPos) {
		if (endPos < 0) {
			this.endPos = 0;
		} else if (endPos > streamLength) {
			this.endPos = streamLength;
		} else {
			this.endPos = endPos;
		}
	}

	public double toSeconds(long position) {
		AudioFormat format = audioInputStream.getFormat();
		return position / (format.getFrameRate()
				* format.getFrameSize()* 1.0);
	}
	
	public String toTimeStamp(long position) {
		double sec = toSeconds(position);
		return String.format("%02d:%02d", (int)(sec / 60), (int)(sec % 60));
		
	}
}
