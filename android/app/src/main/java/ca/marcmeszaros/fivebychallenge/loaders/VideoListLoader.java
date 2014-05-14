package ca.marcmeszaros.fivebychallenge.loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import ca.marcmeszaros.fivebychallenge.api.v1.objects.Video;

public class VideoListLoader extends AsyncTaskLoader<List<Video>> {

    private static final String TAG = "VideoListLoader";

    public VideoListLoader(Context context) {
        super(context);
    }

    @Override
    public List<Video> loadInBackground() {
        Log.d(TAG, "load in bg");
        ArrayList<Video> list = new ArrayList<Video>(1);
        Video vid = new Video();
        vid.title = "asd";
        list.add(vid);
        return list;
    }
}
