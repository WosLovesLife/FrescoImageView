package com.example.leonard.frescoimageview;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager manager = getSupportFragmentManager();
        if (manager.findFragmentById(R.id.container) == null) {
            ImagePickerFragment fragment = ImagePickerFragment.newInstance();
            manager.beginTransaction().add(R.id.container, fragment).commit();
        }
    }
}
