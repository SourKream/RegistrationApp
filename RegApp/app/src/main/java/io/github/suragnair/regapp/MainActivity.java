package io.github.suragnair.regapp;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.service.wallpaper.WallpaperService;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView ivLoader = (ImageView) findViewById(R.id.IVloadinganimation);
        ivLoader.setBackgroundResource(R.layout.live_bg);


        AnimationDrawable frameAnimation = (AnimationDrawable) ivLoader.getBackground();
        frameAnimation.start();
    }

}
