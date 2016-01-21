package io.github.suragnair.regapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.service.wallpaper.WallpaperService;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Setting live background

//        ImageView ivLoader = (ImageView) findViewById(R.id.IVloadinganimation);
//        ivLoader.setBackgroundResource(R.layout.live_bg);

//        AnimationDrawable frameAnimation = (AnimationDrawable) ivLoader.getBackground();
//        frameAnimation.start();

    }

    public void two_membered_team_button(View view)
    {
        Intent intent = new Intent(MainActivity.this, TwoMemberedTeamActivity.class);
        startActivity(intent);
    }

    public void three_membered_team_button(View view)
    {
        Intent intent = new Intent(MainActivity.this, ThreeMemberedTeamActivity.class);
        startActivity(intent);
    }

}

