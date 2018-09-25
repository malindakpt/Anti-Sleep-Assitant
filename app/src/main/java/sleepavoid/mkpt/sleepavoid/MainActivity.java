package sleepavoid.mkpt.sleepavoid;


import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    public static String LBL_ACTIVATE = "Click to Activate";
    public static String LBL_DECTIVATE = "Click to Deactivate";
    public static String LBL_SPEAK_TO_PHONE = "Give your answer";
    public static String LBL_DISABLED = "Assitant Disabled";
    public static String LBL_SLEEPING = "You were sleeping";

    public static final int SLEEP_TIMEOUT = 5000;
    public static int autoQuestionFreq = 2;

    private MediaPlayer mediaPlayer;
    public static MainActivity mainActivity;
    private HashMap<String, String> params = new HashMap<String, String>();
    private TextToSpeech tts;
    private Button btnStart;
    private ImageButton btnMute;
    private ProgressBar progressLinear, progressCircle;
    private String TAG = "MainActivity";
    private RecorderActivity recorderActivity;
    private Handler mHandler = new Handler();

    private boolean questionInprogress = false;
    

    enum DeviceStatus{
        IDLE,RUNNING;
    }

    enum UIStatus{
        ACTIVE, SLEEPING, LISTENING;
    }

    private DeviceStatus deviceStatus = DeviceStatus.IDLE;
    private UIStatus uiStatus = UIStatus.ACTIVE;
    private boolean sleepDetected = false;
    private boolean asnwerDetected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainActivity = this;
        recorderActivity = new RecorderActivity(this);
        startServices();
        startAskingQuestions();
    }

    private void startAskingQuestions() {
        mStatusChecker.run();
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                @Override
                public void onStart(String utteranceId) {
                    questionInprogress = true;
                    Log.e("MainActivity", "Speech started");
                }
                @Override
                public void onDone(String utteranceId) {
                    View v = getWindow().getDecorView().getRootView();
                    v.post(new Runnable() {
                        public void run() {
                            if(deviceStatus == DeviceStatus.RUNNING) {
                                if (QuestioinManager.islastAskedQuestion) {
                                    setState(deviceStatus, UIStatus.LISTENING);
                                    recorderActivity.startListening();
                                    Log.e("MainActivity", "speechCompleted. islastAskedQuestion true");
                                } else {
                                    setState(deviceStatus, UIStatus.ACTIVE);
                                    questionInprogress = false;
                                    Log.e("MainActivity", "speechCompleted. islastAskedQuestion false");
                                }
                            }
                        }
                    });
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

    public void onlistningComplete(ArrayList<String> matches) {
        questionInprogress = false;
        if(deviceStatus == DeviceStatus.RUNNING) {
            if (matches.size() > 0) {
                asnwerDetected = true;
                speak(QuestioinManager.getAnswer());
                Log.e("MainActivity", "onlistningComplete. matches.size()>0 Answer given");
            } else {
                // temporary only
                // onSleepPersonDitected();
            }
        }
    }

    public void onSleepPersonDetected() {
        questionInprogress = false;
        if(deviceStatus == DeviceStatus.RUNNING) {
            Log.e("MainActivity", "Sleeping person detected");
            sleepDetected = true;
            QuestioinManager.getAnswer();
            setState(deviceStatus, UIStatus.SLEEPING);
            playSound();
        }
    }

    private void playSound(){
        try {
            if(mediaPlayer!=null){
                mediaPlayer.release();
            }
            mediaPlayer= MediaPlayer.create(MainActivity.mainActivity,R.raw.alert1);
            mediaPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startServices() {
       btnMute = (ImageButton) findViewById(R.id.imageButton);
       btnStart = (Button) findViewById(R.id.button);
       btnStart.setText(LBL_ACTIVATE);
       progressCircle = (ProgressBar) findViewById(R.id.progressBar3);
       progressLinear = (ProgressBar) findViewById(R.id.progressBar5);
       mediaPlayer= MediaPlayer.create(MainActivity.mainActivity,R.raw.alert1);
       setState(DeviceStatus.IDLE, UIStatus.ACTIVE);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if(deviceStatus == DeviceStatus.IDLE){
                     setState(DeviceStatus.RUNNING, UIStatus.ACTIVE);
                } else {
                    setState(DeviceStatus.IDLE, UIStatus.ACTIVE);
                }

            }
        });

        btnMute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                mediaPlayer.stop();
            }
        });

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "stringId");
        tts = new TextToSpeech(this, this);
        tts.setSpeechRate(0.65F);
        tts.setPitch(-5);

        getPackageManager().queryIntentActivities(new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);

        WebService webService = new WebService(this);
        webService.sendRequest();
        QuestioinManager.loadQues1();
    }

    public void setState(DeviceStatus deviceStatus, UIStatus uiStatus){
        this.deviceStatus = deviceStatus;
        switch (deviceStatus){
            case IDLE:
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.stop();
                }
                btnStart.setText(LBL_ACTIVATE);
                new Handler(getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        progressCircle.setVisibility(View.INVISIBLE);
                        progressLinear.setIndeterminate(true);
                    }
                });

                break;
            case RUNNING:
                btnStart.setText(LBL_DECTIVATE);

                switch (uiStatus){
                    case SLEEPING:
                        btnStart.setText(LBL_SLEEPING);
                        new Handler(getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                progressCircle.setVisibility(View.VISIBLE);
                                progressLinear.setVisibility(View.VISIBLE);
                                progressLinear.setIndeterminate(true);
                            }
                        });
                        break;
                    case LISTENING:
                        btnStart.setText(LBL_SPEAK_TO_PHONE);
                        new Handler(getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                progressCircle.setVisibility(View.INVISIBLE);
                                progressLinear.setVisibility(View.VISIBLE);
                                progressLinear.setIndeterminate(false);
                            }
                        });
                        break;
                    default:
                        btnStart.setText(LBL_DECTIVATE);
                        new Handler(getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                progressCircle.setVisibility(View.VISIBLE);
                                progressLinear.setVisibility(View.INVISIBLE);
                                progressLinear.setIndeterminate(false);
                            }
                        });
                }
                break;

        }
    }

    public void speak(String str) {
        Log.i(TAG, "speaking: " + str);
        tts.speak(str, TextToSpeech.QUEUE_FLUSH, params);
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

    Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {
            try {
                if(!questionInprogress && deviceStatus == DeviceStatus.RUNNING && !sleepDetected && !asnwerDetected) {
                    setState(deviceStatus, UIStatus.ACTIVE);
                    speak(QuestioinManager.getQuestion()); //this function can change value of mInterval.
                    Log.i(TAG, "Thread:  true" );
                } else {
                    Log.i(TAG, "Thread:  false" );
                    sleepDetected = false;
                    asnwerDetected = false;
                }
            } finally {
                mHandler.postDelayed(mStatusChecker, SLEEP_TIMEOUT);
            }
        }
    };


    public void onRMSChange(int rmsdB){
        progressLinear.setProgress(10*rmsdB);
    }
}
