package com.example.anas.recipebook;

import com.example.anas.recipebook.content_provider.MyContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.ContentResolver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/* MOST OF THIS CLASS WAS DERIVED FROM LAB 5 (G53MDP) */

public class DatabaseHandler extends SQLiteOpenHelper {

    // declare global variables
    private ContentResolver contentResolver;
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "recipeDB.db";
    public static final String TABLE_RECIPES = "recipes";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TITLE = "recipetitle";
    public static final String COLUMN_CONTENT = "content";

    // define class constructor
    public DatabaseHandler(Context context, String name, SQLiteDatabase.CursorFactory factory,
                    int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
        contentResolver = context.getContentResolver();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.d("MYAPP", "DatabaseHandler: onCreate()");
        String CREATE_RECIPES_TABLE = "CREATE TABLE " + TABLE_RECIPES + "(" + COLUMN_ID +
                " INTEGER PRIMARY KEY," + COLUMN_TITLE + " TEXT," + COLUMN_CONTENT +
                " TEXT" + ")";
        sqLiteDatabase.execSQL(CREATE_RECIPES_TABLE);
    } // end of onCreate()

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        Log.d("MYAPP", "DatabaseHandler: onUpgrade()");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPES);
        onCreate(sqLiteDatabase);
    } // end of onUpgrade()

    public void addRecipe(Recipe recipe) {
        // create instance of ContentValues with recipe details
        ContentValues values = new ContentValues();
        values.put(AppContract.COLUMN_ID, recipe.getID());
        values.put(COLUMN_TITLE, recipe.getRecipeTitle());
        values.put(COLUMN_CONTENT, recipe.getRecipeContent());
        // insert new recipe into database
        contentResolver.insert(AppContract.CONTENT_URI, values);
    } // end of addRecipe()

    public List<Recipe> findRecipe(String recipeTitle) {
        // create empty list of recipes
        List<Recipe> recipeList = new ArrayList<Recipe>();
        // retrieve data from database to check
        Cursor cursor = contentResolver.query(AppContract.CONTENT_URI,
                null, null, null, null);
        while (cursor.moveToNext()) { // loop through data to find matches
            // create Recipe instance from retrieved row
            Recipe recipe = new Recipe();
            recipe.setID(Integer.parseInt(cursor.getString(cursor.getColumnIndex(AppContract.COLUMN_ID))));
            recipe.setRecipeTitle(cursor.getString(cursor.getColumnIndex(AppContract.COLUMN_TITLE)));
            recipe.setRecipeContent(cursor.getString(cursor.getColumnIndex(AppContract.COLUMN_CONTENT)));

            // if any part of the title contains the search string, add recipe to the list
            if ((recipe.getRecipeTitle()).toLowerCase().contains(recipeTitle.toLowerCase())) {
                recipeList.add(recipe);
            }
        } // end of while loop
        return recipeList;
    } // end of findRecipe()

    public boolean deleteRecipe(String recipeTitle) {
        boolean result = false;
        String selection = "recipetitle = \"" + recipeTitle + "\"";
        int rowsDeleted = contentResolver.delete(MyContentProvider.CONTENT_URI,
                selection, null);
        if (rowsDeleted > 0) { result = true; }
        return result;
    } // end of deleteRecipe()

} // end of DatabaseHandler.java
