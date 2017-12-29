package dima.sabor.utils;

import java.util.List;

public class RecipeChecker {

    public final static void checkTitle(String target) throws Exception {
        if(target.length()<3 || target.length()>100) throw new Exception("Title between 3 and 100 characters");
    }
    public final static void checkDescription(String target) throws Exception {
        if(target.length()<50 /* || target.length()>150*/) throw new Exception("Description can not be less than 50 characters");
    }
    public final static void checkIngredients(List<String> target) throws Exception {
        if(target.isEmpty())throw new Exception("You must introduce at least one ingredient to the recipe");
        //if(target.get(0) == "") ;
    }
    public final static void checkDifficulty(String target) throws Exception {
        if(target.isEmpty()) throw new Exception("You must introduce the difficulty level of the recipe");
    }
    public final static void checkPlace(String target) throws Exception {
        if(target.isEmpty() || target.length()<0 ) throw new Exception("You must introduce the place where the recipe is from");
    }
    public final static void checkPeople(String target) throws Exception {
        if(target.isEmpty()) throw new Exception ("You must introduce for how many people is the recipe");
    }
    public final static void checkTime(String target) throws Exception {
        if(target.length() != 5) throw new Exception("Check the cooking time. Time format: hh:mm");
    }
    public final static void checkImages(String photo1, String photo2, String photo3, String photo4) throws Exception {
        if(photo1.isEmpty() && photo2.isEmpty() && photo3.isEmpty() && photo4.isEmpty())
            throw new Exception("You must upload at least one picture of the recipe");
    }
}
