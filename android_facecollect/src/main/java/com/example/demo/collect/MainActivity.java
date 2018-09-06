package com.example.demo.collect;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private MovieRecorderView movieRV;
    private Button startBtn;
    private Button stopBtn;
    int position;

    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        initEvents();
    }
    private void initViews()
    {
        movieRV=(MovieRecorderView)findViewById(R.id.moive_rv);
        //录制
        startBtn=(Button)findViewById(R.id.start_btn);
        stopBtn=(Button)findViewById(R.id.stop_btn);
    }

    private void initEvents()
    {
        //开始录制
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                movieRV.record(new MovieRecorderView.OnRecordFinishListener() {
                    @Override
                    public void onRecordFinish() {

                    }
                });
            }
        });
        //停止录制
        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                movieRV.stop();
            }
        });
    }
}
