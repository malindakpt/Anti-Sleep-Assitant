package sleepavoid.mkpt.sleepavoid.ques;

public class QuestionDB extends QuestionEntity{
    private String ques;
    public QuestionDB(String line){
        this.ques = line;
    }
    @Override
    public String getQuestion() {
        return ques;
    }
}
