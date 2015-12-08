package com.exosoft.zerfall;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class AudioLoader {

	public Clip loadClip(String filename) {
		Clip in = null;
		try {
			AudioInputStream audioIn = AudioSystem.getAudioInputStream(getClass().getResource(filename));
			in = AudioSystem.getClip();
			in.open(audioIn);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return in;
	}

}
