package sleepavoid.mkpt.sleepavoid.ques;

public class Question9 extends QuestionEntity{
    @Override
    public String getQuestion(){
        return "Can Name an animal, which starts with letter, "+getRandomWordLetter();
    }
}
