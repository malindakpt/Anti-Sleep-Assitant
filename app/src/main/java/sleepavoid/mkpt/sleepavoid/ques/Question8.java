package sleepavoid.mkpt.sleepavoid.ques;

public class Question8 extends QuestionEntity{
    @Override
    public String getQuestion(){
        return "Can you tell me a verb, which starts with letter, "+getRandomWordLetter();
    }
}
