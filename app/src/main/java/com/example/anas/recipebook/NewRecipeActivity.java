package com.example.anas.recipebook;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import java.util.Random;

public class NewRecipeActivity extends AppCompatActivity {

    // declare global variables
    EditText recipeTitle;
    EditText recipeContent;
    DatabaseHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("MYAPP", "NewRecipeActivity: onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_recipe);

        // handle landscape mode by hiding action bar
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            getSupportActionBar().hide();
        }

        // initialize global variables
        recipeTitle = findViewById(R.id.recipeTitleEditText);
        recipeContent = findViewById(R.id.recipeContentEditText);
        dbHandler = new DatabaseHandler(this, null, null,
                AppContract.DATABASE_VERSION);
    } // end of onCreate()

    public void proceedBtnOnClick(View view) {
        // validate input data
        if (recipeTitle.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Title field cannot be empty",
                    Toast.LENGTH_LONG).show();
        } else if (recipeContent.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Description field cannot be empty",
                    Toast.LENGTH_LONG).show();
        } else { // if all is valid
            // create new recipe with random id of 3 digits
            Random random = new Random();
            Recipe recipe = new Recipe(100 + random.nextInt(900),
                    recipeTitle.getText().toString(), recipeContent.getText().toString());
            dbHandler.addRecipe(recipe);

            Toast.makeText(getApplicationContext(), "New recipe saved successfully",
                    Toast.LENGTH_LONG).show();

            // end activity and return result
            Intent returnIntent = new Intent();
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        }
    } // end of proceedBtnOnClick()

    // methods to handle activity lifecycle
    @Override
    protected void onStart() { super.onStart(); Log.d("MYAPP", "NewRecipeActivity: onStart()"); }
    @Override
    protected void onResume() { super.onResume(); Log.d("MYAPP", "NewRecipeActivity: onResume()"); }
    @Override
    protected void onPause() { super.onPause(); Log.d("MYAPP", "NewRecipeActivity: onPause()"); }
    @Override
    protected void onStop() { super.onStop(); Log.d("MYAPP", "NewRecipeActivity: onStop()"); }
    @Override
    protected void onDestroy() { super.onDestroy(); Log.d("MYAPP", "NewRecipeActivity: onDestroy()"); }

} // end of NewRecipeActivity.java
