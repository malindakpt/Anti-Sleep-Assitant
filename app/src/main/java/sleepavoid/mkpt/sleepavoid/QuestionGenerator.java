package sleepavoid.mkpt.sleepavoid;

import java.util.Random;

import sleepavoid.mkpt.sleepavoid.ques.Question1;
import sleepavoid.mkpt.sleepavoid.ques.Question10;
import sleepavoid.mkpt.sleepavoid.ques.Question11;
import sleepavoid.mkpt.sleepavoid.ques.Question12;
import sleepavoid.mkpt.sleepavoid.ques.Question13;
import sleepavoid.mkpt.sleepavoid.ques.Question14;
import sleepavoid.mkpt.sleepavoid.ques.Question15;
import sleepavoid.mkpt.sleepavoid.ques.Question16;
import sleepavoid.mkpt.sleepavoid.ques.Question17;
import sleepavoid.mkpt.sleepavoid.ques.Question18;
import sleepavoid.mkpt.sleepavoid.ques.Question2;
import sleepavoid.mkpt.sleepavoid.ques.Question3;
import sleepavoid.mkpt.sleepavoid.ques.Question4;
import sleepavoid.mkpt.sleepavoid.ques.Question5;
import sleepavoid.mkpt.sleepavoid.ques.Question6;
import sleepavoid.mkpt.sleepavoid.ques.Question7;
import sleepavoid.mkpt.sleepavoid.ques.Question8;
import sleepavoid.mkpt.sleepavoid.ques.Question9;
import sleepavoid.mkpt.sleepavoid.ques.QuestionDB;
import sleepavoid.mkpt.sleepavoid.ques.QuestionEntity;

public class QuestionGenerator {
    private String[] dbQuestions;
    private QuestionGenerator(){}
    private static QuestionGenerator instance;
    public static QuestionGenerator getInstance(){
        if(instance==null){
          instance = new QuestionGenerator();
        }
        return instance;
    }
    private Random random = new Random();
    private QuestionEntity[] questionEntities= new QuestionEntity[]{
            new Question1(),
            new Question2(),
            new Question3(),
            new Question4(),
            new Question5(),
            new Question6(),
            new Question7(),
            new Question8(),
            new Question9(),
            new Question10(),
            new Question11(),
            new Question12(),
            new Question13(),
            new Question14(),
            new Question15(),
            new Question16(),
            new Question17(),
            new Question18()};

    public QuestionEntity getRandomQuestion(){
        return questionEntities[random.nextInt(questionEntities.length)];
    }

    public QuestionEntity getDbQuestion(){
        return new QuestionDB(this.dbQuestions[random.nextInt(dbQuestions.length)]);
    }

    public void setDbQuestions(String[] content){
        this.dbQuestions = content;
    }

}
