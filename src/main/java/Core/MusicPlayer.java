package Core;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

@Author(name = "Mateusz Biskup")
public class MusicPlayer {
    private Clip clip;
    private boolean isPlaying = false;

    // Wczytuje i odtwarza muzykę w pętli
    public void playMusic(InputStream resourceStream) {
        if (resourceStream == null) {
            System.out.println("Strumień zasobu jest pusty");
            return;
        }

        try {
            // Zatrzymaj poprzednią muzykę jeśli gra
            stopMusic();

            // Wczytaj plik audio
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(new BufferedInputStream(resourceStream));

            // Stwórz clip do odtwarzania
            clip = AudioSystem.getClip();
            clip.open(audioStream);

            // Odtwarzaj w pętli
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();

            isPlaying = true;

        } catch (UnsupportedAudioFileException e) {
            System.out.println("Nieobsługiwany format audio: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Nie można wczytać pliku muzyki: " + e.getMessage());
        } catch (LineUnavailableException e) {
            System.out.println("Linia audio niedostępna: " + e.getMessage());
        }
    }

    // Zatrzymuje muzykę
    public void stopMusic() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
            clip.close();
            isPlaying = false;
            System.out.println("Muzyka zatrzymana");
        }
    }

    // Pauza
    public void pauseMusic() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
            isPlaying = false;
            System.out.println("Muzyka spauzowana");
        }
    }

    // Wznów
    public void resumeMusic() {
        if (clip != null && !clip.isRunning()) {
            clip.start();
            isPlaying = true;
            System.out.println("Muzyka wznowiona");
        }
    }

    // Ustaw głośność (0.0 do 1.0)
    public void setVolume(float volume) {
        if (clip != null) {
            FloatControl volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float min = volumeControl.getMinimum();
            float max = volumeControl.getMaximum();
            float value = min + (max - min) * volume;
            volumeControl.setValue(value);
        }
    }

    // Sprawdź czy muzyka gra
    public boolean isPlaying() {
        return isPlaying;
    }
}