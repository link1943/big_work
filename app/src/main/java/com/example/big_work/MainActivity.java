package com.example.big_work;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Environment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ByListFragment.FragmentInteraction,Runnable {
    private ImageButton btest1,btest2,btest3;
    SeekBar seekBar;
    TextView musicStatus,musicTime;
    GramophoneView gramophoneView;


    private SimpleDateFormat time = new SimpleDateFormat("mm:ss");

    MediaPlayer player = null;
    MyPageAdapter myPageAdapter;

    File[] aa;
    String[] muName;
    int len,posi;

    int pf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        seekBar = findViewById(R.id.MusicSeekBar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    player.seekTo(seekBar.getProgress()*player.getDuration()/100);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        player = new MediaPlayer();
        player.reset();


        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/testmusic");
        aa = file.listFiles();
        muName = file.list();
        len = aa.length;
        posi = 0;



        ViewPager viewPager = findViewById(R.id.viewpager);
        myPageAdapter = new MyPageAdapter(getSupportFragmentManager());
        viewPager.setAdapter(myPageAdapter);


        pf = 0;//播放还是暂停的标志位
        btest1 = findViewById(R.id.imageButton2);
        btest1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pf == 0 ) {
                    try {
                        player.stop();
                        player.reset();
                        player.setDataSource(aa[0].getAbsolutePath());
                        player.prepare();
                        player.start();
                        pf =1;
                        btest1.setBackgroundDrawable(getResources().getDrawable(R.drawable.stop));
                        //gramophoneView.setPlaying(!gramophoneView.getPlaying());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else if(pf==1){
                    player.pause();
                    pf=2;
                    btest1.setBackgroundDrawable(getResources().getDrawable(R.drawable.play));
                    //gramophoneView.setPlaying(!gramophoneView.getPlaying());
                }
                else {
                    player.start();
                    pf = 1;
                    btest1.setBackgroundDrawable(getResources().getDrawable(R.drawable.stop));
                    //gramophoneView.setPlaying(!gramophoneView.getPlaying());
                }
            }
        });

        btest2 = findViewById(R.id.imageButton);
        btest2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player.stop();
                player.reset();
                posi--;
                if (posi<0)
                    posi+=len;
                try {
                    player.setDataSource(aa[posi].getAbsolutePath());
                    player.prepare();
                    player.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                pf = 1;
            }
        });

        btest3 = findViewById(R.id.imageButton3);
        btest3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player.stop();
                player.reset();
                posi++;
                if (posi>=len)
                    posi=0;
                System.out.println(posi);
                try {
                    player.setDataSource(aa[posi].getAbsolutePath());
                    player.prepare();
                    player.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                pf =1;
            }
        });


        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {

                while (true) {
                    try {
                        Thread.sleep(800);
                        gramophoneView = findViewById(R.id.gramophone_view);
                        if (pf == 1) {
                            gramophoneView.setPlaying(true);
                        } else
                            gramophoneView.setPlaying(false);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread2.start();

        Thread thread = new Thread(this);
        thread.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu1, menu);

        MenuItem item1 = menu.findItem(R.id.item12);
        item1.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                return true;
            }
        });

        return true;
    }

    //选择歌曲的函数
    @Override
    public void process(int str) {
        pf = 1;
        btest1 = findViewById(R.id.imageButton2);
        btest1.setBackgroundDrawable(getResources().getDrawable(R.drawable.stop));
        player.stop();
        player.reset();
        posi = str;
        try {
            player.setDataSource(aa[str].getAbsolutePath());
            player.prepare();
            player.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void run() {
        while (true) {
            int a = player.getCurrentPosition(),b = player.getDuration();
            try {
                musicStatus = findViewById(R.id.musicName);
                musicTime = findViewById(R.id.musicTime);
                musicTime.setText(time.format(a) + "/" + time.format(b));
                musicStatus.setText(muName[posi]);
                //System.out.println(player.getCurrentPosition()+"\t"+player.getDuration());
                System.out.println("out");
            }catch (Exception e){
                ;
            }

            seekBar.setProgress((int)((double)(a)/b*100));
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }




}