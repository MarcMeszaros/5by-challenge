package ca.marcmeszaros.fivebychallenge.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ca.marcmeszaros.fivebychallenge.R;

public class VideoListFragment extends FivebyListFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_video_list, null);
    }

}
