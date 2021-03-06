package com.example.adamm.gamepad;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jjoe64.graphview.series.DataPoint;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Scanner;

import android.media.*;
import android.widget.Toast;

/**
 * Created by adamm on 2/28/2018.
 */

public class ResultsActivity extends AppCompatActivity {

    public int index = -1;
    private PatientList masterList = MainActivity.masterList;
    public  TextView scoreText;
    public  TextView highscoreLabel;
    public String address;
    //public EditText infoText;
    //changed editText to textview
    public TextView infoText;
    MediaPlayer mediaPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        mediaPlayer = MediaPlayer.create(this, R.raw.high_score_cheer);
        mediaPlayer.start(); // no need to call prepare(); create() does that for you

        scoreText = findViewById(R.id.scoreView);
        //infoText = findViewById(R.id.infoText);
        highscoreLabel = findViewById(R.id.highscoreView);


        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        String json = sharedPref.getString("stored_master_list", "");
        Gson gson = new Gson();

        Intent iin = getIntent();
        Bundle b = iin.getExtras();
        address = b.get("Address").toString();
        index = (int)b.get("Patient");
        int _score_ = (int)b.get("Score");
        int _throws_ = (int)b.get("Throws");
        //int _weight_ = Integer.parseInt((String)b.get("Weight"));
        //double _distance_ = Double.parseDouble(((String)b.get("Distance")).substring(0, 2));
        //int _time_ = Integer.parseInt(((String)b.get("Time")).substring(0, 1));

        // Parsing int with Scanner class
        String weight = b.get("Weight").toString();
        Scanner in = new Scanner(weight).useDelimiter("[^0-9]+");
        int _weight_ = in.nextInt();

        String distance = b.get("Distance").toString();
        in = new Scanner(distance).useDelimiter("[^0-9]+");
        double _distance_ = in.nextInt();

        String time = b.get("Time").toString();
        in = new Scanner(time).useDelimiter("[^0-9]+");
        int _time_ = in.nextInt();

        int _height_ = masterList.getPatient(index).getHeight();

        ScoreRecord scoreRecord = new ScoreRecord(_score_, "Standard", Calendar.getInstance(), _distance_, _throws_, _time_, _weight_, _height_);

        if(_score_ > masterList.getPatient(index).getHighScore())
            highscoreLabel.setText("NEW HIGH SCORE!!!");

        if(_score_ == masterList.getPatient(index).getHighScore())
            highscoreLabel.setText("You tied your high score!");

        if(_score_ != 0)
            masterList.getPatient(index).addScore(scoreRecord);

        try {
            saveChanges();
        } catch(Exception ex) {

            Toast.makeText(getApplicationContext(), "Something went wrong. The score has not been recorded.",
                    Toast.LENGTH_LONG).show();
        }

        scoreText.setText("YOU SCORED " + (int)scoreRecord.getScore() + " POINTS");
        //infoText.setText(scoreRecord.toString());
        //temp
        // setText Double.toString(scoreRecord.getScore())
        //infoText.setText("NULL");



        ArrayList<ScoreRecord> scores = masterList.getPatient(index).getScores();
        DataPoint[] data = new DataPoint[scores.size()];
        for(int i = 0; i < scores.size(); i++) {
            data[i] = new DataPoint(i, scores.get(i).getScore());
        }
    }


    public void goto_main(View view)
    {
        Intent intent = new Intent(ResultsActivity.this, MainActivity.class);
        intent.putExtra("Patient", index);
        startActivity(intent);
    }
    public void goto_newGame(View view)
    {
        Intent intent = new Intent(ResultsActivity.this, NewGame.class);
        intent.putExtra("Patient", index);
        intent.putExtra("Address", address);
        startActivity(intent);
    }

    public void saveChanges()
    {
        MainActivity.saveMasterList(this.getApplicationContext(), masterList);
    }
}
