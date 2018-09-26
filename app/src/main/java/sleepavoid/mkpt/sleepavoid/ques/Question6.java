package sleepavoid.mkpt.sleepavoid.ques;

public class Question6 extends QuestionEntity{
    @Override
    public String getQuestion(){
        int[] n = {
                getRandom100(),
                getRandom100(),
                getRandom100(),
                getRandom100(),
                getRandom100()
        };
        int min = 1000;
        for (int i=0; i<n.length ;i++){
            if(min > n[i]){
                min = n[i];
            }
        }
        return "What is the minimum number from "+ n[0]+", "+n[1]+", "+n[2]+", "+n[3]+", "+n[4]+". : the Minimum number is, "+min+".";
    }
}
