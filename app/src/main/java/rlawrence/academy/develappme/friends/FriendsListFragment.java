package rlawrence.academy.develappme.friends;

import android.content.ContentResolver;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;


import java.util.List;

/**
 * Created by Raquel Lawrence on 12/12/15.
 */
public class FriendsListFragment extends ListFragment
        implements LoaderManager.LoaderCallbacks<List<Friends>>{

    private static final String LOG_TAG = FriendsListFragment.class.getSimpleName();
    private FriendsCustomAdapter mAdapter;
    private ContentResolver mContentResolver;
    private static final int LOADER_ID = 1;
    private List<Friends> mFriends;


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
        mContentResolver = getActivity().getContentResolver();
        mAdapter = new FriendsCustomAdapter(getActivity(), getChildFragmentManager());// mechnism,allows control fragments on screen
        setEmptyText("No Friends");
        setListAdapter(mAdapter);
        setListShown(false);
        getLoaderManager().initLoader(LOADER_ID, null, this);
    }

    @Override
    public Loader<List<Friends>> onCreateLoader(int id, Bundle args) {
        mContentResolver = getActivity().getContentResolver();
        return new FriendsListLoader(getActivity(), FriendsContract.URI_TABLE, mContentResolver);
    }

    @Override
    public void onLoadFinished(Loader<List<Friends>> loader, List<Friends> friends) {
        mAdapter.setData(friends);
        mFriends = friends;
        if(isResumed())
        {
            setListShown(true);
        } else {
            setListShownNoAnimation(true);
        }

    }

    @Override
    public void onLoaderReset(Loader<List<Friends>> loader) {
        mAdapter.setData(null);
    }
}
