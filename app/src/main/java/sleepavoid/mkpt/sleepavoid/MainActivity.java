package sleepavoid.mkpt.sleepavoid;


import android.content.Intent;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.RotateDrawable;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    public static String LBL_ACTIVATE = "Activate Assistant";
    public static String LBL_DECTIVATE = "Deactivate Assistant";
    public static String LBL_SPEAK_TO_PHONE = "Give your answer";
    public static String LBL_DISABLED = "Assitant Disabled";
    public static String LBL_SLEEPING = "You are sleeping";

    private MediaPlayer mediaPlayer;
    public static MainActivity mainActivity;
    private HashMap<String, String> params = new HashMap<String, String>();
    private TextToSpeech tts;
    private Button btnStart;
    private ProgressBar progressLinear, progressCircle;
    private String TAG = "MainActivity";
    private RecorderActivity recorderActivity;
    private Handler mHandler = new Handler();
    

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
                    Log.e("TTS", "On start");
                }
                @Override
                public void onDone(String utteranceId) {
                    View v = getWindow().getDecorView().getRootView();
                    v.post(new Runnable() {
                        public void run() {
                            if(deviceStatus == DeviceStatus.RUNNING) {
                                if (QuestioinManager.islastAskedQuestion) {
                                    recorderActivity.startListening();
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
        if(deviceStatus == DeviceStatus.RUNNING) {
            if (matches.size() > 0) {
                asnwerDetected = true;
                speak(QuestioinManager.getAnswer());
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        setState(deviceStatus, LBL_DECTIVATE);
//                    }
//                }, 5000);
            } else {
                // temporary only
                // onSleepPersonDitected();
            }
        }
    }

    public void onSleepPersonDetected() {
        if(deviceStatus == DeviceStatus.RUNNING) {
            sleepDetected = true;
            QuestioinManager.getAnswer();
            setState(deviceStatus, LBL_SLEEPING);
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
        Toast.makeText(getApplicationContext(), "Next", Toast.LENGTH_SHORT).show();
    }

    private void startServices() {
       btnStart = (Button) findViewById(R.id.button);
       btnStart.setText("Activate Assistant");
       progressCircle = (ProgressBar) findViewById(R.id.progressBar3);
       progressLinear = (ProgressBar) findViewById(R.id.progressBar5);
       mediaPlayer= MediaPlayer.create(MainActivity.mainActivity,R.raw.alert1);
       setState(DeviceStatus.IDLE, LBL_ACTIVATE);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if(deviceStatus == DeviceStatus.IDLE){
                     setState(DeviceStatus.RUNNING, LBL_DECTIVATE);
                } else {
                    setState(DeviceStatus.IDLE, LBL_DISABLED);
                }

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



    public void setState(DeviceStatus deviceStatus, String uiStatus){
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
                new Handler(getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        progressCircle.setVisibility(View.VISIBLE);
                        progressLinear.setIndeterminate(false);
                    }
                });
//                switch (uiStatus){
//                    case SLEEPING:
//                        btnStart.setText(LBL_SLEEPING);
//                        break;
//                    case LISTENING:
//                        btnStart.setText(LBL);
//                        break;
//                }
                break;
//            case SPEAKING:
//                new Handler(getMainLooper()).post(new Runnable() {
//                    @Override
//                    public void run() {
//                        progressCircle.setVisibility(View.VISIBLE);
//                        ProgressLinear.setIndeterminate(true);
//                    }
//                });
//                break;
//            case LISTENING:
//                new Handler(getMainLooper()).post(new Runnable() {
//                    @Override
//                    public void run() {
//                        progressCircle.setVisibility(View.INVISIBLE);
//                        ProgressLinear.setIndeterminate(false);
//                    }
//                });
//                break;
//            case SLEEPING:
//                deviceStatus = DeviceStatus.SLEEPING;
//                new Handler(getMainLooper()).post(new Runnable() {
//                    @Override
//                    public void run() {
//                        progressCircle.setVisibility(View.VISIBLE);
//                        ProgressLinear.setIndeterminate(true);
//                    }
//                });
//                break;



        }
    }

    public void speak(String str) {
        if(deviceStatus == DeviceStatus.RUNNING ){
            Log.i(TAG, "speak: " + str);
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

    Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {
            try {
                if(!sleepDetected && !asnwerDetected) {
                    speak(QuestioinManager.getQuestion()); //this function can change value of mInterval.
                } else {
                    sleepDetected = false;
                    asnwerDetected = false;
                }
//                if (deviceStatus == DeviceStatus.RUNNING) {
//                    setState(DeviceStatus.LISTENING, LBL_SPEAK_TO_PHONE);
//                }
            } finally {
                // 100% guarantee that this always happens, even if
                // your update method throws an exception
                mHandler.postDelayed(mStatusChecker, 10000);
            }
        }
    };


    public void onRMSChange(int rmsdB){
        progressLinear.setProgress(10*rmsdB);
    }
}
