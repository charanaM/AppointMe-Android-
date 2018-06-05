package com.example.acer.appointme;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Charana Mayakaduwa
 * 2016139
 * w1626663
 *
 * Class for creating appointment.
 *  gets user inputs for the appointment and pass it to the database
 */

public class CreateActivity extends AppCompatActivity implements View.OnClickListener {



    EditText titleTxt, timeTxt, detailsTxt, thesauruslbl;
    Button saveBtn, thesaurusBtn;

    String datevalue;

    Database calendarDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);


        saveBtn = (Button)findViewById(R.id.saveBTN);
        saveBtn.setOnClickListener(this);

        thesaurusBtn = (Button)findViewById(R.id.thesaurusBTN);
        thesaurusBtn.setOnClickListener(this);

        titleTxt = (EditText)findViewById(R.id.titleTXT);
        timeTxt = (EditText)findViewById(R.id.timeTXT);
        detailsTxt = (EditText)findViewById(R.id.detailsTXT);
        thesauruslbl = (EditText)findViewById(R.id.thesaurusLBL);


        Intent intent = getIntent();


        datevalue = intent.getStringExtra("Date");

        calendarDB = new Database(this, null, null, 1);




    }

    @Override
    public void onClick(View view) {

        //Hides the virtual keyboard when the buttons are clicked
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);



        switch (view.getId()) {

            case R.id.saveBTN: {


                String time = timeTxt.getText().toString();
                String title = titleTxt.getText().toString();
                String details = detailsTxt.getText().toString();

                if (TextUtils.isEmpty(time)) {

                    timeTxt.setError("Please set a time for the appointment.");
                    return;

                } else if (TextUtils.isEmpty(title)) {

                    titleTxt.setError("Please set a title for the appointment.");
                    return;

                } else if (TextUtils.isEmpty(details)) {

                    detailsTxt.setError("Please set a details for the appointment.");
                    return;

                } else {

                    Data data = new Data(datevalue, time, title, details);
                    int i = calendarDB.addAppointmentData(data);
                    if (i == 1) {

                       // errorDialog("Appointment " + title + " on " + datevalue + " was created successfully.");
//String dbString = calendarDB.DBToString();
                       // Toast.makeText(getBaseContext(),dbString,Toast.LENGTH_SHORT).show();
                        //printDatabase();

                        Toast.makeText(CreateActivity.this, "Success", Toast.LENGTH_LONG).show();

                    } else if (i == -1) {


                        Toast.makeText(CreateActivity.this, "Failed. Title already exist", Toast.LENGTH_LONG).show();

                    }


                }
                //Toast.makeText(CreateActivity.this, , Toast.LENGTH_LONG).show();
                break;


            }

            case R.id.thesaurusBTN: {

                 Thesaurus thesaurus = new Thesaurus(thesauruslbl.getText().toString());
                 Thread thrd = new Thread(thesaurus);
                 thrd.start();

            }

        }

    }


}
