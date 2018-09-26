package sleepavoid.mkpt.sleepavoid.ques;

public class Question10 extends QuestionEntity{
    @Override
    public String getQuestion(){
        return "Can Name ,a Country or region, which ,starts with letter, "+getRandomWordLetter()+". :Nice try.";
    }
}
