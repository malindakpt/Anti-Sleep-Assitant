package sleepavoid.mkpt.sleepavoid.ques;

public class Question16 extends QuestionEntity{
    @Override
    public String getQuestion(){
        String items = getRandomItem();
        String relation = getRandomRelation();
        int remaining = getRandom15();
        int initial = remaining + getRandom15();
        int given = initial - remaining;
        return "You had "+initial+" of "+items+". Then you gave "+given +" of them to your "+relation+". Now how many "+items +" remaining with you?: You have "+remaining+" "+items+ " with you.";
    }
}
