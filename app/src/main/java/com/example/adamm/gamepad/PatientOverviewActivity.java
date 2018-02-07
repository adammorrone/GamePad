package com.example.adamm.gamepad;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.google.gson.Gson;
import org.w3c.dom.Text;


public class PatientOverviewActivity extends AppCompatActivity  {

    private TextView patientNameText;
    private PatientList masterList = MainActivity.masterList;
    private TextView patientInfoText;
    private int index = -1;



    /** This application's preferences */
    private static SharedPreferences settings;

    /** This application's settings editor*/
    private static SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        patientNameText = findViewById(R.id.patientNameBox);
        patientInfoText = findViewById(R.id.patientInfoBox);

        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        String json = sharedPref.getString("stored_master_list", "");
        Gson gson = new Gson();

        //if (json.equals(""))
        //    masterList = new PatientList();
        //else
        //    masterList = gson.fromJson(json, PatientList.class);

        //masterList = new PatientList();

        Intent iin = getIntent();
        Bundle b = iin.getExtras();
        index = (int)b.get("Patient");

        if (b != null) {
            String name = masterList.getName(index);
            patientNameText.setTextSize(25);
            patientNameText.setText(name);

            String info = masterList.getDOB(index) + "\t\t\t" + masterList.getGender(index);
            patientInfoText.setTextSize(15);
            patientInfoText.setText(info);

        }
    }

    @Override
    public void onPause()
    {
        super.onPause();
        Gson gson = new Gson();
        String json = gson.toJson(masterList);

        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("stored_master_list", json);
        editor.commit();
    }


    public void goto_newGame(View view)
    {
        Intent intent = new Intent(this, NewGame.class);
        startActivity(intent);
    }

    public void goto_editPatient(View view)
    {
        Intent intent = new Intent(PatientOverviewActivity.this, EditPatientInfo.class);
        intent.putExtra("Patient", index);
        startActivity(intent);
    }
}
