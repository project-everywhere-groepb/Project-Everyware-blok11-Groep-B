package com.example.everyware.ui.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.media.projection.MediaProjectionManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.everyware.R;
import com.example.everyware.ui.Models.EditorCanvas;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Timer;
import java.util.TimerTask;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    private static final int GET_CONTENT_REQUEST_CODE = 1;
    private static final int SCREEN_RECORD_REQUEST_CODE = 2;
    private StretchedVideoView videoView;
    private Timer seekerTimer;
    private SeekBar seekBar;
    private TextView videoSeekHint;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        videoView = (StretchedVideoView) root.findViewById(R.id.videoView);

        EditorCanvas canvas = (EditorCanvas) root.findViewById(R.id.canvas);

        Button selectVideoButton = (Button) root.findViewById(R.id.selectVideoButton);
        selectVideoButton.setOnClickListener(v -> {
            Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
            chooseFile.setType("video/*");
            chooseFile = Intent.createChooser(chooseFile, "Choose a file");
            startActivityForResult(chooseFile, GET_CONTENT_REQUEST_CODE);
        });

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                int[] pos = new int[2];
                videoView.getLocationOnScreen(pos);
                int widthOffset = videoView.getWidth();
                int topOffset = videoView.getMeasuredHeight();
                Rect rectangle = new Rect(pos[0] - widthOffset, pos[1]-topOffset+100, pos[0] + widthOffset, pos[1] + topOffset-640); //TODO: No hardcoding the canvas size to match the video

                canvas.setArea(rectangle);

                seekBar.setMax(videoView.getDuration());
                runSeeker();
            }
        });

        FloatingActionButton clearButton = (FloatingActionButton) root.findViewById(R.id.clearButton);
        clearButton.setOnClickListener(v -> canvas.clearDrawing());

        FloatingActionButton playButton = (FloatingActionButton) root.findViewById(R.id.playButton);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(videoView.isPlaying()){
                    videoView.pause();
                } else {
                    canvas.clearDrawing();
                    videoView.start();
                }
            }
        });

        FloatingActionButton recordButton = (FloatingActionButton) root.findViewById(R.id.recordButton);
        recordButton.setOnClickListener(v -> startRecordingScreen());

        videoSeekHint = root.findViewById(R.id.videoSeekHint);

        seekBar = root.findViewById(R.id.videoSeekBar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(videoView == null){
                    return;
                }

                long minutesProgress = (progress / 1000) / 60;
                long secondsProgress = (progress / 1000) % 60;
                long minutesLength = (videoView.getDuration() / 1000) / 60;
                long secondsLength = (videoView.getDuration() / 1000) % 60;

                String timeLabel = minutesProgress + ":" + getTimeString(secondsProgress) + "/" + getTimeString(minutesLength) + ":" + getTimeString(secondsLength);
                videoSeekHint.setText(timeLabel);

                if (!fromUser) {
                    return;
                }

                videoView.seekTo(progress);
            }

            private String getTimeString(Long time) {
                if (time > 9) {
                    return Long.toString(time);
                }
                return '0' + Long.toString(time);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        return root;
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data == null || resultCode != Activity.RESULT_OK){
            return;
        }

        switch(requestCode){
            case GET_CONTENT_REQUEST_CODE:
                videoView.setVideoURI(data.getData());
                videoView.start();
                break;
            case SCREEN_RECORD_REQUEST_CODE:
                break;
        }
    }


    private void runSeeker() {
        seekerTimer = new Timer();

        final TimerTask task = new TimerTask() {
            int total;
            int currentPosition;

            public void run() {

                getActivity().runOnUiThread(() -> {
                    if(!videoView.isPlaying()){
                        return;
                    }

                    total = videoView.getDuration();
                    currentPosition = videoView.getCurrentPosition();
                    if (currentPosition >= total) {
                        return;
                    }

                    seekBar.setProgress(currentPosition);
                });

            }
        };
        seekerTimer.schedule(task, 1000, 15);
    }

    @Override
    public void onStop() {
        if (seekerTimer != null) {
            seekerTimer.cancel();
            seekerTimer.purge();
        }
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (seekerTimer != null) {
            seekerTimer.cancel();
            seekerTimer.purge();
        }
    }


    private void startRecordingScreen() {
        MediaProjectionManager mediaProjectionManager = (MediaProjectionManager) getContext().getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        Intent permissionIntent = mediaProjectionManager != null ? mediaProjectionManager.createScreenCaptureIntent() : null;
        startActivityForResult(permissionIntent, SCREEN_RECORD_REQUEST_CODE);
    }
}