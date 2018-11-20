package com.example.mohamed.task;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        BlankFragment blankFragment=new BlankFragment();
        fragmentTransaction.replace(R.id.frame,blankFragment);
        fragmentTransaction.commit();
    }

}
