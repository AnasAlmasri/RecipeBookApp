package com.example.anas.recipebook;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.anas.recipebook.content_provider.MyContentProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    ListView recipesListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("MYAPP", "MainActivity: onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recipesListView = findViewById(R.id.recipesListView);
        recipesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Map<String, String> map = (Map<String, String>) adapterView.getSelectedItem();
                Intent intent = new Intent(getApplicationContext(), ViewRecipeActivity.class);
                intent.putExtra("id", new ArrayList<String>(map.values()).get(0));
                startActivityForResult(intent, 1);
            }
        });
        displayDBContents();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 0) {
            Log.d("MYAPP", Integer.toString(resultCode));
            displayDBContents();
        }
    }

    private void displayDBContents() {
        // define set of columns to query
        String[] mProjection = new String[] {
            DatabaseHandler.COLUMN_ID,
            DatabaseHandler.COLUMN_RECIPETITLE,
            DatabaseHandler.COLUMN_RECIPECONTENT
        };

        Cursor cursor = getContentResolver().query(MyContentProvider.CONTENT_URI, mProjection,
                null, null, null);

        List<Map<String, String>> data = new ArrayList<Map<String, String>>();
        Map<String, String> map;

        while (cursor.moveToNext()) {
            map = new HashMap<>();
            map.put("id", cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_ID)));
            map.put("title", cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_RECIPETITLE)));
            data.add(map);
        }

        ContentAdapter adapter = new ContentAdapter(this, data);
        recipesListView.setAdapter(adapter);
    }

    public void addRecipeOnClick(View view) {
        Intent intent = new Intent(this, NewRecipeActivity.class);
        startActivityForResult(intent, 0);
    }

    public void findRecipeOnClick(View view) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Search by ID");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        dialog.setView(input);

        dialog.setPositiveButton("Search", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String[] mProjection = new String[] {
                        DatabaseHandler.COLUMN_ID,
                        DatabaseHandler.COLUMN_RECIPETITLE,
                };
                Cursor cursor = getContentResolver().query(MyContentProvider.CONTENT_URI, mProjection,
                        "id = ? ", new String[]{input.getText().toString()}, null);

                Map<String, String> map;
                if (cursor.moveToNext()) {
                    map = new HashMap<>();
                    map.put("id", cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_ID)));
                    map.put("title", cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_RECIPETITLE)));
                }
            }
        });

        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        dialog.show();
    }

    public void deleteRecipeOnClick(View view) {

    }

}
