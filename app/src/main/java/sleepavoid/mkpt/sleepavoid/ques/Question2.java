package sleepavoid.mkpt.sleepavoid.ques;

public class Question2 extends QuestionEntity{
    @Override
    public String getQuestion() {
        String s = "";
        int n = 0;
        for(int i=0; i<5; i++){
            if(getRandom100()%2==0){
                s = s +"," +getRandomLetter();
            } else {
                s = s +"," +getRandomVowel();
                n++;
            }
        }
        return "Please tell me, how many, vowels, contained, in following letters. "+s+": Ok. there were "+n+" vowels.";
    }
}
