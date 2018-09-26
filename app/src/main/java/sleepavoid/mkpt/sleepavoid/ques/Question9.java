package sleepavoid.mkpt.sleepavoid.ques;

public class Question9 extends QuestionEntity{
    @Override
    public String getQuestion(){
        return "Can you Name, an animal, which ,starts with letter, "+getRandomWordLetter();
    }
}
