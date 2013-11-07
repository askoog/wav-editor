package se.askware.audio;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFrame;

public class WavTest extends Thread {

//	private String filename;
//
//	private Position curPosition;
//
//	private final int EXTERNAL_BUFFER_SIZE = 524288; // 128Kb
//
//	enum Position {
//		LEFT, RIGHT, NORMAL
//	};
//
//	public static void main(String[] args) {
//		new WavTest("C:\\opt\\gvkk\\Libera Me\\test.wav").start();
//	}
//
//	public WavTest(String wavfile) {
//		filename = wavfile;
//		curPosition = Position.NORMAL;
//	}
//
//	public WavTest(String wavfile, Position p) {
//		filename = wavfile;
//		curPosition = p;
//	}
//
//	public void run() {
//
//		File soundFile = new File(filename);
//		if (!soundFile.exists()) {
//			System.err.println("Wave file not found: " + filename);
//			return;
//		}
//
//		AudioInputStream audioInputStream = null;
//		try {
//			audioInputStream = AudioSystem.getAudioInputStream(soundFile);
//		} catch (UnsupportedAudioFileException e1) {
//			e1.printStackTrace();
//			return;
//		} catch (IOException e1) {
//			e1.printStackTrace();
//			return;
//		}
//
//		AudioFormat format = audioInputStream.getFormat();
//		SourceDataLine auline = null;
//		DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
//
//		System.out.println(format.getSampleSizeInBits());
//		System.out.println(format.getChannels());
//		
//		try {
//			auline = (SourceDataLine) AudioSystem.getLine(info);
//			auline.open(format);
//		} catch (LineUnavailableException e) {
//			e.printStackTrace();
//			return;
//		} catch (Exception e) {
//			e.printStackTrace();
//			return;
//		}
//
//		if (auline.isControlSupported(FloatControl.Type.PAN)) {
//			FloatControl pan = (FloatControl) auline
//					.getControl(FloatControl.Type.PAN);
//			if (curPosition == Position.RIGHT)
//				pan.setValue(1.0f);
//			else if (curPosition == Position.LEFT)
//				pan.setValue(-1.0f);
//		}
//
//		auline.start();
//		int nBytesRead = 0;
//		byte[] abData = new byte[EXTERNAL_BUFFER_SIZE];
//
//		try {
//			DataInputStream din = new DataInputStream(audioInputStream);
//			System.out.println(audioInputStream.available() / 4);
//			byte[] chunk = new byte[10000];
//			int pos = 0;
//			XYSeries series1 = new XYSeries("");
//			XYSeries series2 = new XYSeries("");
//			for (int j = 0; j < 10; j++) {
//				audioInputStream.read(chunk);
//			for (int i = 0; i < chunk.length; i += 4) {
//				short s1 = toShort(chunk[i+1], chunk[i]);
//				short s2 = toShort(chunk[i + 3], chunk[i + 2]);
//				//System.out.println(pos++ + "\t" + Integer.toHexString(chunk[i]) + Integer.toHexString(chunk[i+1]) + "\t"+  Integer.toHexString(chunk[i+2]) + Integer.toHexString(chunk[i+3]) + "\t"+ s1 + "\t" + s2);
////				series1.add(pos,-((chunk[i]-1) ^ 0xff));
////				series1.add(pos,-((chunk[i+1]-1)^ 0xff));
//				series1.add(pos,s1);
//				pos++;
//			}
//			}
//			XYSeriesCollection seriesCollection = new XYSeriesCollection(series1);
//			seriesCollection.addSeries(series2);
//		    JFreeChart chart = ChartFactory.createXYLineChart("XY Series Demo 1", "X", "Y", seriesCollection, PlotOrientation.VERTICAL, true, true, false);
//			JFrame frame = new JFrame();
//			frame.add(new ChartPanel(chart));
//			frame.pack();
//			frame.setVisible(true);
//			
//			
////			while (true) {
////				
////				// audioInputStream.reset();
////				Random random = new Random();
////				long skipped = audioInputStream.skip((random.nextBoolean() ? -1
////						: 1)
////						* random.nextInt(100000));
////				System.out.println(skipped);
////				int total = 0;
////				while (nBytesRead != -1 && total < 10000) {
////					nBytesRead = audioInputStream
////							.read(abData, 0, abData.length);
////					if (nBytesRead >= 0)
////						auline.write(abData, 0, nBytesRead);
////					total += nBytesRead;
////				}
////			}
//		} catch (IOException e) {
//			e.printStackTrace();
//			return;
//		} finally {
//			auline.drain();
//			auline.close();
//		}
//
//	}
//
//	private short toShort(byte b, byte b2) {
//		int v;
//		v = (b << 8) | b2;
//		if (((v >>> 16) & 1) == 1) {
//			v = v - 1;
//			v = v ^ 0xffff;
//			v = -v;
//		}
//		short s2 = (short) v;
//		return s2;
//	}
//
//	public short getShort(byte littleByte, byte bigByte) {
//		int v = (bigByte << 8) | littleByte;
//		if (((v >>> 16) & 1) == 1) {
//			v = v - 1;
//			v = v ^ 0xffff;
//			v = -v;
//		}
//		return (short) v;
//	}
}
