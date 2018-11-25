package com.example.anas.recipebook;

import com.example.anas.recipebook.content_provider.MyContentProvider;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // declare global variables
    private static List<Recipe> data;
    private static ContentAdapter adapter;
    private static boolean isFinding = false;
    private static DatabaseHandler dbHandler;
    ListView recipesListView;
    Button addNewBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("MYAPP", "MainActivity: onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // handle landscape mode by hiding action bar
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            getSupportActionBar().hide();
        }

        // initialize global variables
        dbHandler = new DatabaseHandler(this, null, null,
                AppContract.DATABASE_VERSION);
        isFinding = false;
        addNewBtn = findViewById(R.id.addRecBtn);
        addNewBtn.setText("Add new");
        recipesListView = findViewById(R.id.recipesListView);

        // define onClick listener for ListView
        recipesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Recipe recipe = data.get(i); // get the clicked item's recipe

                // start ViewRecipeActivity with current recipe's details
                Intent intent = new Intent(getApplicationContext(), ViewRecipeActivity.class);
                intent.putExtra("id", Integer.toString(recipe.getID()));
                intent.putExtra("title", recipe.getRecipeTitle());
                intent.putExtra("content", recipe.getRecipeContent());
                startActivityForResult(intent, 1);
            }
        }); // end of onClick listener
        displayDBContents();
    } // end of onCreate()



    private void displayDBContents() { // method to render recipe details onto ListView
        // define set of columns to query
        String[] mProjection = new String[] {
            AppContract.COLUMN_ID,
            AppContract.COLUMN_TITLE,
            AppContract.COLUMN_CONTENT
        };

        // start needed cursor
        Cursor cursor = getContentResolver().query(MyContentProvider.CONTENT_URI, mProjection,
                null, null, null);

        data = new ArrayList<Recipe>();

        while (cursor.moveToNext()) { // loop through cursor elements
            // create new recipe object from recipe details brought by cursor
            Recipe recipe = new Recipe(
                    Integer.parseInt(cursor.getString(cursor.getColumnIndex(AppContract.COLUMN_ID))),
                    cursor.getString(cursor.getColumnIndex(AppContract.COLUMN_TITLE)),
                    cursor.getString(cursor.getColumnIndex(AppContract.COLUMN_CONTENT)));
            data.add(recipe);
        }
        // set adapter data and list adapter
        adapter = new ContentAdapter(this, data);
        recipesListView.setAdapter(adapter);
    } // end of displayDBContents()

    public void addRecipeOnClick(View view) {
        if (isFinding) { // if user is done searching
            isFinding = false;
            addNewBtn.setText("Add new");
            displayDBContents();
        } else { // if user wants to add new recipe to the db
            Intent intent = new Intent(this, NewRecipeActivity.class);
            startActivityForResult(intent, 0);
        }
    } // end of addRecipeOnClick()

    public void findRecipeOnClick(View view) {
        // create search dialog box
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Search by title");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        dialog.setView(input);

        dialog.setPositiveButton("Search", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                isFinding = true;
                addNewBtn.setText("Cancel");
                // reset list data by refilling the list adapter
                List<Recipe> foundMatches = dbHandler.findRecipe(input.getText().toString());
                adapter.clear();
                adapter.addAll(foundMatches);
                adapter.notifyDataSetChanged();
            }
        });
        dialog.show();
    } // end of findRecipeOnClick()

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == 0 || requestCode == 1) {
                addNewBtn.setText("Add New"); // reset button to original look
                displayDBContents(); // update list at every return to the activity
            }
        }
    }

    // methods to handle activity lifecycle
    @Override
    protected void onStart() { super.onStart(); Log.d("MYAPP", "MainActivity: onStart()"); }
    @Override
    protected void onResume() { super.onResume(); Log.d("MYAPP", "MainActivity: onResume()"); }
    @Override
    protected void onPause() { super.onPause(); Log.d("MYAPP", "MainActivity: onPause()"); }
    @Override
    protected void onStop() { super.onStop(); Log.d("MYAPP", "MainActivity: onStop()"); }
    @Override
    protected void onDestroy() { super.onDestroy(); Log.d("MYAPP", "MainActivity: onDestroy()"); }

} // end of MainActivity.java
