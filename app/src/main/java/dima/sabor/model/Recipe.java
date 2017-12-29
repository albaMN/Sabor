package dima.sabor.model;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

public class Recipe {
    @NonNull
    String id;
    String userId;
    @Nullable
    String title;
    String time;
    String people;
    String difficulty;
    String place;
    List<String> ingredients;
    String description;
    List<String> images;

    public Recipe(){
    }

    public Recipe(String id, String userId, String title, String time, String people, String difficulty, String place, List<String> ingredients, String description, List<String> images) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.time = time;
        this.people = people;
        this.difficulty = difficulty;
        this.place = place;
        this.ingredients = ingredients;
        this.description = description;
        this.images = images;
    }

    public void setRecipe (Recipe r) {
        this.id = r.getId();
        this.userId = r.getUserId();
        this.title = r.getTitle();
        this.time = r.getTime();
        this.people = r.getPeople();
        this.difficulty = r.getDifficulty();
        this.place = r.getPlace();
        this.ingredients = r.getIngredients();
        this.description = r.getDescription();
        this.images = r.getImages();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getPeople() {
        return people;
    }

    public void setPeople(String people) {
        this.people = people;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
