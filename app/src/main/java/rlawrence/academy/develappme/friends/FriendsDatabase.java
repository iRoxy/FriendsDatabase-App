package rlawrence.academy.develappme.friends;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by Raquel Lawrence on 11/2/15.
 *
 * Purpose: This class uses SQLiteOpenHelper to create databases
 * Makes opening/closing databases easier
 */
public class FriendsDatabase extends SQLiteOpenHelper
{
    private static final String TAG = FriendsDatabase.class.getSimpleName();
    private static final String DATABASE_NAME = "friends.db";
    private static final int DATABASE_VERSION = 2;
    private final Context mContext;

    interface Tables
    {
        String FRIENDS = "friends";
    }

    /**
     * Constructor for class
     * @param context - the application context
     */
    public FriendsDatabase(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    /**
     * This method creates the latest version of a table within
     * the database
     * @param db - the database
     */
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE " + Tables.FRIENDS + "(" +
                BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + FriendsContract.FriendsColumns.FRIENDS_NAME + " TEXT NOT NULL,"
                + FriendsContract.FriendsColumns.FRIENDS_PHONE + " TEXT NOT NULL,"
                + FriendsContract.FriendsColumns.FRIENDS_EMAIL + " TEXT NOT NULL)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        int version = oldVersion; //version user is running at the time is automatically set to the old version

        if(version == 1)
        {
            // Extra fields should be added w/o deleting existing data
            version = 2;
        }

        if(version != DATABASE_VERSION)
        {
            db.execSQL("DROP TABLE IF EXISTS " + Tables.FRIENDS);
            onCreate(db); // recreates the tables
        }
    }

    /**
     * This code deletes the database
     * @param context - the application's context
     */
    public static void deleteDatabase(Context context)
    {
        context.deleteDatabase(DATABASE_NAME);
    }
}
