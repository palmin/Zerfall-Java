package com.exosoft.zerfall;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

//Adapted from code by TdotThomas on Stack Overflow.

public class Audio {
	private Clip clip;

	public Audio(String fileName) throws UnsupportedAudioFileException,
			IOException, LineUnavailableException {
		File file = new File(fileName);
		if (file.exists()) {
			AudioInputStream sound = AudioSystem.getAudioInputStream(file);
			clip = AudioSystem.getClip();
			clip.open(sound);
		}
	}

	public void play() {
		clip.setFramePosition(0);
		clip.start();
	}

	public void loop() {
		clip.loop(Clip.LOOP_CONTINUOUSLY);
	}

	public void stop() {
		clip.stop();
	}

	public float length() {
		return clip.getMicrosecondLength();
	}
}
