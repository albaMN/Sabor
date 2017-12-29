package dima.sabor.utils;


import java.util.ArrayList;
import java.util.List;

public class RecipeDifficulties {
    public static final String VERY_DIFFICULT = "Very difficult";
    public static final String DIFFICULT= "Difficult";
    public static final String INTERMEDIATE = "Intermediate";
    public static final String EASY = "Easy";
    public static final String VERY_EASY = "Very easy";

    public static List<String> getAllDifficulties() {
        List<String> difficulties = new ArrayList<>();
        difficulties.add(VERY_DIFFICULT);
        difficulties.add(DIFFICULT);
        difficulties.add(INTERMEDIATE);
        difficulties.add(EASY);
        difficulties.add(VERY_EASY);
        return difficulties;
    }
}
