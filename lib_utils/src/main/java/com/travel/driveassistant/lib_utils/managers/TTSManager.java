package com.travel.driveassistant.lib_utils.managers;

import android.content.Context;
import android.media.AudioManager;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.widget.Toast;

import com.travel.driveassistant.lib_utils.DateUtil;
import com.travel.driveassistant.lib_utils.FileLogger;
import com.travel.driveassistant.lib_utils.Logger;

import java.util.Locale;

public class TTSManager {
    private Logger logger = new Logger(TTSManager.class.getSimpleName());

    // There should be a delay between speeches
    private final int SPEECHES_DELAY_IN_SECS = 20;

    private Context applicationContext;

    private TextToSpeech textToSpeech;
    private long lastSpeechTimeStamp = -1;

    public TTSManager(@NonNull final Context applicationContext) {
        init(applicationContext);
    }

    private void init(@NonNull final Context applicationContext) {
        textToSpeech = new TextToSpeech(applicationContext, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    final int ttsLang = textToSpeech.setLanguage(Locale.US);
                    if (ttsLang == TextToSpeech.LANG_MISSING_DATA
                            || ttsLang == TextToSpeech.LANG_NOT_SUPPORTED) {
                        logger.debug("TextToSpeech, the Language is not supported!");
                    }
                    logger.debug("TextToSpeech initialized successfully.");
                    textToSpeech.setPitch(1.0f);
                    textToSpeech.setSpeechRate(0.9f);

                } else {
                    logger.debug("TextToSpeech initialization failed.");
                    Toast.makeText(applicationContext, "TextToSpeech Initialization failed!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void speak(@NonNull String speech) {
        try {
            // Validate speech
            if (textToSpeech == null || TextUtils.isEmpty(speech)) {
                return;
            }
            if (lastSpeechTimeStamp > 0 && DateUtil.underNSecs(lastSpeechTimeStamp, SPEECHES_DELAY_IN_SECS)) {
                return;
            }
            try {
                // FIXME: Its not working properly, fix it later on
                maximizeDeviceVolume();
            } catch (Exception e) {
            }
            // Speak
            int speechStatus = textToSpeech.speak(speech, TextToSpeech.QUEUE_FLUSH, null);
            FileLogger.writeCommonLog("TTS_SPEECH: " + speech);

            if (speechStatus == TextToSpeech.ERROR) {
                logger.debug("Error in converting Text to Speech!");
            } else {
                lastSpeechTimeStamp = System.currentTimeMillis();
            }
        } catch (Exception e) {

        }
    }

    public void maximizeDeviceVolume() {
        AudioManager audioManager =
                (AudioManager) applicationContext.getSystemService(Context.AUDIO_SERVICE);

        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC),
                AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
    }

    public void onDestroy() {
        if (textToSpeech != null) {
            try {
                textToSpeech.stop();
                textToSpeech.shutdown();
            } catch (Exception e) {
                logger.debug("Error in stopping and shutting down Text to Speech!");
            }
        }
        textToSpeech = null;
        logger = null;
    }
}
