package sleepavoid.mkpt.sleepavoid.ques;

import java.util.Random;

public abstract class QuestionEntity {
    public Random random = new Random();
    public String[] letters = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    public String[] wordLetters = {"A", "B", "C", "D", "E", "F", "G", "H", "K", "L", "M", "N", "O", "P", "R", "S", "T", "W"};
    public String[] vowels = {"A", "E", "I", "O", "U"};
    public String getRandomLetter(){
        return letters[random.nextInt(letters.length)];
    }
    public String getRandomWordLetter(){
        return wordLetters[random.nextInt(wordLetters.length)];
    }
    public String getRandomVowel(){
        return vowels[random.nextInt(vowels.length)];
    }
    public int getRandom100(){
        return random.nextInt(10);
    }
    public abstract String getQuestion();

    ;
}
