package com.mtsoft.olaassistant;

import android.speech.tts.TextToSpeech;

/**
 * Created by manhhung on 3/29/19.
 */

public class Text2Speech {
    private TextToSpeech tts;

    public Text2Speech( TextToSpeech tts) {
        this.tts = tts;
    }


    public TextToSpeech getTts() {
        return tts;
    }

    public void setTts(TextToSpeech tts) {
        this.tts = tts;
    }

    public void speech(String txt) {
        if (txt.trim().length() > 0) {
            tts.speak(txt.trim(), TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    public void stopSpeech() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
    }
}
