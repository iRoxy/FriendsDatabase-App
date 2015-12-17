package rlawrence.academy.develappme.friends;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Raquel Lawrence on 11/19/15.
 */
public class FriendsSearchListLoader extends AsyncTaskLoader<List<Friends>> {

    private static final String LOG_TAG = FriendsSearchListLoader.class.getSimpleName();
    private List<Friends> mFriends;
    private ContentResolver mContentResolver;
    private Cursor mCursor;
    private String mFilterText;

    public FriendsSearchListLoader(Context context, Uri uri, ContentResolver contentResolver, String filterText) {
        super(context);
        mContentResolver = contentResolver;
        mFilterText = filterText;
    }

    @Override
    public List<Friends> loadInBackground()
    {
        String[] projection = {BaseColumns._ID,
        FriendsContract.FriendsColumns.FRIENDS_NAME,
        FriendsContract.FriendsColumns.FRIENDS_PHONE,
        FriendsContract.FriendsColumns.FRIENDS_EMAIL};
        List<Friends> entries = new ArrayList<Friends>();

        String selection = FriendsContract.FriendsColumns.FRIENDS_NAME + " LIKE " + mFilterText + "%";
        mCursor = mContentResolver.query(FriendsContract.URI_TABLE, projection, selection, null, null);
        if(mCursor != null)
        {
            if(mCursor.moveToFirst())
            {
                do
                {
                    int _id = mCursor.getInt(mCursor.getColumnIndex(BaseColumns._COUNT));
                    String name = mCursor.getString(
                            mCursor.getColumnIndex(FriendsContract.FriendsColumns.FRIENDS_NAME));
                    String phone = mCursor.getString(
                            mCursor.getColumnIndex(FriendsContract.FriendsColumns.FRIENDS_PHONE));
                    String email = mCursor.getString(
                            mCursor.getColumnIndex(FriendsContract.FriendsColumns.FRIENDS_EMAIL));
                    Friends friends = new Friends(_id, name, phone, email);
                    entries.add(friends);
                }

                while(mCursor.moveToNext());
            }
        }
        return entries;
    }

    @Override
    public void deliverResult(List<Friends> friends)
    {
        if(isReset())
        {
            if(friends != null)
            {
                mCursor.close();
            }
        }

        List<Friends> oldFriendList = mFriends;
        if(mFriends == null || mFriends.size() == 0)
        {
            Log.d(LOG_TAG, "+++++++++ No data returned");
        }

        mFriends = friends;
        if(isStarted())
        {
            super.deliverResult(friends);
        }

        if(oldFriendList != null && oldFriendList != friends)
        {
            mCursor.close();
        }
    }

    @Override
    protected void onStartLoading()
    {
        if(mFriends != null)
        {
            deliverResult(mFriends);
        }

        if(takeContentChanged() || mFriends == null)
        {
            forceLoad();
        }
    }

    @Override
    protected void onStopLoading()
    {
        cancelLoad();
    }

    @Override
    protected void onReset()
    {
        onStopLoading();
        if(mCursor != null)
        {
            mCursor.close();
        }

        mFriends = null;
    }

    @Override
    public void onCanceled(List<Friends> friends)
    {
        super.onCanceled(friends);
        if(mCursor != null)
        {
            mCursor.close();
        }
    }

    @Override
    public void forceLoad()
    {
        super.forceLoad();
    }
}
