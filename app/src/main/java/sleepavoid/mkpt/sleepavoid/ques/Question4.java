package sleepavoid.mkpt.sleepavoid.ques;

public class Question4 extends QuestionEntity{
    @Override
    public String getQuestion(){
        int num = getRandom100()+10;
        return "Can you, count, from, "+ num + ", to, " +(num - 7)+".: ok. thank you.";
    }
}
