package com.example.acer.appointme;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;


/**
 * Charana Mayakaduwa
 * 2016139
 * w1626663
 *
 * Main class of the program. This class directs to other classes accordingly.
 * also displays the calendar to select a date
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //declaring the ui elements
    CalendarView calender;
    TextView textLBL;
    Button createbtn, viewbtn, delbtn, delAllbtn, delSelectbtn, movebtn, searchbtn;

    EditText searchTxt;

    PopupWindow popup;

    //string variable to store the selected date
    private String selectedDate;

    //database object for the database
    Database calendarDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //ui button declaration
        createbtn = (Button) findViewById(R.id.createBTN);
        viewbtn = (Button) findViewById(R.id.viewBTN);
        delbtn = (Button) findViewById(R.id.deleteBTN);
        movebtn = (Button) findViewById(R.id.moveBTN);
        searchbtn = (Button) findViewById(R.id.searchBTN);

        calender = (CalendarView) findViewById(R.id.calendarV);

        searchTxt = (EditText)findViewById(R.id.searchTXT);


        createbtn.setOnClickListener(this);
        viewbtn.setOnClickListener(this);
        delbtn.setOnClickListener(this);
        movebtn.setOnClickListener(this);
        searchbtn.setOnClickListener(this);


        //get the selected date
        calender.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                String dateSelected = simpleDateFormat.format(new GregorianCalendar(year, month, dayOfMonth).getTime());
                selectedDate = dateSelected;
            }
        });


        //to get the current date as the date if nothing selected
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String dateSelected = simpleDateFormat.format(new Date(calender.getDate()));
        selectedDate = dateSelected;
        calendarDB = new Database(this,null,null,1);

    }

    //onclick listeners for buttons
    public void onClick(View v) {


            //for create button
            switch (v.getId()) {
                case R.id.createBTN: {
                    Intent newintent = new Intent(MainActivity.this, CreateActivity.class);
                    newintent.putExtra("Date", selectedDate);
                    startActivity(newintent);
                    //finish();
                    break;
                }

                //for the view button
                case R.id.viewBTN: {
                    Intent newintent = new Intent(MainActivity.this, ViewActivity.class);
                    newintent.putExtra("Date", selectedDate);
                    newintent.putExtra("Change Type", "editAP");
                    startActivity(newintent);
                    break;
                }

                //for the move button
                case R.id.moveBTN: {
                    Intent newintent = new Intent(MainActivity.this, ViewActivity.class);
                    newintent.putExtra("Date", selectedDate);
                    newintent.putExtra("Change Type", "moveAP");
                    startActivity(newintent);
                    break;
                }

                //for the delete button
                case R.id.deleteBTN: {
                    deletePOP(v);
                    break;
                }

                //for the search button
                case R.id.searchBTN: {
                    ArrayList<Data> searched = new ArrayList<>();

                    String searchTerm = searchTxt.getText().toString();

                    List<Data> list = calendarDB.displayAllData();

                    for (int i = 0; i < list.size(); i++){
                        if (list.get(i).getTitle().toLowerCase().contains(searchTerm.toLowerCase())){
                            searched.add(list.get(i));
                        }else if (list.get(i).getDetails().toLowerCase().contains(searchTerm.toLowerCase())){
                            searched.add(list.get(i));
                        }
                    }

                    SearchActivity.list = searched;

                    Intent newintent = new Intent(MainActivity.this, SearchActivity.class);
                    startActivity(newintent);
                    break;
                }

            }
//        }else {
//            Toast.makeText(MainActivity.this, "Please select a date first.", Toast.LENGTH_LONG).show();
//        }
    }


    //popup for the delete button
    public void deletePOP(View v){

        LayoutInflater inflater = (LayoutInflater) MainActivity.this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //initiate the view
        final View layout = inflater.inflate(R.layout.popup_delete,
                (ViewGroup) findViewById(R.id.Layoutview));

        //initialize a size for the popup
        popup = new PopupWindow(layout, 1200, 900 ,  true);
        // display the popup in the center
        popup.showAtLocation(v, Gravity.CENTER, 0, 0);

        //Deletes all the appointments for a given date
        delAllbtn = (Button) layout.findViewById(R.id.delAllBTN);

        delAllbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getBaseContext(),"Deleted all the appointments on "+ selectedDate,Toast.LENGTH_LONG).show();
                calendarDB.deleteAppointmentData(selectedDate);
                popup.dismiss();
            }
        });

        //Opens up the list of appointments for the given date
        delSelectbtn = (Button) layout.findViewById(R.id.delSelBTN);

        delSelectbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getBaseContext() , ViewActivity.class);
                intent.putExtra("Date" , selectedDate ); // format - dd/MM/yyyy
                intent.putExtra("Change Type" , "Delete" );
                startActivity(intent);
                popup.dismiss();
            }
        });
    }
}
