package com.example.anas.recipebook;

import android.net.Uri;

public class AppContract {
    // communication contract shared among classes

    public static final String AUTHORITY = "com.example.anas.recipebook.content_provider.MyContentProvider";
    public static final String RECIPES_TABLE = "recipes";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + RECIPES_TABLE);
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TITLE = "recipetitle";
    public static final String COLUMN_CONTENT = "content";
    public static final int DATABASE_VERSION = 1;

}
