package sleepavoid.mkpt.sleepavoid.ques;

public class Question12 extends QuestionEntity{
    @Override
    public String getQuestion(){
        return "Can you tell me, a verb, which, has exactly, "+ (random.nextInt(2)+2) +" letters.: Ok. thanks.";
    }
}
