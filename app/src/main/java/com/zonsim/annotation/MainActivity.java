package com.zonsim.annotation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.zonsim.annotation.butterknife1.OnClick;
import com.zonsim.annotation.butterknife1.ViewInject;
import com.zonsim.annotation.butterknife1.ViewInjectUtils;

public class MainActivity extends AppCompatActivity {
    @ViewInject(R.id.tv)
    TextView textView;
    
    @OnClick(R.id.tv)
    void clickTextView(View view) {
        Toast.makeText(this,"kkkk",Toast.LENGTH_SHORT).show();
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewInjectUtils.inject(this);
        //TextView textView = (TextView) findViewById(R.id.tv);
        
        textView.setText("哈哈哈哈哈哈");
        
    }
}
