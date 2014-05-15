package ca.marcmeszaros.fivebychallenge.activities;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import ca.marcmeszaros.fivebychallenge.R;
import ca.marcmeszaros.fivebychallenge.fragments.VideoWebViewFragment;

public class PlayerActivity extends Activity {

    private static final String TAG = PlayerActivity.class.getSimpleName();

    private static final int CONTENT_VIEW_ID = 100110;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        // only do this if we are not restoring
        if (savedInstanceState == null && findViewById(R.id.activity_player__fragment_video_webview) != null) {
            // the url to watch
            String video_id = getIntent().getAction();
            Log.d(TAG, "video_id: " + video_id);
            Bundle args = new Bundle(1);
            args.putString("video_id", video_id);

            // build/attach the fragment
            VideoWebViewFragment newFragment = new VideoWebViewFragment();
            newFragment.setArguments(args);
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.add(R.id.activity_player__fragment_video_webview, newFragment).commit();
        }

    }

}
