package sleepavoid.mkpt.sleepavoid.ques;

public class Question3 extends QuestionEntity{
    @Override
    public String getQuestion(){
        int num = getRandom100();
        return "Can you count from "+ num + " to " +(num + 7)+". : Ok. good.";
    }
}
