package sleepavoid.mkpt.sleepavoid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;

/**
 * Created by MalindaK on 12/5/2017.
 */

public class RecorderActivity implements RecognitionListener {

    private TextView returnedText;
    private ToggleButton toggleButton;
    private Button button1;
    private ProgressBar progressBar;
    private SpeechRecognizer speech = null;
    private Intent recognizerIntent;
    private String LOG_TAG = "Mkpt rec ";
    private MainActivity mainActivity;
    Handler handler = null;
    Runnable callBack = null;

    public RecorderActivity(MainActivity mainActivity){
        this.mainActivity = mainActivity;
    }


    public void startServices(){
        startListening();
        startTimeout();
    }

    private void stopTimeout() {
        if (handler != null && callBack != null) {
            handler.removeCallbacks(callBack);
        }
    }

    private void startTimeout() {

        if (handler != null && callBack != null) {
            handler.removeCallbacks(callBack);
        }
        handler = new Handler();
        callBack = new Runnable() {
            @Override
            public void run() {
                Log.e(LOG_TAG, "Timeout occured ");
                mainActivity.onlistningComplete(new ArrayList<String>());
            }
        };
        handler.postDelayed(callBack, 10000);
    }

    private void stopListening() {
        if (speech != null) {
            speech.destroy();
            Log.i(LOG_TAG, "Stop Listening");
        }
    }

    private void startListening() {
        stopListening();
        speech = SpeechRecognizer.createSpeechRecognizer(mainActivity);
        speech.setRecognitionListener(this);
        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "en");
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, mainActivity.getPackageName());
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3);
        speech.startListening(recognizerIntent);
    }

    @Override
    public void onBeginningOfSpeech() {
        Log.i(LOG_TAG, "onBeginningOfSpeech");
       // progressBar.setMax(10);
    }

    @Override
    public void onBufferReceived(byte[] arg0) {
        Log.i(LOG_TAG, "onBufferReceived: " + arg0);
    }

    @Override
    public void onEndOfSpeech() {
        Log.i(LOG_TAG, "onEndOfSpeech");
    }

    @Override
    public void onError(int errorCode) {
        String errorMessage = getErrorText(errorCode);
        Log.e(LOG_TAG, "FAILED " + errorMessage);
        Toast.makeText(mainActivity.getApplicationContext(), "FAILED", Toast.LENGTH_SHORT).show();
        startListening();
    }

    @Override
    public void onEvent(int arg0, Bundle arg1) {
        Log.i(LOG_TAG, "onEvent");
        Toast.makeText(mainActivity.getApplicationContext(), "onEvent", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPartialResults(Bundle arg0) {
        Log.i(LOG_TAG, "onPartialResults");
        Toast.makeText(mainActivity.getApplicationContext(), "onPartialResults", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onReadyForSpeech(Bundle arg0) {
        Log.i(LOG_TAG, "onReadyForSpeech");
    }

    @Override
    public void onResults(Bundle arg0) {
        Log.i(LOG_TAG, "onResults");
        ArrayList<String> matches = arg0.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        stopTimeout();
        mainActivity.onlistningComplete(matches);
    }

    @Override
    public void onRmsChanged(float rmsdB) {
       // progressBar.setProgress((int) rmsdB);
    }

    public static String getErrorText(int errorCode) {
        String message;
        switch (errorCode) {
            case SpeechRecognizer.ERROR_AUDIO:
                message = "Audio recording error";
                break;
            case SpeechRecognizer.ERROR_CLIENT:
                message = "Client side error";
                break;
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                message = "Insufficient permissions";
                break;
            case SpeechRecognizer.ERROR_NETWORK:
                message = "Network error";
                break;
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                message = "Network timeout";
                break;
            case SpeechRecognizer.ERROR_NO_MATCH:
                message = "No match";
                break;
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                message = "RecognitionService busy";
                break;
            case SpeechRecognizer.ERROR_SERVER:
                message = "error from server";
                break;
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                message = "No speech input";
                break;
            default:
                message = "Didn't understand, please try again.";
                break;
        }
        return message;
    }
}
