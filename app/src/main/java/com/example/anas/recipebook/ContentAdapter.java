package com.example.anas.recipebook;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;

public class ContentAdapter extends ArrayAdapter<Recipe> {
    // class to render ListView items through a custom adapter

    // declare global variables
    List<Recipe> recipeList;
    TextView recipeId;
    TextView recipeTitle;

    // define class constructor
    public ContentAdapter(Context context, List<Recipe> recipeList) {
        super(context, R.layout.listview_item, recipeList);
        this.recipeList = recipeList;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customView = inflater.inflate(R.layout.listview_item, parent, false);

        // get recipe details
        Recipe item = getItem(position);
        recipeId = customView.findViewById(R.id.recipeIdLV);
        recipeTitle = customView.findViewById(R.id.recipeTitleLV);

        // update UI element values
        recipeId.setText(Integer.toString(item.getID()));
        recipeTitle.setText(item.getRecipeTitle());

        return customView;
    }
} // end of ContentAdapter.java
