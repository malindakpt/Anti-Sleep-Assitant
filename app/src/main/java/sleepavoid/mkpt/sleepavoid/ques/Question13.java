package sleepavoid.mkpt.sleepavoid.ques;

public class Question13 extends QuestionEntity{
    @Override
    public String getQuestion(){
        return "Can you name, an animal, which has exactly, "+ (random.nextInt(2)+2) +", letters.: ok.";
    }
}
