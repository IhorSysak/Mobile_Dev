package com.example.lab4_probe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;

public class AudioPlayerActivity extends AppCompatActivity {

    ImageView play, prev, next, imageView;
    TextView songTitle;
    SeekBar mSeekBarTime, mSeekBarVol;
    Button next_activity;
    static MediaPlayer mMediaPlayer;
    private Runnable runnable;
    private AudioManager mAudioManager;
    int currentIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        play = findViewById(R.id.play);
        prev = findViewById(R.id.prev);
        next = findViewById(R.id.next);
        next_activity = findViewById(R.id.next_activity);
        imageView = findViewById(R.id.image_view);
        songTitle = findViewById(R.id.song_title);
        mSeekBarTime = findViewById(R.id.seek_bar_time);
        mSeekBarVol = findViewById(R.id.seek_bar_volume);

        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        final ArrayList<Integer> song = new ArrayList<>();
        song.add(0, R.raw.music);
        song.add(1, R.raw.music1);

        mMediaPlayer = MediaPlayer.create(getApplicationContext(), song.get(currentIndex));

        int maxV = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int curV = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        mSeekBarVol.setMax(maxV);
        mSeekBarVol.setProgress(curV);

        mSeekBarVol.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        play.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                mSeekBarTime.setMax(mMediaPlayer.getDuration());
                if(mMediaPlayer != null && mMediaPlayer.isPlaying()){
                    mMediaPlayer.pause();
                    play.setImageResource(R.drawable.ic_play);
                } else{
                    mMediaPlayer.start();
                    play.setImageResource(R.drawable.ic_pause);
                }

                songDetails();
            }
        });

        next.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(mMediaPlayer != null){
                    play.setImageResource(R.drawable.ic_pause);
                }
                if (currentIndex < song.size() - 1){
                    currentIndex ++;
                } else{
                    currentIndex = 0;
                }
                if (mMediaPlayer.isPlaying()) {
                    mMediaPlayer.stop();
                }

                mMediaPlayer = MediaPlayer.create(getApplicationContext(), song.get(currentIndex));
                mMediaPlayer.start();
                songDetails();
            }
        });

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMediaPlayer != null){
                    play.setImageResource(R.drawable.ic_pause);
                }

                if (currentIndex > 0){
                    currentIndex--;
                } else{
                    currentIndex = song.size() - 1;
                }

                if (mMediaPlayer.isPlaying()){
                    mMediaPlayer.stop();
                }

                mMediaPlayer = MediaPlayer.create(getApplicationContext(), song.get(currentIndex));
                mMediaPlayer.start();
                songDetails();
            }
        });

        next_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mMediaPlayer.isPlaying()){
                    mMediaPlayer.stop();
                }

                Intent intent = new Intent(AudioPlayerActivity.this, VideoPlayerActivity.class);
                startActivity(intent);
            }
        });

        mSeekBarTime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mMediaPlayer.seekTo(progress);
                    mSeekBarTime.setProgress(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void songDetails(){

        if (currentIndex == 0){
            songTitle.setText("Lil Skies - I");
            imageView.setImageResource(R.drawable.download_2);
        }
        if (currentIndex == 1){
            songTitle.setText("Mirror Masa");
            imageView.setImageResource(R.drawable.download);
        }

        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener(){
            @Override
            public void onPrepared(MediaPlayer mp){
                mSeekBarTime.setMax(mMediaPlayer.getDuration());
                mMediaPlayer.start();
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (mMediaPlayer != null){
                    try {
                        if (mMediaPlayer.isPlaying()){
                            Message message = new Message();
                            message.what = mMediaPlayer.getCurrentPosition();
                            handler.sendMessage(message);
                            Thread.sleep(1000);
                        }
                    }
                    catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    @SuppressLint("HundlerLeak")
    Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg){
            mSeekBarTime.setProgress(msg.what);
        }
    };
}