package sleepavoid.mkpt.sleepavoid;

import android.content.SharedPreferences;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by MalindaK on 12/3/2017.
 */

public class QuestioinManager {
    private static String MY_PREFS_NAME="MY_PREFS_NAME";
    private static String LAST_UPDATED_TIME = "LAST_UPDATED_TIME";
    private static String Q_BOX = "Q_BOX";
    public static boolean islastAskedQuestion = true;
    private static String CURRENT_QUESTION = "CURRENT_QUESTION";
    private static String autoAnswer;
    public static boolean autoAsked = false;

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

    private static int getNextCount(boolean incrVal){
        int count = prefs.getInt(CURRENT_QUESTION, 0);
        if(count == contentQ.length){
            count = 0;
        }
        if(incrVal){
            prefs.edit().putInt(CURRENT_QUESTION, count+1).commit();
        } else {
            prefs.edit().putInt(CURRENT_QUESTION, count).commit();
        }
        return count;
    }

    public static String getQuestion() {
        islastAskedQuestion = true;
        int count = getNextCount(false);

        if(count%MainActivity.autoQuestionFreq==0 && !autoAsked){
            autoAsked = true;
            Log.e("QM", "geQuestion() autoAsked=true" + " count="+count);
            return getAutoQuestion();
        } else{
            autoAsked = false;
            Log.e("QM", "geQuestion() autoAsked=false" + " count="+count);
            return contentQ[count].split(":")[0];
        }
    }

    public static String getAnswer() {
        islastAskedQuestion = false;

        if(count%MainActivity.autoQuestionFreq==0 && autoAsked){
            Log.e("QM", "geQuestion() autoAsked=true" + " count="+count);
            return autoAnswer;
        } else{
            int count = getNextCount(true);
            Log.e("QM", "geQuestion() autoAsked=false" + " count="+count);
            return contentQ[count].split(":")[1];
        }

    }

    private static String getAutoQuestion(){
        Random r = new Random();
        String question;
        int val1 = r.nextInt(50);
        int val2 = r.nextInt(50);

        if(val1%2==0){
            question = "This is simple maths: "+val1+" plus "+ val2+ ". what is the answer";
            autoAnswer ="Answer is "+ (val1+val2);
        } else {
            question = "This is simple maths: "+val1+" minus "+ val2+ ". what is the answer";
            autoAnswer ="Answer is "+ (val1-val2);
        }
        return question;
    }
}



