package com.example.anas.recipebook;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ViewRecipeActivity extends AppCompatActivity {

    // declare global variables
    private static String ID;
    private static String TITLE;
    private static String CONTENT;
    private static boolean isEditing = false;
    DatabaseHandler dbHandler;
    TextView idTextView;
    EditText titleEditText;
    EditText contentEditText;
    Button backBtn;
    Button editBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("MYAPP", "ViewRecipeActivity: onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_recipe);

        // handle landscape mode by hiding action bar
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            getSupportActionBar().hide();
        }

        // initialize global variables
        dbHandler = new DatabaseHandler(this, null, null,
                AppContract.DATABASE_VERSION);
        idTextView = findViewById(R.id.idTextView);
        titleEditText = findViewById(R.id.titleTextView);
        contentEditText = findViewById(R.id.contentTextView);
        backBtn = findViewById(R.id.backBtn);
        editBtn = findViewById(R.id.editBtn);

        // get recipe details from intent extras
        Intent intent = getIntent();
        ID = intent.getStringExtra("id");
        TITLE = intent.getStringExtra("title");
        CONTENT = intent.getStringExtra("content");

        // User Interface related settings
        titleEditText.setEnabled(false);
        contentEditText.setEnabled(false);
        idTextView.setText(ID);
        titleEditText.setText(TITLE);
        contentEditText.setText(CONTENT);
    } // end of onCreate()

    public void deleteRecipeOnClick(View view) {
        // delete current recipe
        dbHandler.deleteRecipe(TITLE);

        // end activity and return result
        Intent resultIntent = new Intent();
        setResult(Activity.RESULT_OK, resultIntent);
        finish();

        Toast.makeText(getApplicationContext(), "Entry " + TITLE + " was deleted",
                Toast.LENGTH_LONG).show();
    } // end of deleteRecipeOnClick()

    public void editBtnOnClick(View view) {
        if (isEditing) { // if user is done editing
            // validate inputs before saving
            if (titleEditText.getText().toString().isEmpty()) {
                Toast.makeText(getApplicationContext(), "Title field cannot be empty",
                        Toast.LENGTH_LONG).show();
            } else if (contentEditText.getText().toString().isEmpty()) {
                Toast.makeText(getApplicationContext(), "Description field cannot be empty",
                        Toast.LENGTH_LONG).show();
            } else { // if all is valid
                // set UI elements to not editable
                isEditing = false;
                titleEditText.setEnabled(false);
                contentEditText.setEnabled(false);
                editBtn.setText("Edit");

                // update global variable values
                TITLE = titleEditText.getText().toString();
                CONTENT = contentEditText.getText().toString();

                // update current recipe's details
                ContentValues values = new ContentValues();
                values.put(AppContract.COLUMN_ID, ID);
                values.put(AppContract.COLUMN_TITLE, TITLE);
                values.put(AppContract.COLUMN_CONTENT, CONTENT);
                getContentResolver().update(AppContract.CONTENT_URI, values,
                        "id = " + ID, null);

                Toast.makeText(getApplicationContext(), "Changes saved successfully",
                        Toast.LENGTH_LONG).show();

                // end activity and return results
                Intent resultIntent = new Intent();
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }

        } else { // set UI items to editable
            isEditing = true;
            titleEditText.setEnabled(true);
            contentEditText.setEnabled(true);
            editBtn.setText("Save");
        }
    } // end of editBtnOnClick()

    public void backOnClick(View view) {
        // handle UI changes based on current state
        if (isEditing) { // id user is done editing
            isEditing = false;
            backBtn.setText("Back");
        } else {
            isEditing = true;
            backBtn.setText("Cancel");
            Intent resultIntent = new Intent();
            setResult(Activity.RESULT_OK, resultIntent);
            finish();
        }
    } // end of backOnClick()

    // disable built-in back button
    @Override
    public void onBackPressed() { }

    // methods to handle activity lifecycle
    @Override
    protected void onStart() { super.onStart(); Log.d("MYAPP", "ViewRecipeActivity: onStart()"); }
    @Override
    protected void onResume() { super.onResume(); Log.d("MYAPP", "ViewRecipeActivity: onResume()"); }
    @Override
    protected void onPause() { super.onPause(); Log.d("MYAPP", "ViewRecipeActivity: onPause()"); }
    @Override
    protected void onStop() { super.onStop(); Log.d("MYAPP", "ViewRecipeActivity: onStop()"); }
    @Override
    protected void onDestroy() { super.onDestroy(); Log.d("MYAPP", "ViewRecipeActivity: onDestroy()"); }

} // end of ViewRecipeActivity.java
