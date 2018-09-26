package sleepavoid.mkpt.sleepavoid.ques;

public class Question1 extends QuestionEntity{
    @Override
    public String getQuestion() {
        return "Can you repeat these letters. "+getRandomLetter()+","+getRandomLetter()+"+"+getRandomLetter()+","+getRandomLetter()+": Ok. there were 4 letters.";
    }
}
