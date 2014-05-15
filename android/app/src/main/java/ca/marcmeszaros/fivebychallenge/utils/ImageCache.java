package ca.marcmeszaros.fivebychallenge.utils;

import android.annotation.TargetApi;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.util.LruCache;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * A class to help manage all the caching stuff.
 */
public class ImageCache {

    private static final String TAG = "ImageCache";

    /**
     * A custom version of BitmapDrawable that wraps it with an AsyncTask to
     * load in the data.
     */
    public static class AsyncDrawable extends BitmapDrawable {
        private final WeakReference<BitmapWorkerTask> bitmapWorkerTaskReference;

        public AsyncDrawable(Resources res, Bitmap bitmap, BitmapWorkerTask bitmapWorkerTask) {
            super(res, bitmap);
            bitmapWorkerTaskReference = new WeakReference<BitmapWorkerTask>(bitmapWorkerTask);
        }

        public BitmapWorkerTask getBitmapWorkerTask() {
            return bitmapWorkerTaskReference.get();
        }
    }

    /**
     * Base AsyncTask used to load in images to an ImageView.
     */
    public static abstract class BitmapWorkerTask extends AsyncTask<String, Void, Bitmap> {

        protected String data = "";
        protected final WeakReference<ImageView> imageView;
        protected LruCache<String, Bitmap> mCache = null;

        public BitmapWorkerTask(ImageView imageView) {
            this.imageView = new WeakReference<ImageView>(imageView);
        }

        protected abstract Bitmap doInBackground(String... params);

        @Override
        protected void onPostExecute(Bitmap result) {
            if (isCancelled()) {
                result = null;
            }

            if (this.imageView != null && result != null) {
                final ImageView imageView = this.imageView.get();
                final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);
                if (this == bitmapWorkerTask && this.imageView != null) {
                    // setup the drawables for the crossfade
                    BitmapDrawable bitmapDrawable = new BitmapDrawable(imageView.getResources(), result);
                    Drawable arrayDrawable[] = new Drawable[2];
                    arrayDrawable[0] = imageView.getDrawable();
                    arrayDrawable[1] = bitmapDrawable;
                    // setup the transition
                    TransitionDrawable transitionDrawable = new TransitionDrawable(arrayDrawable);
                    transitionDrawable.setCrossFadeEnabled(true);
                    imageView.setImageDrawable(transitionDrawable);
                    transitionDrawable.startTransition(150);
                }
            }
        }

        private static BitmapWorkerTask getBitmapWorkerTask(ImageView imageView) {
            if (imageView != null) {
                final Drawable drawable = imageView.getDrawable();
                if (drawable instanceof AsyncDrawable) {
                    final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
                    return asyncDrawable.getBitmapWorkerTask();
                }
            }
            return null;
        }

        public static boolean cancelPotentialWork(String data, ImageView imageView) {
            final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);

            if (bitmapWorkerTask != null) {
                final String bitmapData = bitmapWorkerTask.data;
                if (bitmapData != data) {
                    // Cancel previous task
                    bitmapWorkerTask.cancel(true);
                } else {
                    // The same work is already in progress
                    return false;
                }
            }
            // No task associated with the ImageView, or an existing task was cancelled
            return true;
        }

        // helpers
        public Bitmap getPhoto(String id, String size) {
            return getBitmapFromCache(id);
        }

        public void addBitmapToCache(String key, Bitmap bitmap) {
            if (getBitmapFromCache(key) == null && bitmap != null) {
                if (mCache != null) { mCache.put(key, bitmap); }
            }
        }

        @Deprecated
        public Bitmap getBitmapFromCacheMemory(String key) {
            return getBitmapFromCache(key, true);
        }

        public Bitmap getBitmapFromCache(String key) {
            return getBitmapFromCache(key, false);
        }

        public Bitmap getBitmapFromCache(String key, boolean hitMemoryOnly) {
            // make sure the caches exist
            if (mCache == null) {
                return null;
            }

            // try to get the bitmap from memory first
            Bitmap result = null;
            if (mCache != null) {
                result = mCache.get(key); // try and get from memory
            }

            // if we aren't only checking memory, try to get it from disk
            if (!hitMemoryOnly && result == null) {
                if (result != null) {
                    mCache.put(key, result); // add to memory if found on disk
                }
            }

            return result;
        }
    }

    /**
     * The event photo caching and loading class.
     */
    public static class ImageWorkerTask extends BitmapWorkerTask {

        // class specific static cached
        private static LruCache<String, Bitmap> memoryCache = null;
        private static final int MCACHE_SIZE = 2 * 1024 * 1024; // 2MB
        //private static final int DCACHE_SIZE = 4 * 1024 * 1024; // 4MB

        @TargetApi(Build.VERSION_CODES.KITKAT)
        public static LruCache<String, Bitmap> getMemoryCache() {
            if (memoryCache == null) {
                memoryCache = new LruCache<String, Bitmap>(MCACHE_SIZE) {
                    @Override
                    protected int sizeOf(String key, Bitmap value) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            return value.getAllocationByteCount();
                        } else {
                            return value.getByteCount();
                        }
                    }
                };
            }
            return memoryCache;
        }

        public ImageWorkerTask(ImageView imageView) {
            super(imageView);
            mCache = getMemoryCache();
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            this.data = params[0];
            final String imageKey = params[0] + "_150x150";
            Bitmap bm = getBitmapFromCache(imageKey);
            if (bm != null) {
                return bm;
            } else{
                try {
                    URL url = new URL(this.data);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    InputStream input = connection.getInputStream();
                    bm = BitmapFactory.decodeStream(input);
                    addBitmapToCache(params[0], bm);
                    return bm;
                } catch (IOException e) {
                    Log.e(TAG, "problem getting the photo from the web", e);
                    return bm;
                }
            }
        }

    }

}
