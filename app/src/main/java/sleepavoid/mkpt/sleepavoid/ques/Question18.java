package sleepavoid.mkpt.sleepavoid.ques;

public class Question18 extends QuestionEntity{
    @Override
    public String getQuestion(){
        String item1 = getRandomItem();
        String item2 = getRandomItem();
        if(item1.equals(item2)){
            item2 = getRandomItem();
        }
        int n1 = getRandom10()+1;
        int n2 = getRandom10();

        return "1, "+ item1.substring(0, item1.length()-1) + " is, equal to, "+ n1+ " "+item2+". then, "+n2+" "+item1+" are equals to, how many of"+item2+"?: The answer is "+(n1*n2) + " "+item2+".";
    }
}
