package com.example.everyware.ui.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Rect;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.everyware.R;
import com.example.everyware.ui.Models.EditorCanvas;
import com.example.everyware.ui.Models.StretchedVideoView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    private StretchedVideoView videoView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        videoView = (StretchedVideoView) root.findViewById(R.id.videoView);

        EditorCanvas canvas = (EditorCanvas) root.findViewById(R.id.canvas);

        Button selectVideoButton = (Button) root.findViewById(R.id.selectVideoButton);
        selectVideoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
                chooseFile.setType("video/*");
                chooseFile = Intent.createChooser(chooseFile, "Choose a file");
                startActivityForResult(chooseFile, 9999);
            }
        });

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                int[] pos = new int[2];
                videoView.getLocationOnScreen(pos);

//                DisplayMetrics metrics = new DisplayMetrics();
//                getActivity().getWindowManager().getCurrentWindowMetrics();
                int topOffset = 370; //Offset is caused by the status bar and notification bar. TODO: No hardcode
                Rect rectangle = new Rect(pos[0], pos[1]-topOffset, pos[0] + videoView.getWidth(), pos[1] + videoView.getHeight()-topOffset);
                canvas.setArea(rectangle);
            }
        });

        FloatingActionButton clearButton = (FloatingActionButton) root.findViewById(R.id.clearButton);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                canvas.clearDrawing();
            }
        });

        FloatingActionButton playButton = (FloatingActionButton) root.findViewById(R.id.playButton);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(videoView.isPlaying()){
                    videoView.pause();
                } else {
                    videoView.start();
                }
            }
        });

        return root;
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data == null){
            return;
        }

        videoView.setVideoURI(data.getData());
//        videoView.setVideoSize(videoView.getWidth());
        videoView.start();

    }

}