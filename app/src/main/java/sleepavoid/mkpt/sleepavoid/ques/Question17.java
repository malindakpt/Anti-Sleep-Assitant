package sleepavoid.mkpt.sleepavoid.ques;

public class Question17 extends QuestionEntity{
    @Override
    public String getQuestion(){
        String items = getRandomItem();
        String relation = getRandomRelation();
        int initial = getRandom15();
        int given = getRandom15();
        int remaining = initial+given;

        return "You had "+initial+" of "+items+". Then your "+relation+" gave you another "+given+". Now how many "+items +" altogether with you?: You have "+remaining+" "+items+ " with you.";
    }
}
