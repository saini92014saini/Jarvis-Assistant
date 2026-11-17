package com.jarvis.assistant;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends Activity {

    private SpeechRecognizer speechRecognizer;
    private TextToSpeech tts;
    private TextView status;
    private TextView orb;

    private int dp(float value) {
        return (int) (value * getResources().getDisplayMetrics().density);
    }

    private TextView text(String value, int size, int color) {
        TextView view = new TextView(this);
        view.setText(value);
        view.setTextSize(size);
        view.setTextColor(color);
        view.setGravity(Gravity.CENTER);
        return view;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setStatusBarColor(Color.rgb(5, 12, 28));
        getWindow().setNavigationBarColor(Color.rgb(5, 12, 28));

        tts = new TextToSpeech(this, result -> {
            if (result == TextToSpeech.SUCCESS) {
                tts.setLanguage(new Locale("hi", "IN"));
            }
        });

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setGravity(Gravity.CENTER);
        root.setPadding(dp(24), dp(24), dp(24), dp(24));

        GradientDrawable background = new GradientDrawable(
            GradientDrawable.Orientation.TL_BR,
            new int[]{
                Color.rgb(5, 12, 28),
                Color.rgb(12, 28, 55),
                Color.rgb(3, 8, 20)
            }
        );
        root.setBackground(background);

        TextView title = text("J A R V I S", 30, Color.WHITE);
        title.setTypeface(Typeface.DEFAULT_BOLD);
        root.addView(title);

        TextView subtitle = text(
            "YOUR PERSONAL AI ASSISTANT",
            11,
            Color.rgb(120, 180, 255)
        );
        subtitle.setPadding(0, dp(8), 0, dp(55));
        root.addView(subtitle);

        orb = text("J", 58, Color.WHITE);
        orb.setTypeface(Typeface.DEFAULT_BOLD);

        GradientDrawable circle = new GradientDrawable(
            GradientDrawable.Orientation.TL_BR,
            new int[]{
                Color.rgb(30, 130, 255),
                Color.rgb(10, 45, 110)
            }
        );
        circle.setShape(GradientDrawable.OVAL);
        circle.setStroke(dp(3), Color.rgb(90, 190, 255));
        orb.setBackground(circle);

        LinearLayout.LayoutParams orbParams =
            new LinearLayout.LayoutParams(dp(190), dp(190));
        orbParams.gravity = Gravity.CENTER;
        root.addView(orb, orbParams);

        orb.setOnClickListener(v -> startListening());

        status = text("JARVIS IS READY", 17, Color.WHITE);
        status.setPadding(0, dp(45), 0, dp(10));
        root.addView(status);

        TextView instruction = text(
            "Tap J to speak",
            13,
            Color.rgb(155, 175, 205)
        );
        root.addView(instruction);

        setContentView(root);
    }

    private void startListening() {
        if (!SpeechRecognizer.isRecognitionAvailable(this)) {
            Toast.makeText(this, "Speech recognition not available",
                Toast.LENGTH_LONG).show();
            return;
        }

        if (speechRecognizer != null) {
            speechRecognizer.destroy();
        }

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);

        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {
                status.setText("LISTENING...");
            }

            @Override
            public void onBeginningOfSpeech() {
                status.setText("I AM LISTENING");
            }

            @Override
            public void onRmsChanged(float rmsdB) {}

            @Override
            public void onBufferReceived(byte[] buffer) {}

            @Override
            public void onEndOfSpeech() {
                status.setText("PROCESSING...");
            }

            @Override
            public void onError(int error) {
                status.setText("TAP J TO TRY AGAIN");
            }

            @Override
            public void onResults(Bundle results) {
                ArrayList<String> matches =
                    results.getStringArrayList(
                        SpeechRecognizer.RESULTS_RECOGNITION
                    );

                if (matches != null && !matches.isEmpty()) {
                    String spokenText = matches.get(0);
                    status.setText("YOU SPOKE");
                    speak("आपने कहा " + spokenText);
                }
            }

            @Override
            public void onPartialResults(Bundle partialResults) {}

            @Override
            public void onEvent(int eventType, Bundle params) {}
        });

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        );
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "hi-IN");
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "hi-IN");

        speechRecognizer.startListening(intent);
    }

    private void speak(String message) {
        if (tts != null) {
            tts.speak(message, TextToSpeech.QUEUE_FLUSH, null, "jarvis_reply");
        }
    }

    @Override
    protected void onDestroy() {
        if (speechRecognizer != null) {
            speechRecognizer.destroy();
        }
        if (tts != null) {
            tts.shutdown();
        }
        super.onDestroy();
    }
}
