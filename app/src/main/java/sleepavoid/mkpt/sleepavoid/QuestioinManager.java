package sleepavoid.mkpt.sleepavoid;

import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by MalindaK on 12/3/2017.
 */

public class QuestioinManager {
    private static String MY_PREFS_NAME="MY_PREFS_NAME";
    private static String LAST_UPDATED_TIME = "LAST_UPDATED_TIME";
    private static String Q_BOX = "Q_BOX";

    private static SharedPreferences prefs = MainActivity.mainActivity.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

    public static int count = 0;
    public static  List<String> contentQ, contentA, contentR;

    public static String getLastUpdatedTime(){
        return prefs.getString(LAST_UPDATED_TIME, "2018-09-22 10:17:30");
    }

    public static  void loadQues2(String res) {
        //String res = "2018-09-22 11:25:21#How Are You:I am good persons#Whats your name:Malinda#qqqq:aaaaa#";
        String[] arr = res.split("#");
        prefs.edit().putString(LAST_UPDATED_TIME, arr[0]);

        String qBox = prefs.getString(Q_BOX, "");
        for(int i=1; i<arr.length;i++){
            qBox = qBox + "#" + arr[i];
        }
        prefs.edit().putString(Q_BOX, qBox);


        String[] newArr = qBox.split("#");

        contentQ = new ArrayList<String>();
        contentA = new ArrayList<String>();

        for(int i=0; i<newArr.length;i++){
            String[] phrases = newArr[i].split(":");
            if(phrases.length==2) {
                contentQ.add(phrases[0]);
                contentA.add(phrases[1]);
            }
        }
    }


    public static String getQuestion() {
        if(count == contentQ.size()){
            count = 0;
        }
        return contentQ.get(count++);
    }

    public static String getAnswer() {
        if(count == contentQ.size()){
            count = 0;
        }
        return contentA.get(count);
    }
}



