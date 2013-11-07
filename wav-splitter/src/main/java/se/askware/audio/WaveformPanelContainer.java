package se.askware.audio;
import javax.swing.*;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.UnsupportedAudioFileException;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Jonathan Simon
 * Date: Mar 20, 2005
 * Time: 5:08:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class WaveformPanelContainer extends JPanel {
    private ArrayList<SingleWaveformPanel> singleChannelWaveformPanels = new ArrayList<SingleWaveformPanel>();
    private AudioInfo audioInfo = null;

    public WaveformPanelContainer() {
        setLayout(new GridLayout(0,1));
    }

    public void setAudioToDisplay(File wavFile) throws FileNotFoundException, UnsupportedAudioFileException, IOException{
        singleChannelWaveformPanels = new ArrayList<SingleWaveformPanel>();
        audioInfo = new AudioInfo(wavFile, 1000);
        for (int t=0; t<audioInfo.getNumberOfChannels(); t++){
            SingleWaveformPanel waveformPanel
                    = new SingleWaveformPanel(audioInfo, new TracksHandler());
            singleChannelWaveformPanels.add(waveformPanel);
            add(createChannelDisplay(waveformPanel, t));
        }
    }

    private JComponent createChannelDisplay(SingleWaveformPanel waveformPanel, int index) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(waveformPanel, BorderLayout.CENTER);

        JLabel label = new JLabel("Channel " + ++index);
        panel.add(label, BorderLayout.NORTH);

        return panel;
    }
    
    public ArrayList<SingleWaveformPanel> getSingleChannelWaveformPanels() {
		return singleChannelWaveformPanels;
	}

    public AudioInfo getAudioInfo() {
		return audioInfo;
	}

}
