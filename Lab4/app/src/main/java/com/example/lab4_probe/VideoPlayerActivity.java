package com.example.lab4_probe;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.jaiselrahman.filepicker.activity.FilePickerActivity;
import com.jaiselrahman.filepicker.config.Configurations;
import com.jaiselrahman.filepicker.model.MediaFile;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.util.ArrayList;

public class VideoPlayerActivity extends AppCompatActivity {

    private YouTubePlayerView youTubePlayerView;
    Button button_play, btVideo;
    VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        videoView = findViewById(R.id.video_view);
        button_play = findViewById(R.id.play_video);

        btVideo = findViewById(R.id.bt_video);

        String videoPath = "android.resource://" + getPackageName() + "/" + R.raw.video;
        Uri uri = Uri.parse(videoPath);
        videoView.setVideoURI(uri);

        btVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(VideoPlayerActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(VideoPlayerActivity.this, new String[]{Manifest.permission.CAMERA}, 2);
                } else {
                    videoPicker();
                }
            }
        });

        MediaController mediaController = new MediaController(this);
        videoView.setMediaController(mediaController);
        mediaController.setAnchorView(videoView);

        Intent intent = new Intent(this, YouTubeActivity.class);
        button_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText editText = (EditText) findViewById(R.id.URL);
                if (editText.getText().toString().trim().length() <= 0){
                    Toast toast = Toast.makeText(VideoPlayerActivity.this, "URL is empty", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    String message = editText.getText().toString();
                    intent.putExtra("url", message);
                    startActivity(intent);
                }
            }
        });
    }

    private void videoPicker() {
        Intent intent = new Intent(VideoPlayerActivity.this, FilePickerActivity.class);
        intent.putExtra(FilePickerActivity.CONFIGS, new Configurations.Builder().setCheckPermission(true).setShowVideos(true).setShowImages(false).enableVideoCapture(true).setMaxSelection(1).setSkipZeroSizeFiles(true).build());
        startActivityForResult(intent, 104);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)){
            if (requestCode == 1){
                imagePicker();
            } else {
                videoPicker();
            }
        } else{
            Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null){
            ArrayList<MediaFile> mediaFiles = data.getParcelableArrayListExtra(FilePickerActivity.MEDIA_FILES);
            String path = mediaFiles.get(0).getPath();
            switch (requestCode){
                case 104:
                    String s = path;
                    Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();

                    String videoPath = s;
                    Uri uri = Uri.parse(videoPath);
                    videoView.setVideoURI(uri);

                    break;
            }
        }
    }

    private void imagePicker(){
        Intent intent = new Intent(VideoPlayerActivity.this, FilePickerActivity.class);
        intent.putExtra(FilePickerActivity.CONFIGS, new Configurations.Builder().setCheckPermission(true).setShowVideos(false).setShowImages(true).enableVideoCapture(true).setMaxSelection(1).setSkipZeroSizeFiles(true).build());
        startActivityForResult(intent, 101);
    }
}