package sleepavoid.mkpt.sleepavoid;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    public static MainActivity mainActivity;
    private HashMap<String, String> params = new HashMap<String, String>();
    private TextToSpeech tts;
    private TextView textView;
    private Button btnStart, btnStop;
    private ProgressBar progressBar;
    private boolean isRun = true;
    boolean isNeedToListen = false;
    Handler handler = null;
    Runnable callBack = null;
    private boolean isAnswered = false;
    private String TAG = "Night Driving : ";
    private static final int REQUEST_CODE = 1234;
    RecorderActivity recorderActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainActivity = this;
        recorderActivity = new RecorderActivity(this);
        startServices();
       // recorderActivity.startServices();
    }
    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                @Override
                public void onStart(String utteranceId) {
                    Log.e("TTS", "On start");
                }
                @Override
                public void onDone(String utteranceId) {
                    if(isNeedToListen && isRun) {

                        //mainActivity.startVoiceRecognitionActivity();


                       // View v = findViewById(R.id.main_layout); //fetch a View: any one will do

                        View v = getWindow().getDecorView().getRootView();

                        v.post(new Runnable(){ public void run(){
                            recorderActivity.startServices();
                        }});
                    }
                }
                @Override
                public void onError(String utteranceId) {
                    Log.e("TTS", "This Language is not supported");
                }
            });
            int result = tts.setLanguage(Locale.US);
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "New This Language is not supported New");
                Intent installIntent = new Intent();
                installIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(installIntent);
                this.finish();
                System.exit(0);
            } else {
                btnStart.setEnabled(true);
            }
        } else {
            Log.e("TTS", "Initilization Failed!");
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            // Populate the wordsList with the String values the recognition engine thought it heard
            final ArrayList<String> matches = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            Log.i(TAG, "Got a answer : " +matches);
            if(matches.size()>0) {
                textView.setText(matches.get(0));
                isAnswered = true;
                isNeedToListen = false;
                speak(QuestioinManager.getAnswer());
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.i(TAG, "------- NEW QUESTION --------");
                        isNeedToListen = true;
                        speak(QuestioinManager.getQuestion());
                    }
                }, 5000);
            }else{
                textView.setText("ERRPR CODE");
                Toast.makeText(getApplicationContext(), "Timeout Please wakeup!",Toast.LENGTH_SHORT).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void onlistningComplete(ArrayList<String> matches){
        Log.i(TAG, "Got a answer : " +matches);
        if(matches.size()>0) {
            textView.setText(matches.get(0));
            isAnswered = true;
            isNeedToListen = false;
            speak(QuestioinManager.getAnswer());
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Log.i(TAG, "------- NEW QUESTION --------");
                    isNeedToListen = true;
                    speak(QuestioinManager.getQuestion());
                }
            }, 5000);
        }else{
            textView.setText("ERRPR CODE");
            Toast.makeText(getApplicationContext(), "Timeout Please wakeup!",Toast.LENGTH_SHORT).show();
        }
    }

//    public void startVoiceRecognitionActivity()
//    {
//        Intent intent = new Intent(this, RecorderActivity.class);
//        startActivityForResult(intent,REQUEST_CODE);
//        Log.i(TAG, "Listner opened");
//    }

    private void startServices(){
        btnStart = (Button) findViewById(R.id.btnStart);
        btnStop = (Button) findViewById(R.id.btnStop);
        textView = (TextView) findViewById(R.id.textView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar3);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                isRun = true;
                askQues();
                progressBar.setIndeterminate(true);
            }
        });
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                isRun = false;
                progressBar.setIndeterminate(false);
            }
        });

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID,"stringId");
        tts = new TextToSpeech(this, this);
        tts.setSpeechRate(0.65F);
        tts.setPitch(-5);

        getPackageManager().queryIntentActivities(new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);

        WebService webService = new WebService(this);
        webService.sendRequest();
        QuestioinManager.loadQues1();
}

    private void askQues(){
        isNeedToListen = true;
        speak(QuestioinManager.getQuestion());
    }

    public void speak(String str){
        if (handler != null && callBack != null) {
            handler.removeCallbacks(callBack);
        }

        if(isRun) {
            Log.i(TAG, "speak: " + str);
            isAnswered = false;
            tts.speak(str, TextToSpeech.QUEUE_FLUSH, params);
        }
    }

    @Override
    public void onDestroy() {
        // Don't forget to shutdown tts!
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }




}
