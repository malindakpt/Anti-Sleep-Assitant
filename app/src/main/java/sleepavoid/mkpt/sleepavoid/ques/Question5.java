package sleepavoid.mkpt.sleepavoid.ques;

public class Question5 extends QuestionEntity{
    @Override
    public String getQuestion(){
        int[] n = {
                getRandom100(),
                getRandom100(),
                getRandom100(),
                getRandom100(),
                getRandom100()
        };
        int max = 0;
        for (int i=0; i<n.length ;i++){
            if(max < n[i]){
                max = n[i];
            }
        }
        return "What is the maximum number, from ,"+ n[0]+", "+n[1]+", "+n[2]+", "+n[3]+", "+n[4]+". : the Maximum number is, "+max+".";
    }
}
