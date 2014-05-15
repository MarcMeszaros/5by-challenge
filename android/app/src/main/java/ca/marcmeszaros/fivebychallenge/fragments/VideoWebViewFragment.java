package ca.marcmeszaros.fivebychallenge.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import ca.marcmeszaros.fivebychallenge.R;

public class VideoWebViewFragment extends Fragment {

    private static final String TAG = VideoWebViewFragment.class.getSimpleName();

    private String mUrl = "";

    // we need an empty constructor
    public VideoWebViewFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUrl = getArguments() != null ? getArguments().getString("url") : "";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_video_webview, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        loadUrl(mUrl);
    }

    // helper
    public void loadUrl(String url) {
        Log.d(TAG, "url: " + url);
        WebView webView = (WebView) getView().findViewById(R.id.fragment_video_webview__webview);
        webView.getSettings().setJavaScriptEnabled(true);

        webView.setWebViewClient(new WebViewClient());

        if (webView != null) {
            webView.loadUrl(url);
        }
    }

}
