package rlawrence.academy.develappme.friends;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

/**
 * Created by Raquel Lawrence on 11/2/15.
 */
public class FriendsProvider extends ContentProvider {
    private FriendsDatabase mOpenHelper;
    private static String TAG = FriendsProvider.class.getSimpleName();
    private static final UriMatcher sUriMatcher = buildUriMatcher(); // looks at URI passed to content provider. decides if it is valid
    private static final int FRIENDS = 100; // all the records in the table
    private static final int FRIENDS_ID = 101; // single record in the table

    private static UriMatcher buildUriMatcher()
    {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = FriendsContract.CONTENT_AUTHORITY;
        matcher.addURI(authority, "friends", FRIENDS); // all the records
        matcher.addURI(authority, "friends/*", FRIENDS_ID); // one particular record
        return matcher;
    }

    @Override
    public boolean onCreate()
    {
        mOpenHelper = new FriendsDatabase(getContext()); // gives access to friends database
        return true;
    }

    private void deleteDatabase()
    {
        mOpenHelper.close();
        FriendsDatabase.deleteDatabase(getContext());
        mOpenHelper = new FriendsDatabase(getContext());
    }

    @Nullable
    @Override
    public String getType(Uri uri)
    {
        final int  match = sUriMatcher.match(uri);
        switch (match)
        {
            case FRIENDS:
                return FriendsContract.Friends.CONTENT_TYPE;
            case FRIENDS_ID:
                return FriendsContract.Friends.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown Uri" + uri);

        }
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder)
    {
        final SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        final int match = sUriMatcher.match(uri);
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(FriendsDatabase.Tables.FRIENDS);

        switch(match)
        {
            case FRIENDS:
                // do nothing
                break;
            case FRIENDS_ID:
                String id = FriendsContract.Friends.getFriendId(uri); // extracts id that has been passed
                queryBuilder.appendWhere(BaseColumns._ID + "=" + id);
                break;
            default:
                throw new IllegalArgumentException("Unknown Uri" + uri);
        }
        return queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values)
    {
        Log.v(TAG, "insert(uri=" + uri + ", values=" + values.toString());
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);

        switch(match)
        {
            case FRIENDS:
                long recordId = db.insertOrThrow(FriendsDatabase.Tables.FRIENDS, null, values);
                return FriendsContract.Friends.buildFriendUri(String.valueOf(recordId));
            default:
                throw new IllegalArgumentException("Unknown Uri" + uri);
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs)
    {
        Log.v(TAG, "update(uri=" + uri + ", values=" + values.toString());
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);

        String selectionCriteria = selection;
        switch(match)
        {
            case FRIENDS:
                // do nothing
                break;
            case FRIENDS_ID:
                String id = FriendsContract.Friends.getFriendId(uri);
                selectionCriteria = BaseColumns._ID + "=" + id
                        + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ")" : "");
                break;
            default:
                throw new IllegalArgumentException("Unknown Uri" + uri);
        }
        return db.update(FriendsDatabase.Tables.FRIENDS, values, selectionCriteria, selectionArgs);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs)
    {
        Log.v(TAG, "delete(uri=" + uri);

        if(uri.equals(FriendsContract.BASE_CONTENT))
        {
            deleteDatabase();
            return 0;
        }

        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);

        switch(match)
        {
            case FRIENDS_ID:
                String id = FriendsContract.Friends.getFriendId(uri);
                String selectionCriteria = BaseColumns._ID + "=" + id
                        + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ")" : "");
                return db.delete(FriendsDatabase.Tables.FRIENDS, selectionCriteria, selectionArgs);
            default:
                throw new IllegalArgumentException("Unknown Uri" + uri);

        }

    }
}
