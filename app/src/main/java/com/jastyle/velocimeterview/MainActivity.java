package com.jastyle.velocimeterview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.jastyle.velocimeterview.velocimeter.VelocimeterView;

public class MainActivity extends AppCompatActivity {
    private VelocimeterView mVelocimeterView;
    private Button start_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findView();
    }
    private void findView() {
        start_btn = (Button) findViewById(R.id.start_btn);
        mVelocimeterView = (VelocimeterView) findViewById(R.id.velocimter);
        mVelocimeterView.setCreditRank("信用良好");
        mVelocimeterView.setReportTime("2017.1.21");
        mVelocimeterView.setValue(500,true);
        start_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mVelocimeterView.setCreditRank("信用极好");
                mVelocimeterView.setReportTime("2017.1.21");
                mVelocimeterView.setValue(890,true);
            }
        });
    }

}
