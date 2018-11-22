package com.example.anas.recipebook;

public class Recipe {

    private int recipeId;
    private String recipeTitle;
    private String recipeContent;

    public Recipe () { }

    public Recipe (int id, String title, String content) {
        this.recipeId = id;
        this.recipeTitle = title;
        this.recipeContent = content;
    }

    public Recipe(String title, String content) {
        this.recipeTitle = title;
        this.recipeContent = content;
    }

    public void setID(int id) { this.recipeId = id; }

    public int getID() { return this.recipeId; }

    public void setRecipeTitle(String title) { this.recipeTitle = title; }

    public String getRecipeTitle() { return this.recipeTitle; }

    public void setRecipeContent(String content) { this.recipeContent = content; }

    public String getRecipeContent() { return this.recipeContent; }

}
