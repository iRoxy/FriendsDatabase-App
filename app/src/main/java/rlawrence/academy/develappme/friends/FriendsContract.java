package rlawrence.academy.develappme.friends;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Raquel Lawrence on 11/2/15.
 *
 * Purpose: Class containing constant definition for URI
 * of Content Provider. It includes, column names, definitions
 * for Authority if URI format. Fixed information
 */
public class FriendsContract
{
    interface FriendsColumns
    {
        String FRIENDS_ID = "_id";
        String FRIENDS_NAME = "friends_name";
        String FRIENDS_EMAIL = "friends_email";
        String FRIENDS_PHONE = "friends_phone";
    }

    // authority in URI format, essentially package name
    public static final String CONTENT_AUTHORITY = "rlawrence.academy.develappme.friends.provider";
    // base content URI
    public static final Uri BASE_CONTENT = Uri.parse("content://" + CONTENT_AUTHORITY);
    // paths definition
    private static final String PATH_FRIENDS = "friends";

    public static final Uri URI_TABLE = Uri.parse(BASE_CONTENT.toString() + "/" + PATH_FRIENDS);

    // array of strings that contain all top level paths (for multiple paths)
    public static final String[] TOP_LEVEL_PATHS ={
            PATH_FRIENDS
    };

    public static class Friends implements FriendsColumns, BaseColumns
    {
        public static final Uri CONTENT_URI =
                BASE_CONTENT.buildUpon().appendEncodedPath(PATH_FRIENDS).build(); // creates access to friends content provider

        // access to more than one data at one time | access one record
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd." + CONTENT_AUTHORITY + ".friends";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd." + CONTENT_AUTHORITY + ".friends";

        public static Uri buildFriendUri(String friendId)// URI built to access a particular record
        {
            return CONTENT_URI.buildUpon().appendEncodedPath(friendId).build();
        }

        public static String getFriendId(Uri uri)
        {
            return uri.getPathSegments().get(1);
        }
    }

}

