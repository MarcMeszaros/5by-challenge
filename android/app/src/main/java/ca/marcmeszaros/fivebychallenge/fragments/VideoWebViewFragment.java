package ca.marcmeszaros.fivebychallenge.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;

import ca.marcmeszaros.fivebychallenge.R;
import ca.marcmeszaros.fivebychallenge.utils.WebView;

public class VideoWebViewFragment extends Fragment {

    private static final String TAG = VideoWebViewFragment.class.getSimpleName();

    private String mVideoId = "";

    // we need an empty constructor
    public VideoWebViewFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mVideoId = getArguments() != null ? getArguments().getString("video_id") : "";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_video_webview, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        loadVideo(mVideoId);
    }

    // helper
    public void loadVideo(String videoId) {
        Log.d(TAG, "video_id: " + videoId);
        String url = "https://www.youtube.com/embed/" + videoId + "?autoplay=1";
        WebView webView = (WebView) getView().findViewById(R.id.fragment_video_webview__webview);

        // enable some settings
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient()); // prevent youtube app from starting
        webView.getSettings().setAppCacheEnabled(true);

        if (webView != null) {
            webView.loadUrl(url);
        }
    }

}
