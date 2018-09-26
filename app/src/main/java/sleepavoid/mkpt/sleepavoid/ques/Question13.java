package sleepavoid.mkpt.sleepavoid.ques;

public class Question13 extends QuestionEntity{
    @Override
    public String getQuestion(){
        return "Can Name an animal, which has exactly, "+ (random.nextInt(2)+2) +" letters.: ok.";
    }
}
