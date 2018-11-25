package com.example.anas.recipebook;

/* MOST OF THIS CLASS WAS DERIVED FROM LAB 5 (G53MDP) */

public class Recipe {
    // class to encapsulate each recipe's details together

    // declare global variables
    private int recipeId;
    private String recipeTitle;
    private String recipeContent;

    // define constructors
    public Recipe () { }
    public Recipe (int id, String title, String content) {
        this.recipeId = id;
        this.recipeTitle = title;
        this.recipeContent = content;
    }

    // define setters and getters
    public void setID(int id) { this.recipeId = id; }
    public int getID() { return this.recipeId; }
    public void setRecipeTitle(String title) { this.recipeTitle = title; }
    public String getRecipeTitle() { return this.recipeTitle; }
    public void setRecipeContent(String content) { this.recipeContent = content; }
    public String getRecipeContent() { return this.recipeContent; }

} // end of Recipe.java
