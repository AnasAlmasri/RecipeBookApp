package com.example.anas.recipebook.content_provider;

import com.example.anas.recipebook.DatabaseHandler;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.content.UriMatcher;
import android.text.TextUtils;
import android.util.Log;

/* THIS CLASS WAS DERIVED FROM LAB 5 (G53MDP) */

public class MyContentProvider extends ContentProvider {

    private static final String AUTHORITY = "com.example.anas.recipebook.content_provider.MyContentProvider";
    private static final String RECIPES_TABLE = "recipes";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + RECIPES_TABLE);
    public static final int RECIPES = 1;
    public static final int RECIPES_ID = 2;
    private DatabaseHandler dbHandler;

    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sURIMatcher.addURI(AUTHORITY, RECIPES_TABLE, RECIPES);
        sURIMatcher.addURI(AUTHORITY, RECIPES_TABLE + "/#", RECIPES_ID);
    }

    public MyContentProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = dbHandler.getWritableDatabase();
        int rowsDeleted = 0;
        switch (uriType) {
            case RECIPES:
                rowsDeleted = sqlDB.delete(DatabaseHandler.TABLE_RECIPES,
                        selection, selectionArgs);
                break;
            case RECIPES_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = sqlDB.delete(DatabaseHandler.TABLE_RECIPES,
                            DatabaseHandler.COLUMN_ID + "=" + id, null);
                } else {
                    rowsDeleted = sqlDB.delete(DatabaseHandler.TABLE_RECIPES,
                            DatabaseHandler.COLUMN_ID + "=" + id
                    + " and " + selection, selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = dbHandler.getWritableDatabase();
        long id = 0;
        switch (uriType) {
            case RECIPES:
                id = sqlDB.insert(DatabaseHandler.TABLE_RECIPES, null, values);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(RECIPES_TABLE + "/" + id);
    }

    @Override
    public boolean onCreate() {
        Log.d("MYAPP", "MyContentProvider: onCreate()");
        dbHandler = new DatabaseHandler(getContext(), null, null, 1);
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(DatabaseHandler.TABLE_RECIPES);
        int uriType = sURIMatcher.match(uri);
        switch (uriType) {
            case RECIPES_ID:
                queryBuilder.appendWhere(DatabaseHandler.COLUMN_ID + "="
                + uri.getLastPathSegment());
                break;
            case RECIPES:
                break;
            default:
                throw new IllegalArgumentException("Unknown URI");
        }
        Cursor cursor = queryBuilder.query(dbHandler.getReadableDatabase(), projection,
                selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = dbHandler.getWritableDatabase();
        int rowsUpdated = 0;
        switch (uriType) {
            case RECIPES:
                rowsUpdated = sqlDB.update(DatabaseHandler.TABLE_RECIPES, values,
                        selection, selectionArgs);
                break;
            case RECIPES_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = sqlDB.update(DatabaseHandler.TABLE_RECIPES, values,
                            DatabaseHandler.COLUMN_ID + "=" + id, null);
                } else {
                    rowsUpdated = sqlDB.update(DatabaseHandler.TABLE_RECIPES, values,
                            DatabaseHandler.COLUMN_ID + "=" + id
                            + " and " + selection, selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }
}
