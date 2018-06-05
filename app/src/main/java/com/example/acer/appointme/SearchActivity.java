package com.example.acer.appointme;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Charana Mayakaduwa
 * 2016139
 * w1626663
 */

public class SearchActivity extends AppCompatActivity {

    static ArrayList<Data> list = new ArrayList<>();
    static ArrayList<Data> listSearch = new ArrayList<>();
    ListView searchList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchList = (ListView)findViewById(R.id.searchList);

        ArrayList<String> list1 = new ArrayList<>();

        for (Data x : list ) {
            list1.add(x.getTitle());
            listSearch.add(x);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, list1);

        searchList.setAdapter(adapter);

        searchList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent newintent = new Intent(SearchActivity.this, ViewActivity.class);
                //newintent.putExtra("Search", listSearch);
                //startActivity(newintent);
            }
        });
    }
}
