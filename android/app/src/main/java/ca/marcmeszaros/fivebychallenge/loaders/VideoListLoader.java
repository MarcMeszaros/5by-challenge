package ca.marcmeszaros.fivebychallenge.loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import ca.marcmeszaros.fivebychallenge.api.FivebyClient;
import ca.marcmeszaros.fivebychallenge.api.v1.Client;
import ca.marcmeszaros.fivebychallenge.api.v1.objects.Pager;
import ca.marcmeszaros.fivebychallenge.api.v1.objects.Video;
import ca.marcmeszaros.fivebychallenge.api.v1.resources.VideoResource;
import ca.marcmeszaros.fivebychallenge.utils.Network;

public class VideoListLoader extends AsyncTaskLoader<List<Video>> {

    private static final String TAG = "VideoListLoader";

    private VideoResource videoRes;
    private Network netInfo;

    public VideoListLoader(Context context) {
        super(context);
        videoRes = FivebyClient.getInstance().getRestAdapter().create(VideoResource.class);
        netInfo = new Network(context);
    }

    @Override
    public List<Video> loadInBackground() {
        if (netInfo.isConnected() && netInfo.isConnectedToServer(FivebyClient.getInstance().getBaseUrl(), 2)) {
            Pager<Video> videos = videoRes.getVideos();
            return videos.objects;
        }
        return new ArrayList<Video>();
    }
}
