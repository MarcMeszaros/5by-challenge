package ca.marcmeszaros.fivebychallenge.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;

public class WebView extends android.webkit.WebView {

    public WebView(Context context) {
        this(context, null);
    }

    public WebView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        /* any initialisation work here */
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        /* your code here */
        return super.onKeyDown(keyCode, event);
    }

}