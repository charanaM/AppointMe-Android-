package com.example.acer.appointme;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Charana Mayakaduwa
 * 2016139
 * w1626663
 *
 * This class
 */

public class ViewActivity extends AppCompatActivity{

    public static final String TAG = "ViewActivity";

    Database calendarDB;
    //private ListView mListView;

    String datevalue, apmntNo, Type;

    EditText IDtxt;
    Button editbtn;

    ArrayAdapter arrayAdapt;
    ListView listV;

    ArrayList<String> arrayList;
    List<Data> listArray;

    PopupWindow popupWindow;
    Button updateBtn;
    EditText titleET, timeET, detailsET;


    Button moveBtn;
    CalendarView calendarView;
    String popupDate;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        IDtxt = (EditText)findViewById(R.id.idTXT);
        editbtn = (Button)findViewById(R.id.editBTN);

        //mListView = (ListView)findViewById(R.id.listV);
        //calendarDB = new Database(this);

        //get the intents
        Intent intent = getIntent();
        //get the date intent
        datevalue = intent.getStringExtra("Date");
        //get the type intent
        Type = intent.getStringExtra("Change Type");

       // populateListView();

        if(Type.equals("Delete")){
            editbtn.setText("DELETE");


        }else if(Type.equals("editAP")){
            editbtn.setText("EDIT");

        }else if(Type.equals("moveAP")){
            editbtn.setText("MOVE");

        }else {
            Toast.makeText(getBaseContext() ,"Oops! Something went wrong!" , Toast.LENGTH_SHORT ).show();
            finish();
        }

        //creates an instance of the Database
        calendarDB = new Database(this, null, null, 1);

        listArray = calendarDB.displayAppointmentData(datevalue);
        arrayList = new ArrayList<>();

        for(int j=0 ; j<listArray.size() ; j++){

            arrayList.add(j+1 + ". " + listArray.get(j).getTime() + " " + listArray.get(j).getTitle());
            //Toast.makeText(getBaseContext() ,arrayList.get(j) , Toast.LENGTH_SHORT ).show();

        }

        arrayAdapt = new ArrayAdapter<String>(this, R.layout.activity_listview, arrayList);

        listV = (ListView) findViewById(R.id.listV);
        listV.setAdapter(arrayAdapt);

        editbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Hides the virtual keyboard when the buttons are clicked
                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);

                apmntNo = IDtxt.getText().toString();
                if(apmntNo.equals(null) || apmntNo.equals("")){
                    editbtn.setError("Please select a valid appointment number");
                    IDtxt.setText("");
                    return;
                }else{
                    try{

                        //if the change type is delete
                        if(Type.equals("Delete")) {

                            errorDialog("Would you like to delete event : “ " +
                                    listArray.get(Integer.parseInt(apmntNo) - 1).getTitle() + " ”?");

                        } else if (Type.equals("editAP")){

                            Toast.makeText(ViewActivity.this, "edit", Toast.LENGTH_LONG).show();
                            updateAppointmentPopup(v);

                        } else if (Type.equals("moveAP")){

                            moveAppointmentPopup(v);
                            //finish();
                        }
                        IDtxt.setText("");
                    }catch (IndexOutOfBoundsException e){
                        IDtxt.setText("");
                        Toast.makeText(getBaseContext(), "There's no appointment numbered " + apmntNo +
                                ". Please try again with a valid number." , Toast.LENGTH_SHORT).show();
                    }catch (Exception e){
                        IDtxt.setText("");
                        Toast.makeText(getBaseContext(), "Invalid input. Please try again with a valid number." , Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

    }




    public void errorDialog(String error)
    {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(error);
        builder.setCancelable(true);

        builder.setPositiveButton(
                "YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(getBaseContext(), "Deleted the " +
                                listArray.get(Integer.parseInt(apmntNo) - 1).getTitle() +
                                " appointment.", Toast.LENGTH_SHORT).show();
                        calendarDB.deleteAppointmentData(datevalue , listArray.get(Integer.parseInt(apmntNo)-1).getTitle());
                        //adapter.notifyDataSetChanged(); //refreshes the list, NOT WORKING
                        dialog.dismiss();

                        //bad way to refresh
                        finish();
                        startActivity(getIntent());
                    }
                });
        builder.setNegativeButton(
                "NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }

    private void updateAppointmentPopup (View v) {

        try {
            //get an instance of layoutinflater
            LayoutInflater inflater = (LayoutInflater) ViewActivity.this
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //initiate the view
            final View layout = inflater.inflate(R.layout.popup_update,
                    (ViewGroup) findViewById(R.id.updatePopupView));

            //initialize a size for the popup
            popupWindow = new PopupWindow(layout, 1200, 1650 ,  true);
            // display the popup in the center
            popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);

            //initialising the update popup button and edit texts
            titleET = (EditText) layout.findViewById(R.id.updateTitleEditText);
            timeET = (EditText) layout.findViewById(R.id.updateTimeEditText);
            detailsET = (EditText) layout.findViewById(R.id.updateDetailsEditText);

            //Updates the selected appointment
            updateBtn = (Button) layout.findViewById(R.id.updateButton);
            updateBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try {
                        int success = calendarDB.updateAppointmentData(listArray.get(Integer.parseInt(apmntNo) - 1),
                                timeET.getText().toString(), titleET.getText().toString(), detailsET.getText().toString());

                        if (success == 1) {

                            Toast.makeText(getBaseContext(), "Successfully updated the appointment", Toast.LENGTH_LONG).show();

                        } else if (success == -1) {

                            Toast.makeText(getBaseContext(), "There's no appointment numbered " + apmntNo +
                                    ". Please try again with a valid number.", Toast.LENGTH_SHORT).show();

                        }

                        //refreshes the page
                        finish();
                        startActivity(getIntent());

                    }catch (IndexOutOfBoundsException e){

                        Toast.makeText(getBaseContext(), "Couldn't find the specified appointment in the database." , Toast.LENGTH_SHORT).show();

                    }catch (Exception e){

                        Toast.makeText(getBaseContext(), "Invalid input. Please try again with a valid number." , Toast.LENGTH_SHORT).show();
                    }
                    timeET.setText(""); titleET.setText(""); detailsET.setText("");
                    popupWindow.dismiss();
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * This junction creates a popup window with a calender view and a button
     *
     * @param v The current view instance is passed
     */
    private void moveAppointmentPopup (View v) {

        try {
            //get an instance of layoutinflater
            LayoutInflater inflater = (LayoutInflater) ViewActivity.this
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //initiate the view
            final View layout = inflater.inflate(R.layout.popup_move,
                    (ViewGroup) findViewById(R.id.movePopupView));

            //initialize a size for the popup
            popupWindow = new PopupWindow(layout, 1200, 1800 ,  true);
            // display the popup in the center
            popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);

            calendarView = (CalendarView) layout.findViewById(R.id.calendarViewPopup);
            calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                @Override
                public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    String dateSelected = simpleDateFormat.format(new GregorianCalendar(year, month, dayOfMonth).getTime());
                    popupDate = dateSelected;
                    //Toast.makeText(getBaseContext(),popupDate,Toast.LENGTH_SHORT).show();
                }
            });

            //Updates the selected appointment
            moveBtn = (Button) layout.findViewById(R.id.moveButton);
            moveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try {

                        calendarDB.moveAppointmentData(listArray.get(Integer.parseInt(apmntNo) - 1) , popupDate);

                        //refreshes the page
                        finish();
                        startActivity(getIntent());

                    }catch (IndexOutOfBoundsException e){

                        Toast.makeText(getBaseContext(), "Couldn't find the specified appointment in the database." , Toast.LENGTH_SHORT).show();

                    }catch (Exception e){

                        Toast.makeText(getBaseContext(), "Invalid input. Please try again with a valid number." , Toast.LENGTH_SHORT).show();
                    }
                    popupWindow.dismiss();
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
