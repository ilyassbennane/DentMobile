package com.bennane.jaii;



import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;



public class AnimationActivity extends AppCompatActivity {
    ImageView image;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation);

        image = findViewById(R.id.logo);
        Animation rotation = AnimationUtils.loadAnimation(this, R.anim.anim);
        image.startAnimation(rotation);
        Thread t1 = new Thread(){
            @Override
            public void run() {
                try {
                    sleep(5000);
                    Intent intent = new Intent(AnimationActivity.this, LoginActivity.class);
                    startActivity(intent);
                    AnimationActivity.this.finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        t1.start();
    }
}