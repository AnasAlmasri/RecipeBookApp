package com.example.anas.recipebook;

import com.example.anas.recipebook.content_provider.MyContentProvider;

import android.content.ContentValues;
import android.content.Context;
import android.content.ContentResolver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {

    private ContentResolver contentResolver;

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "recipeDB.db";
    public static final String TABLE_RECIPES = "recipes";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_RECIPETITLE = "recipetitle";
    public static final String COLUMN_RECIPECONTENT = "content";

    public DatabaseHandler(Context context, String name, SQLiteDatabase.CursorFactory factory,
                    int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
        contentResolver = context.getContentResolver();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_RECIPES_TABLE = "CREATE TABLE " + TABLE_RECIPES + "(" + COLUMN_ID +
                " INTEGER PRIMARY KEY," + COLUMN_RECIPETITLE + " TEXT," + COLUMN_RECIPECONTENT +
                " TEXT" + ")";
        sqLiteDatabase.execSQL(CREATE_RECIPES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPES);
        onCreate(sqLiteDatabase);
    }

    public void addRecipe(Recipe recipe) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_RECIPETITLE, recipe.getRecipeTitle());
        values.put(COLUMN_RECIPECONTENT, recipe.getRecipeContent());
        contentResolver.insert(MyContentProvider.CONTENT_URI, values);
    }

    public Recipe findRecipe(String recipeTitle) {
        String[] projection = {COLUMN_ID, COLUMN_RECIPETITLE, COLUMN_RECIPECONTENT};
        String selection = "recipetitle = \"" + recipeTitle + "\"";
        Cursor cursor = contentResolver.query(MyContentProvider.CONTENT_URI,
                projection, selection, null, null);
        Recipe recipe = new Recipe();
        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            recipe.setID(Integer.parseInt(cursor.getString(0)));
            recipe.setRecipeTitle(cursor.getString(1));
            recipe.setRecipeContent(cursor.getString(2));
            cursor.close();
        } else {
            recipe = null;
        }
        return recipe;
    }

    public boolean deleteRecipe(String recipeTitle) {
        boolean result = false;
        String selection = "recipetitle = \"" + recipeTitle + "\"";
        int rowsDeleted = contentResolver.delete(MyContentProvider.CONTENT_URI,
                selection, null);
        if (rowsDeleted > 0) { result = true; }
        return result;
    }
}
