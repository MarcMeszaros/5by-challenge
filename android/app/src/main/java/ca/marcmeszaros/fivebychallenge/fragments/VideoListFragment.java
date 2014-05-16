package ca.marcmeszaros.fivebychallenge.fragments;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import java.util.List;

import ca.marcmeszaros.fivebychallenge.R;
import ca.marcmeszaros.fivebychallenge.activities.PlayerActivity;
import ca.marcmeszaros.fivebychallenge.adapters.VideoListAdapter;
import ca.marcmeszaros.fivebychallenge.loaders.VideoListLoader;
import ca.marcmeszaros.fivebychallenge.api.v1.objects.Video;
import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;

public class VideoListFragment extends FivebyListFragment implements LoaderManager.LoaderCallbacks<List<Video>>, AdapterView.OnItemClickListener, OnRefreshListener {

    private static final String TAG = "VideoListFragment";

    public static final class LOADERS {
        public static final int VIDEOS = 0x01;
    }

    private VideoListAdapter videoAdapter;
    private PullToRefreshLayout mPullToRefreshLayout;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        videoAdapter = new VideoListAdapter(getActivity());
        setListAdapter(videoAdapter);

        // setup click listener
        getListView().setOnItemClickListener(this);

        // initialize the loader
        Bundle args = new Bundle(1);
        //if (mSearchQuery.length() > 0) {
        //    args.putString("q", mSearchQuery);
        //}
        getLoaderManager().initLoader(LOADERS.VIDEOS, args, this).forceLoad();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_video_list, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // This is the View which is created by ListFragment
        ViewGroup viewGroup = (ViewGroup) view;

        // We need to create a PullToRefreshLayout manually
        mPullToRefreshLayout = new PullToRefreshLayout(viewGroup.getContext());

        // Now setup the PullToRefreshLayout
        ActionBarPullToRefresh.from(getActivity())
                .insertLayoutInto(viewGroup) // We need to insert the PullToRefreshLayout into the Fragment's ViewGroup
                // We need to mark the ListView and it's Empty View as pullable
                // because they are not direct children of the ViewGroup
                .theseChildrenArePullable(getListView(), getListView().getEmptyView())
                .listener(this)
                .setup(mPullToRefreshLayout); // Finally commit the setup to our PullToRefreshLayout
    }

    @Override
    public Loader<List<Video>> onCreateLoader(int id, Bundle args) {
        // This is called when a new Loader needs to be created.
        // First, pick the base URI to use depending on whether we are
        // currently filtering.

        // get the query string if required
        if (args != null && args.containsKey("q")) {
            Log.d(TAG, "CursorLoader: q");
            setListShownNoAnimation(false);
            return new VideoListLoader(getActivity());
        } else {
            Log.d(TAG, "CursorLoader: default");
            setListShownNoAnimation(false);
            return new VideoListLoader(getActivity());
        }
    }

    @Override
    public void onLoadFinished(Loader<List<Video>> loader, List<Video> data) {
        // Swap the new cursor in. (The framework will take care of closing the
        // old cursor once we return.)
        Log.d(TAG, "loader size: " + data.size());
        videoAdapter.setData(data);
        mPullToRefreshLayout.setRefreshComplete();
        setListShown(true);
    }

    @Override
    public void onLoaderReset(Loader<List<Video>> loader) {
        // This is called when the last Cursor provided to onLoadFinished()
        // above is about to be closed. We need to make sure we are no
        // longer using it.
        Log.d(TAG, "loader reset");
        videoAdapter.setData(null);
    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Video video = (Video) getListAdapter().getItem(position);

        // setup the intent to start the player activity
        Intent intent = new Intent(getActivity(), PlayerActivity.class);
        intent.setAction(video.media.oembed.video_id);
        startActivity(intent);
    }

    @Override
    public void onRefreshStarted(View view) {
        getLoaderManager().restartLoader(LOADERS.VIDEOS, null, this).forceLoad();
    }
}
