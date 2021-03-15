package com.example.everyware.ui.home;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.util.AttributeSet;
import android.widget.VideoView;

public class StretchedVideoView  extends VideoView {

    private int videoWidth;
    private int videoHeight;

    public StretchedVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StretchedVideoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

    }

    public StretchedVideoView(Context context) {
        super(context);
    }

    @Override public void setVideoURI(Uri uri) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(this.getContext(), uri);
        videoWidth = Integer.parseInt(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
        videoHeight = Integer.parseInt(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));
        super.setVideoURI(uri);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //Resize, keep aspect ratio
        int width = getDefaultSize(videoWidth, widthMeasureSpec);
        int height = getDefaultSize(videoHeight, heightMeasureSpec);
        if (videoWidth > 0 && videoHeight > 0) {
            if (videoWidth * height > width * videoHeight) {
                // Log.i("@@@", "image too tall, correcting");
                height = width * videoHeight / videoWidth;
            } else if (videoWidth * height < width * videoHeight) {
                // Log.i("@@@", "image too wide, correcting");
                width = height * videoWidth / videoHeight;
            }
        }
        setMeasuredDimension(width, height);
}
}
