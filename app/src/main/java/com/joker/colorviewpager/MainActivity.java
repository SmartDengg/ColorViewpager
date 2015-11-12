package com.joker.colorviewpager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Joker on 2015/11/12.
 */
public class MainActivity extends AppCompatActivity {

  public static void navigateToMain(AppCompatActivity startingActivity) {
    Intent intent = new Intent(startingActivity, MainActivity.class);
    startingActivity.startActivity(intent);
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main_layout);
  }
}
