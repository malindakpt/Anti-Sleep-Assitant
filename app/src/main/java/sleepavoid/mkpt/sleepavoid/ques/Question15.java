package sleepavoid.mkpt.sleepavoid.ques;

public class Question15 extends QuestionEntity{
    @Override
    public String getQuestion(){
        return "Can you, Name an fruit, which has, exactly, "+ (random.nextInt(2)+2) +" letters. : ok.";
    }
}
