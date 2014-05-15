package ca.marcmeszaros.fivebychallenge.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ca.marcmeszaros.fivebychallenge.R;
import ca.marcmeszaros.fivebychallenge.api.v1.objects.Video;
import ca.marcmeszaros.fivebychallenge.utils.ImageCache;

public class VideoListAdapter extends ArrayAdapter<Video> {

    private static final String TAG = "VideoListAdapter";
    private final Bitmap placeholder;

    static class ViewHolder {
        protected TextView title;
        protected TextView description;
        protected ImageView cover;
    }

    public VideoListAdapter(Context context) {
        super(context, R.layout.listview_row_video);
        this.placeholder = BitmapFactory.decodeResource(context.getResources(), R.drawable.photo_blank);
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
            holder.cover = (ImageView)convertView.findViewById(R.id.listview_row_video__cover);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }

        // set the data
        Video video = getItem(position);
        holder.title.setText(video.media.oembed.title);
        holder.description.setText(video.media.oembed.description);

        // get the image, if there is one
        final String imageKey = video.media.oembed.thumbnail_url;
        Bitmap bm = new ImageCache.ImageWorkerTask(null).getBitmapFromCache(imageKey);
        if (bm != null) {
            holder.cover.setImageBitmap(bm);
        } else if (ImageCache.ImageWorkerTask.cancelPotentialWork(video.media.oembed.thumbnail_url, holder.cover)) {
            final ImageCache.ImageWorkerTask task = new ImageCache.ImageWorkerTask(holder.cover);
            final ImageCache.AsyncDrawable asyncDrawable = new ImageCache.AsyncDrawable(getContext().getResources(), this.placeholder, task);
            holder.cover.setImageDrawable(asyncDrawable);
            task.execute(video.media.oembed.thumbnail_url);
        }

        return convertView;
    }

    public void setData(List<Video> data) {
        clear();
        if (data != null) {
            addAll(data);
        }
    }
}
