package com.exosoft.zerfall;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public abstract class Audio extends Clip {

	public static Clip load(String filename) {
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
	
	public void play(){
        clip.setFramePosition(0);
        clip.start();
    }
    public void loop(){
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }
    public void stop(){
            clip.stop();
        }
    }

}
