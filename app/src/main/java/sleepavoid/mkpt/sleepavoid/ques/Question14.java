package sleepavoid.mkpt.sleepavoid.ques;

public class Question14 extends QuestionEntity{
    @Override
    public String getQuestion(){
        return "Can Name a Country or region, which has exactly, "+ (random.nextInt(2)+2) +" letters. : ah ha.";
    }
}
