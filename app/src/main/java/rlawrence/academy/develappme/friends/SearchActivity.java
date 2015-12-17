package rlawrence.academy.develappme.friends;

import android.content.ContentResolver;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.List;

/**
 * Created by Raquel Lawrence on 12/16/15.
 */
public class SearchActivity extends FragmentActivity implements LoaderManager.LoaderCallbacks<List<Friends>>{

    private static final String LOG_TAG = SearchActivity.class.getSimpleName();
    private FriendsCustomAdapter mFriendsCustomAdapter;
    private static int LOADER_ID = 1;
    private ContentResolver mContentResolver;
    private List<Friends> friendsRetrieved;
    private ListView listView;
    private EditText mSearchEditText;
    private Button mSearchFriendButton;
    private String matchText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_layout);
        listView = (ListView) findViewById(R.id.searchResultsList);
        mSearchEditText = (EditText) findViewById(R.id.searchName);
        mSearchFriendButton = (Button) findViewById(R.id.searchButton);
        mContentResolver = getContentResolver();
        mFriendsCustomAdapter = new FriendsCustomAdapter(SearchActivity.this, getSupportFragmentManager());
        listView.setAdapter(mFriendsCustomAdapter);

        mSearchFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                matchText = mSearchEditText.getText().toString();
                getSupportLoaderManager().initLoader(LOADER_ID++, null, SearchActivity.this);
            }
        });

    }

    @Override
    public Loader<List<Friends>> onCreateLoader(int id, Bundle args) {
        return new FriendsSearchListLoader(SearchActivity.this, FriendsContract.URI_TABLE,
                this.mContentResolver, matchText);
    }

    @Override
    public void onLoadFinished(Loader<List<Friends>> loader, List<Friends> friends) {
        mFriendsCustomAdapter.setData(friends);
        this.friendsRetrieved = friends;
    }

    @Override
    public void onLoaderReset(Loader<List<Friends>> loader) {
        mFriendsCustomAdapter.setData(null);
    }
}
