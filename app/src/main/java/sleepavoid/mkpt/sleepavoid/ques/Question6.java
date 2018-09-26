package sleepavoid.mkpt.sleepavoid.ques;

public class Question6 extends QuestionEntity{
    @Override
    public String getQuestion(){
        return "What is the minmum number from "+ getRandom100()+", "+getRandom100()+", "+getRandom100()+", "+getRandom100()+", "+getRandom100();
    }
}
