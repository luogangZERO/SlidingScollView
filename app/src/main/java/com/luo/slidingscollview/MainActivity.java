package com.luo.slidingscollview;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SlidingScollView mSlidingScollView = findViewById(R.id.mSlidingScollView);
        mSlidingScollView.setClickActionUp(new SlidingScollView.OnClickActionUp() {
            @Override
            public void onClickActionUp() {
                Toast.makeText(MainActivity.this,"滑动事件完成",Toast.LENGTH_SHORT).show();
            }
        });
    }
}