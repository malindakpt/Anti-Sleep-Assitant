package sleepavoid.mkpt.sleepavoid.ques;

public class Question5 extends QuestionEntity{
    @Override
    public String getQuestion(){
        return "What is the maximum number from "+ getRandom100()+", "+getRandom100()+", "+getRandom100()+", "+getRandom100()+", "+getRandom100();
    }
}
