package sleepavoid.mkpt.sleepavoid.ques;

public class Question7 extends QuestionEntity{
    @Override
    public String getQuestion(){
        int let = random.nextInt(15);
        return "What is the letters in between "+letters[let]+ " and "+ letters[let+5];
    }
}
