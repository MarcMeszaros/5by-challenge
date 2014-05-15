package ca.marcmeszaros.fivebychallenge.fragments;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import java.util.List;

import ca.marcmeszaros.fivebychallenge.R;
import ca.marcmeszaros.fivebychallenge.adapters.VideoListAdapter;
import ca.marcmeszaros.fivebychallenge.loaders.VideoListLoader;
import ca.marcmeszaros.fivebychallenge.api.v1.objects.Video;

public class VideoListFragment extends FivebyListFragment implements LoaderManager.LoaderCallbacks<List<Video>>, AdapterView.OnItemClickListener {

    private static final String TAG = "VideoListFragment";

    public static final class LOADERS {
        public static final int VIDEOS = 0x01;
    }

    private VideoListAdapter videoAdapter;

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

        //Uri uri = Uri.parse("http://www.example.com");
        Uri uri = Uri.parse(video.media.oembed.url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
}
