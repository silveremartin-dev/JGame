package org.jgame.utils;

import javafx.scene.media.AudioClip;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Centralized manager for playing game sound effects.
 * Uses AudioClip for low-latency playback of short sound samples.
 */
public class SoundManager {

    private static final Logger logger = LogManager.getLogger(SoundManager.class);
    private static SoundManager instance;

    private final Map<String, AudioClip> soundCache = new HashMap<>();
    private boolean muted = false;

    private SoundManager() {
    }

    public static synchronized SoundManager getInstance() {
        if (instance == null) {
            instance = new SoundManager();
        }
        return instance;
    }

    /**
     * Preload or play a sound by its resource path.
     * 
     * @param soundPath Path to sound in resources (e.g., "/sounds/move.wav")
     */
    public void playSound(String soundPath) {
        if (muted)
            return;

        try {
            AudioClip clip = soundCache.computeIfAbsent(soundPath, path -> {
                URL resource = getClass().getResource(path);
                if (resource == null) {
                    logger.warn("Sound resource not found: {}", path);
                    return null;
                }
                return new AudioClip(resource.toExternalForm());
            });

            if (clip != null) {
                clip.play();
            }
        } catch (Exception e) {
            logger.error("Error playing sound: " + soundPath, e);
        }
    }

    public void setMuted(boolean muted) {
        this.muted = muted;
    }

    public boolean isMuted() {
        return muted;
    }

    // Convenience methods for standard game sounds
    public void playMove() {
        playSound("/sounds/move.wav");
    }

    public void playCapture() {
        playSound("/sounds/capture.wav");
    }

    public void playCheck() {
        playSound("/sounds/check.wav");
    }

    public void playWin() {
        playSound("/sounds/win.wav");
    }

    public void playLose() {
        playSound("/sounds/lose.wav");
    }
}
