package ca.marcmeszaros.fivebychallenge.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ca.marcmeszaros.fivebychallenge.R;
import ca.marcmeszaros.fivebychallenge.api.v1.objects.Video;

public class VideoListAdapter extends ArrayAdapter<Video> {

    private static final String TAG = "VideoListAdapter";
    //private final Bitmap placeholder;

    static class ViewHolder {
        protected TextView title;
        protected TextView description;
        protected ImageView cover;
    }

    public VideoListAdapter(Context context) {
        super(context, R.layout.listview_row_video);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        // get a view
        if(convertView == null) {
            final LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_row_video, parent, false);

            holder = new ViewHolder();
            holder.title = (TextView)convertView.findViewById(R.id.listview_row_video__title);
            holder.description = (TextView)convertView.findViewById(R.id.listview_row_video__description);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }

        // set the data
        Video video = getItem(position);
        holder.title.setText(video.media.oembed.title);
        holder.description.setText(video.media.oembed.description);

        return convertView;
    }

    public void setData(List<Video> data) {
        clear();
        if (data != null) {
            addAll(data);
        }
    }
}
