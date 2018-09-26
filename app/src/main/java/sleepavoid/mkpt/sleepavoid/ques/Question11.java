package sleepavoid.mkpt.sleepavoid.ques;

public class Question11 extends QuestionEntity{
    @Override
    public String getQuestion(){
        return "Can Name an fruit, which starts with letter, "+getRandomWordLetter();
    }
}
