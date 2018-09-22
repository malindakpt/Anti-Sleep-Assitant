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
    public static  String[] contentQ;

    public static String getLastUpdatedTime(){
        return prefs.getString(LAST_UPDATED_TIME, "2011-01-11 11:11:11");
    }

    public static void loadQues1(){
        contentQ = prefs.getString(Q_BOX, "Database is not updated yet. please wait: Hold on few seconds").split("#");
    }

    public static  void loadQues2(String res) {
        String[] resArr = res.split("#", 2);

        prefs.edit().putString(LAST_UPDATED_TIME, resArr[0]).commit();

        contentQ = resArr[1].split("#");
        prefs.edit().putString(Q_BOX, resArr[1]).commit();
    }

    public static String getQuestion() {
        if(count == contentQ.length){
            count = 0;
        }
        return contentQ[count].split(":")[0];
    }

    public static String getAnswer() {
        return contentQ[count++].split(":")[1];
    }
}



