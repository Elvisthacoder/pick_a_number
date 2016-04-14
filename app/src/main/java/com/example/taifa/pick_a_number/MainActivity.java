package com.example.taifa.pick_a_number;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.NumberPicker;

public class MainActivity extends AppCompatActivity implements OnValueChangeListener {


    private ActualNumberPicker mTestPicker;
    private ViewGroup mContentRoot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTestPicker = (ActualNumberPicker) findViewById(R.id.actual_picker2);
        mContentRoot = (ViewGroup) findViewById(android.R.id.content);
        mTestPicker.setListener(this);
    }
}
